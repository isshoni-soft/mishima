package tv.isshoni.mishima.annotation.processor.http;

import tv.isshoni.araragi.exception.Exceptions;
import tv.isshoni.mishima.annotation.http.HTTPSerializer;
import tv.isshoni.mishima.http.HTTPService;
import tv.isshoni.mishima.http.IHTTPSerializer;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.processor.IWinryAnnotationProcessor;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.api.meta.IAnnotatedClass;

public class SerializerProcessor implements IWinryAnnotationProcessor<HTTPSerializer> {

    private final HTTPService service;

    private final IWinryContext context;

    public SerializerProcessor(@Inject HTTPService service, @Inject IWinryContext context) {
        this.service = service;
        this.context = context;
    }

    @Override
    public void executeClass(IAnnotatedClass clazz, Object target, HTTPSerializer annotation) {
        if (!IHTTPSerializer.class.isAssignableFrom(clazz.getElement())) {
            throw new IllegalStateException("@HTTPSerializer on non-IHTTPSerializer class");
        }

        try {
            this.service.registerHTTPSerializer((Class<Object>) annotation.value(), (IHTTPSerializer<Object>) clazz.newInstance());
        } catch (Throwable e) {
            throw Exceptions.rethrow(e);
        }
    }

    @Override
    public IWinryContext getContext() {
        return this.context;
    }
}
