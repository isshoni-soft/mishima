package tv.isshoni.mishima.annotation.processor.http;

import tv.isshoni.araragi.data.Constant;
import tv.isshoni.araragi.exception.Exceptions;
import tv.isshoni.mishima.annotation.http.Serialization;
import tv.isshoni.mishima.protocol.http.IHTTPDeserializer;
import tv.isshoni.mishima.protocol.http.IHTTPSerializer;
import tv.isshoni.mishima.protocol.http.handler.HTTPService;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.parameter.Context;
import tv.isshoni.winry.api.annotation.processor.IWinryAnnotationProcessor;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.api.meta.IAnnotatedClass;

public class SerializationProcessor implements IWinryAnnotationProcessor<Serialization> {

    private final HTTPService service;

    private final Constant<IWinryContext> context;

    public SerializationProcessor(@Context IWinryContext context, @Inject HTTPService service) {
        this.service = service;
        this.context = new Constant<>(context);
    }

    @Override
    public void executeClass(IAnnotatedClass clazz, Object target, Serialization annotation) {
        boolean inserted = false;

        try {
            if (IHTTPDeserializer.class.isAssignableFrom(clazz.getElement())) {
                inserted = true;
                this.service.registerHTTPDeserializer((Class<Object>) annotation.value(), (IHTTPDeserializer<Object>) clazz.newInstance());
            } else if (IHTTPSerializer.class.isAssignableFrom(clazz.getElement())) {
                inserted = true;
                this.service.registerHTTPSerializer((Class<Object>) annotation.value(), (IHTTPSerializer<Object>) clazz.newInstance());
            }
        } catch (Throwable e) {
            throw Exceptions.rethrow(e);
        }

        if (!inserted) {
            throw new IllegalStateException("@Serialization on non-HTTPSerializer-type class");
        }
    }

    @Override
    public Constant<IWinryContext> getContext() {
        return this.context;
    }
}
