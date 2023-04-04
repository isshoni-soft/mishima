package tv.isshoni.mishima.protocol.http.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import tv.isshoni.araragi.data.Pair;
import tv.isshoni.araragi.data.collection.map.BucketMap;
import tv.isshoni.araragi.data.collection.map.SubMap;
import tv.isshoni.araragi.data.collection.map.TypeMap;
import tv.isshoni.araragi.data.collection.map.token.TokenMap;
import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.araragi.string.format.StringFormatter;
import tv.isshoni.mishima.event.config.MishimaHTTPConfigEvent;
import tv.isshoni.mishima.protocol.http.HTTPMethod;
import tv.isshoni.mishima.protocol.http.IHTTPDeserializer;
import tv.isshoni.mishima.protocol.http.IHTTPSerializer;
import tv.isshoni.mishima.protocol.http.MIMEType;
import tv.isshoni.mishima.service.ProtocolService;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.Injected;
import tv.isshoni.winry.api.annotation.parameter.Context;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.api.exception.EventExecutionException;
import tv.isshoni.winry.api.meta.IAnnotatedMethod;
import tv.isshoni.winry.api.service.ObjectFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Injected
public class HTTPService {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final AraragiLogger logger;

    @Inject private ProtocolService protocolService;

    @Inject private ObjectFactory objectFactory;

    private final IWinryContext context;

    private final SubMap<HTTPMethod, String, HTTPHandler, TokenMap<HTTPHandler>> handlerMap;

    private final BucketMap<String, Pair<HTTPMethod, HTTPHandler>> handlersByPath;

    private final TypeMap<Class<?>, IHTTPSerializer<?>> serializers;

    private final TypeMap<Class<?>, IHTTPDeserializer<?>> deserializers;

    private final MishimaHTTPConfigEvent httpConfig;

    public static StringFormatter makeNewFormatter() {
        return new StringFormatter("{", "}");
    }

    public HTTPService(@Context IWinryContext context) {
        this.context = context;
        this.logger = this.context.createLogger("HTTPService");
        this.serializers = new TypeMap<>();
        this.deserializers = new TypeMap<>();
        this.handlerMap = new SubMap<>(() -> new TokenMap<>(makeNewFormatter()));
        this.handlersByPath = new BucketMap<>(new TokenMap<>());
        this.httpConfig = new MishimaHTTPConfigEvent();

        try {
            context.getEventBus().fire(this.httpConfig);
        } catch (EventExecutionException e) {
            context.getExceptionManager().toss(e);
        }

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

        if (!this.handlersByPath.containsKey(path)) {
            this.logger.info("Registering options for path: " + path);
            HTTPHandler optionsHandler = new HTTPOptionsHandler(this.context, this, this.objectFactory);
            this.handlerMap.put(HTTPMethod.OPTIONS, Pair.of(path, optionsHandler));
            this.handlersByPath.add(path, Pair.of(HTTPMethod.OPTIONS, optionsHandler));
        }

        HTTPHandler handler = new HTTPHandler(this.context, mimeType, method, object);
        this.handlerMap.put(httpMethod, Pair.of(path, handler));
        this.handlersByPath.add(path, Pair.of(httpMethod, handler));
        this.logger.info("Registered HTTP Handler: " + httpMethod + " " + path + " -- " + method.getDisplay());
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

    public Object execute(HTTPMethod method, String path, Map<String, Object> data) {
        return getHandler(method, path).execute(data);
    }

    public TokenMap<HTTPHandler> getTokenMapForMethod(HTTPMethod method) {
        return this.handlerMap.getOrDefault(method);
    }

    public List<Pair<HTTPMethod, HTTPHandler>> getHandlersForPath(String path) {
        return new LinkedList<>(this.handlersByPath.getOrNew(path));
    }
}
