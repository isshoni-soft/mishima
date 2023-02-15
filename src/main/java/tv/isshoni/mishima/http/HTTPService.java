package tv.isshoni.mishima.http;

import tv.isshoni.araragi.data.Pair;
import tv.isshoni.araragi.data.collection.map.SubMap;
import tv.isshoni.araragi.data.collection.map.TypeMap;
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
import tv.isshoni.winry.api.meta.IAnnotatedMethod;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Injected
public class HTTPService {

    private final AraragiLogger logger;

    @Inject private ProtocolService protocolService;

    private final IWinryContext context;

    private final SubMap<HTTPMethod, String, HTTPHandler, HashMap<String, HTTPHandler>> handlerMap;

    private final Map<Class<?>, IHTTPSerializer<?>> serializers;

    public HTTPService(@Context IWinryContext context) {
        this.context = context;
        this.logger = this.context.createLogger("HTTPService");
        this.serializers = new TypeMap<>();
        this.handlerMap = new SubMap<>(HashMap::new);

        registerHTTPSerializer(String.class, s -> s);
    }

    public void registerHTTPHandler(HTTPMethod httpMethod, MIMEType mimeType, Object object, IAnnotatedMethod method, String path) {
        if (this.handlerMap.containsKey(httpMethod, path)) {
            throw new IllegalStateException("cannot register duplicate path: " + path + " for method: " + httpMethod);
        }

        this.handlerMap.put(httpMethod, Pair.of(path, new HTTPHandler(this.context, mimeType, method, object)));
        logger.info("Registered HTTP Handler: " + httpMethod + " " + path + " -- " + method.getDisplay());
    }

    public boolean hasSerializer(Class<?> type) {
        return this.serializers.containsKey(type);
    }

    public <O> IHTTPSerializer<O> getSerializer(Class<O> type) {
        return (IHTTPSerializer<O>) this.serializers.get(type);
    }

    public <O> void registerHTTPSerializer(Class<O> type, IHTTPSerializer<O> serializer) {
        this.serializers.put(type, serializer);
    }

    public boolean hasHandler(HTTPMethod method, String path) {
        return this.handlerMap.containsKey(method, path);
    }

    public HTTPHandler getHandler(HTTPMethod method, String path) {
        return this.handlerMap.get(method).get(path);
    }

    public <R> R execute(HTTPMethod method, String path, Map<String, Object> data) {
        return getHandler(method, path).execute(data);
    }

    @Listener(ConnectionEvent.class)
    @Async
    public void handleNewConnection(@Event ConnectionEvent event) throws IOException {
        HTTPConnection connection = event.getConnection();

        // PROCESS FIRST LINE
        String line = connection.readLine();
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
        HTTPRequest request = new HTTPRequest(method, tokens[1], httpVersion);
        Optional<IProtocol> protocolOptional = this.protocolService.getProtocol(request.getHTTPVersion());

        logger.debug("Attempting handoff to protocol...");
        if (protocolOptional.isPresent()) {
            protocolOptional.get().handleConnection(request, connection);
        } else {
            throw new HTTPProtocolException(tokens[2] + " is not a supported HTTP version!");
        }
    }
}
