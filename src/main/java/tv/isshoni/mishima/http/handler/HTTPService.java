package tv.isshoni.mishima.http.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import tv.isshoni.araragi.data.Pair;
import tv.isshoni.araragi.data.collection.map.SubMap;
import tv.isshoni.araragi.data.collection.map.TypeMap;
import tv.isshoni.araragi.data.collection.map.token.TokenMap;
import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.araragi.stream.Streams;
import tv.isshoni.araragi.string.format.StringFormatter;
import tv.isshoni.mishima.event.ConnectionEvent;
import tv.isshoni.mishima.exception.HTTPFormatException;
import tv.isshoni.mishima.http.HTTPConnection;
import tv.isshoni.mishima.http.HTTPHeaders;
import tv.isshoni.mishima.http.HTTPMethod;
import tv.isshoni.mishima.http.HTTPRequest;
import tv.isshoni.mishima.http.IHTTPDeserializer;
import tv.isshoni.mishima.http.IHTTPSerializer;
import tv.isshoni.mishima.http.MIMEType;
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
import tv.isshoni.winry.api.service.ObjectFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Injected
public class HTTPService {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final AraragiLogger logger;

    @Inject private ProtocolService protocolService;

    private final IWinryContext context;

    private final SubMap<HTTPMethod, String, HTTPHandler, TokenMap<HTTPHandler>> handlerMap;

    private final TypeMap<Class<?>, IHTTPSerializer<?>> serializers;

    private final TypeMap<Class<?>, IHTTPDeserializer<?>> deserializers;

    public static StringFormatter makeNewFormatter() {
        return new StringFormatter("{", "}");
    }

    public HTTPService(@Context IWinryContext context) {
        this.context = context;
        this.logger = this.context.createLogger("HTTPService");
        this.serializers = new TypeMap<>();
        this.deserializers = new TypeMap<>();
        this.handlerMap = new SubMap<>(() -> new TokenMap<>(makeNewFormatter()));

        registerHTTPSerializer(String.class, s -> s);
        registerHTTPSerializer(JsonElement.class, GSON::toJson);
    }

    public void registerHTTPHandler(HTTPMethod httpMethod, MIMEType mimeType, Object object, IAnnotatedMethod method, String path) {
        if (this.handlerMap.containsKey(httpMethod, path)) {
            throw new IllegalStateException("cannot register duplicate path: " + path + " for method: " + httpMethod);
        }

        if (!path.endsWith("/")) {
            path = path + "/";
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

    public boolean hasDeserializer(Class<?> type) {
        return this.deserializers.containsKey(type);
    }

    public <O> IHTTPDeserializer<O> getDeserializer(Class<O> type) {
        return (IHTTPDeserializer<O>) this.deserializers.get(type);
    }

    public <O> void registerHTTPDeserializer(Class<O> type, IHTTPDeserializer<O> deserializer) {
        this.deserializers.put(type, deserializer);
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

    public TokenMap<HTTPHandler> getTokenMapForMethod(HTTPMethod method) {
        return this.handlerMap.getOrDefault(method);
    }

    @Listener(ConnectionEvent.class)
    @Async
    public void handleNewConnection(@Event ConnectionEvent event, @Inject ObjectFactory factory) throws IOException {
        HTTPConnection connection = event.getConnection();

        // PROCESS FIRST LINE
        String line = connection.readLine();
        String[] tokens = line.split(" ");

        if (tokens.length != 3) {
            throw new HTTPFormatException("malformed first line");
        }

        HTTPMethod method;
        try {
            method = HTTPMethod.valueOf(tokens[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new HTTPFormatException(tokens[0] + " is not a parsable HTTP method");
        }

        HTTPHeaders requestHeaders = new HTTPHeaders();

        // read until body separator
        String dataLine;
        do {
            dataLine = connection.readLine();

            if (dataLine.contains(":")) {
                String[] headerTokens = dataLine.split(": ");

                requestHeaders.addHeader(headerTokens[0], headerTokens[1]);
            }
        } while (dataLine.length() != 0);

        String body = "";
        if (requestHeaders.hasHeader(HTTPHeaders.CONTENT_LENGTH)) {
            int contentLength = Integer.parseInt(requestHeaders.getHeader(HTTPHeaders.CONTENT_LENGTH));

            body = connection.read(contentLength);
        }

        this.logger.info("HAS LINE: " + connection.hasLine());

        String[] versionTokens = tokens[2].split("/");

        if (versionTokens.length != 2) {
            throw new HTTPFormatException(tokens[2] + " is not a parsable HTTP version!");
        }

        String httpVersion = versionTokens[1];
        String path = tokens[1]; // TODO: Apply basic URL regex checks
        Map<String, String> queryParameters = new HashMap<>();

        if (path.contains("?")) {
            int endPath = path.lastIndexOf("?");
            String queryParams = path.substring(endPath + 1);
            String[] serializedEntries = queryParams.split("&");

            Streams.to(serializedEntries).forEach(e -> {
                String[] t = e.split("=");

                if (t.length != 2) {
                    throw new HTTPFormatException("Malformed query parameter!");
                }

                queryParameters.put(t[0], t[1]);
            });

            path = path.substring(0, path.lastIndexOf("?"));
        }

        if (!path.endsWith("/")) {
            path = path + "/";
        }

        Map<String, String> pathParams = new HashMap<>();
        if (this.hasHandler(method, path)) {
            this.handlerMap.get(method).getTokenized(path).getSecond().forEach(t ->
                    pathParams.put(t.getKey(), t.getReplacement()));
        }

        HTTPRequest request;
        if (body.length() > 0) {
            request = new HTTPRequest(method, path, httpVersion, queryParameters, pathParams, requestHeaders,
                    body.trim());
        } else {
            request = new HTTPRequest(method, path, httpVersion, queryParameters, pathParams, requestHeaders);
        }
        Optional<IProtocol> protocolOptional = this.protocolService.getProtocol(request.getHTTPVersion());

        logger.debug("Attempting handoff to protocol...");
        if (protocolOptional.isPresent()) {
            protocolOptional.get().handleConnection(request, connection);
        } else {
            throw new HTTPFormatException(tokens[2] + " is not a supported HTTP version!");
        }
    }
}
