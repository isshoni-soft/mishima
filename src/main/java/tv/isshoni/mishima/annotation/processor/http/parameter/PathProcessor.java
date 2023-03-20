package tv.isshoni.mishima.annotation.processor.http.parameter;

import tv.isshoni.araragi.stream.Streams;
import tv.isshoni.mishima.annotation.http.parameter.Path;
import tv.isshoni.mishima.exception.parameter.MissingRequiredParameterException;
import tv.isshoni.mishima.http.HTTPRequest;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.processor.IWinryAdvancedAnnotationProcessor;
import tv.isshoni.winry.api.context.IWinryContext;

import java.lang.reflect.Parameter;
import java.util.Map;

public class PathProcessor implements IWinryAdvancedAnnotationProcessor<Path, String> {

    private final IWinryContext context;

    public PathProcessor(@Inject IWinryContext context) {
        this.context = context;
    }

    @Override
    public String supply(Path annotation, String previous, Parameter parameter, Map<String, Object> runtimeContext) {
        Map<String, String> queryParams = Streams.to(runtimeContext)
                .filter((k, v) -> k.startsWith(HTTPRequest.PATH_PARAMETER_DATA_PREFIX))
                .mapFirst(k -> k.substring(HTTPRequest.PATH_PARAMETER_DATA_PREFIX.length()))
                .mapSecond(o -> (String) o)
                .toMap();

        if (!queryParams.containsKey(annotation.value())) {
            throw new MissingRequiredParameterException(annotation.value(), "path");
        }

        return queryParams.get(annotation.value());
    }

    @Override
    public IWinryContext getContext() {
        return this.context;
    }
}
