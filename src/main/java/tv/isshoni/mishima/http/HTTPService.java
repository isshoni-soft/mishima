package tv.isshoni.mishima.http;

import tv.isshoni.araragi.data.Pair;
import tv.isshoni.araragi.data.collection.map.SubMap;
import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.mishima.event.ConnectionEvent;
import tv.isshoni.mishima.exception.HTTPProtocolException;
import tv.isshoni.mishima.http.protocol.IProtocol;
import tv.isshoni.mishima.http.protocol.ProtocolService;
import tv.isshoni.winry.api.annotation.Event;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.Injected;
import tv.isshoni.winry.api.annotation.Listener;
import tv.isshoni.winry.api.annotation.parameter.Context;
import tv.isshoni.winry.api.annotation.transformer.Async;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.internal.model.meta.IAnnotatedMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Injected
public class HTTPService {

    private final AraragiLogger logger;

    @Inject private ProtocolService protocolService;

    private final IWinryContext context;

    private final SubMap<HTTPMethod, String, HTTPHandler, HashMap<String, HTTPHandler>> handlerMap;

    public HTTPService(@Context IWinryContext context) {
        this.context = context;
        this.logger = this.context.createLogger("HTTPService");
        this.handlerMap = new SubMap<>(HashMap::new);
    }

    public void registerHTTPHandler(HTTPMethod httpMethod, Object object, IAnnotatedMethod method, String path) {
        if (this.handlerMap.containsKey(httpMethod, path)) {
            throw new IllegalStateException("cannot register duplicate path: " + path + " for method: " + httpMethod);
        }

        this.handlerMap.put(httpMethod, Pair.of(path, new HTTPHandler(this.context, method, object)));
        logger.info("Registered HTTP Handler: " + httpMethod + " " + path + " -- " + method.getDisplay());
    }

    public void execute(HTTPMethod method, String path, Map<String, Object> data) {
        this.handlerMap.get(method).get(path).execute(data);
    }

    @Listener(ConnectionEvent.class)
    @Async
    public void handleNewConnection(@Event ConnectionEvent event) throws IOException {
        Socket client = event.getClient();

        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        // PROCESS FIRST LINE
        String line = in.readLine();
        String[] tokens = line.split(" ");

        if (tokens.length != 3) {
            throw new HTTPProtocolException("malformed first line");
        }

        HTTPMethod method;
        try {
            method = HTTPMethod.valueOf(tokens[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new HTTPProtocolException(tokens[0] + " is not a parsable HTTP method");
        }

        String[] versionTokens = tokens[2].split("/");

        if (versionTokens.length != 2) {
            throw new HTTPProtocolException(tokens[2] + " is not a parsable HTTP version!");
        }

        String httpVersion = versionTokens[1];
        IncomingHTTPRequest request = new IncomingHTTPRequest(method, tokens[1], httpVersion, client, in);

        Optional<IProtocol> protocolOptional = this.protocolService.getProtocol(request.getHTTPVersion());

        logger.debug("Attempting handoff to protocol...");
        if (protocolOptional.isPresent()) {
            protocolOptional.get().handleConnection(request);
        } else {
            throw new HTTPProtocolException(tokens[2] + " is not a supported HTTP version!");
        }
    }
}
