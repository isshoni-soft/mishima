package tv.isshoni.mishima.annotation.processor.http;

import tv.isshoni.mishima.annotation.http.HTTPSerializer;
import tv.isshoni.mishima.http.HTTPService;
import tv.isshoni.mishima.http.IHTTPSerializer;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.processor.IWinryAnnotationProcessor;
import tv.isshoni.winry.internal.model.meta.IAnnotatedClass;

public class SerializerProcessor implements IWinryAnnotationProcessor<HTTPSerializer> {

    private final HTTPService service;

    public SerializerProcessor(@Inject HTTPService service) {
        this.service = service;
    }

    @Override
    public void executeClass(IAnnotatedClass clazz, Object target, HTTPSerializer annotation) {
        if (!IHTTPSerializer.class.isAssignableFrom(clazz.getElement())) {
            throw new IllegalStateException("@HTTPSerializer on non-IHTTPSerializer class");
        }

        this.service.registerHTTPSerializer(annotation.value(), (IHTTPSerializer<?>) clazz.getInstance());
    }
}
