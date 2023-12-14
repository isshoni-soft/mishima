package tv.isshoni.mishima.annotation.processor.http.parameter;

import tv.isshoni.araragi.data.Constant;
import tv.isshoni.mishima.annotation.http.parameter.BodyParam;
import tv.isshoni.mishima.exception.parameter.MissingBodyException;
import tv.isshoni.mishima.exception.parameter.NeedsContentTypeException;
import tv.isshoni.mishima.protocol.http.HTTPHeaders;
import tv.isshoni.mishima.protocol.http.HTTPRequest;
import tv.isshoni.mishima.protocol.http.IHTTPDeserializer;
import tv.isshoni.mishima.protocol.http.handler.HTTPService;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.parameter.Context;
import tv.isshoni.winry.api.annotation.processor.IWinryAdvancedAnnotationProcessor;
import tv.isshoni.winry.api.context.IWinryContext;

import java.lang.reflect.Parameter;
import java.util.Map;

public class BodyProcessor implements IWinryAdvancedAnnotationProcessor<BodyParam, Object> {

    private final Constant<IWinryContext> context;

    private final HTTPService service;

    public BodyProcessor(@Context IWinryContext context, @Inject HTTPService service) {
        this.context = new Constant<>(context);
        this.service = service;
    }

    @Override
    public Object supply(BodyParam annotation, Object previous, Parameter parameter, Map<String, Object> runtimeContext) {
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
    public Constant<IWinryContext> getContext() {
        return this.context;
    }
}
