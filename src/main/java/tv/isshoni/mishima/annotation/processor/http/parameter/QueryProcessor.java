package tv.isshoni.mishima.annotation.processor.http.parameter;

import tv.isshoni.araragi.data.Constant;
import tv.isshoni.araragi.stream.Streams;
import tv.isshoni.mishima.annotation.http.parameter.QueryParam;
import tv.isshoni.mishima.exception.parameter.MissingRequiredParameterException;
import tv.isshoni.mishima.protocol.http.HTTPRequest;
import tv.isshoni.mishima.protocol.http.IHTTPDeserializer;
import tv.isshoni.mishima.protocol.http.handler.HTTPService;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.parameter.Context;
import tv.isshoni.winry.api.annotation.processor.IWinryAdvancedAnnotationProcessor;
import tv.isshoni.winry.api.context.IWinryContext;

import java.lang.reflect.Parameter;
import java.util.Map;

public class QueryProcessor implements IWinryAdvancedAnnotationProcessor<QueryParam, Object> {

    private final Constant<IWinryContext> context;

    private final HTTPService service;

    public QueryProcessor(@Context IWinryContext context, @Inject HTTPService service) {
        this.context = new Constant<>(context);
        this.service = service;
    }

    @Override
    public Object supply(QueryParam annotation, Object previous, Parameter parameter, Map<String, Object> runtimeContext) {
        Map<String, String> queryParams = Streams.to(runtimeContext)
                .filter((k, v) -> k.startsWith(HTTPRequest.QUERY_PARAMETER_DATA_PREFIX))
                .mapFirst(k -> k.substring(HTTPRequest.QUERY_PARAMETER_DATA_PREFIX.length()))
                .mapSecond(o -> (String) o)
                .toMap();

        if (!queryParams.containsKey(annotation.value()) && !annotation.optional()) {
            throw new MissingRequiredParameterException(annotation.value(), "query");
        }

        IHTTPDeserializer<Object> deserializer = (IHTTPDeserializer<Object>) this.service.getDeserializer(parameter.getType());
        String data = queryParams.get(annotation.value());

        if (deserializer != null) {
            return deserializer.deserialize(data);
        }

        return data;
    }

    @Override
    public Constant<IWinryContext> getContext() {
        return this.context;
    }
}
