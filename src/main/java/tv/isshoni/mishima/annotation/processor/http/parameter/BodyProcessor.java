package tv.isshoni.mishima.annotation.processor.http.parameter;

import tv.isshoni.mishima.annotation.http.parameter.Body;
import tv.isshoni.mishima.exception.parameter.MissingBodyException;
import tv.isshoni.mishima.exception.parameter.NeedsContentTypeException;
import tv.isshoni.mishima.http.HTTPHeaders;
import tv.isshoni.mishima.http.HTTPRequest;
import tv.isshoni.mishima.http.IHTTPDeserializer;
import tv.isshoni.mishima.http.handler.HTTPService;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.processor.IWinryAdvancedAnnotationProcessor;
import tv.isshoni.winry.api.context.IWinryContext;

import java.lang.reflect.Parameter;
import java.util.Map;

public class BodyProcessor implements IWinryAdvancedAnnotationProcessor<Body, Object> {

    private final IWinryContext context;

    private final HTTPService service;

    public BodyProcessor(@Inject IWinryContext context, @Inject HTTPService service) {
        this.context = context;
        this.service = service;
    }

    @Override
    public Object supply(Body annotation, Object previous, Parameter parameter, Map<String, Object> runtimeContext) {
        if (!runtimeContext.containsKey(HTTPRequest.BODY_PARAMETER_KEY)) {
            throw new MissingBodyException();
        }

        HTTPRequest request = (HTTPRequest) runtimeContext.get("request");
        String bodyData = (String) runtimeContext.get(HTTPRequest.BODY_PARAMETER_KEY);
        IHTTPDeserializer<Object> deserializer = (IHTTPDeserializer<Object>) this.service.getDeserializer(parameter.getType());

        if (!request.getHeaders().getHeader(HTTPHeaders.CONTENT_TYPE)
                .equals(annotation.value().getSerialized())) {
            throw new NeedsContentTypeException(annotation.value());
        }

        if (deserializer != null) {
            return deserializer.deserialize(bodyData);
        }

        return HTTPService.GSON.fromJson(bodyData, parameter.getType());
    }

    @Override
    public IWinryContext getContext() {
        return this.context;
    }
}