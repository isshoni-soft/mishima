package tv.isshoni.mishima.annotation.processor.http.parameter;

import tv.isshoni.araragi.stream.Streams;
import tv.isshoni.mishima.annotation.http.parameter.QueryParameter;
import tv.isshoni.mishima.exception.parameter.MissingRequiredParameterException;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.processor.IWinryAdvancedAnnotationProcessor;
import tv.isshoni.winry.api.context.IWinryContext;

import java.lang.reflect.Parameter;
import java.util.Map;

public class QueryParameterProcessor implements IWinryAdvancedAnnotationProcessor<QueryParameter, String> {

    public static final String QUERY_PARAMETER_DATA_PREFIX = "QUERY_PARAMETER_";

    private final IWinryContext context;

    public QueryParameterProcessor(@Inject IWinryContext context) {
        this.context = context;
    }

    @Override
    public String supply(QueryParameter annotation, String previous, Parameter parameter, Map<String, Object> runtimeContext) {
        Map<String, String> queryParams = Streams.to(runtimeContext)
                .filter((k, v) -> k.startsWith(QUERY_PARAMETER_DATA_PREFIX))
                .mapFirst(k -> k.substring(QUERY_PARAMETER_DATA_PREFIX.length()))
                .mapSecond(o -> (String) o)
                .toMap();

        if (!queryParams.containsKey(annotation.value()) && !annotation.optional()) {
            throw new MissingRequiredParameterException(annotation.value());
        }

        return queryParams.get(annotation.value());
    }

    @Override
    public IWinryContext getContext() {
        return this.context;
    }
}
