package tv.isshoni.mishima.http.protocol;

import tv.isshoni.mishima.annotation.http.Protocol;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.processor.IWinryAnnotationProcessor;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.api.meta.IAnnotatedClass;
import tv.isshoni.winry.api.meta.ISingletonAnnotatedClass;

public class ProtocolProcessor implements IWinryAnnotationProcessor<Protocol> {

    private final ProtocolService service;

    private final IWinryContext context;

    public ProtocolProcessor(@Inject ProtocolService service, @Inject IWinryContext context) {
        this.service = service;
        this.context = context;
    }

    @Override
    public void executeClass(IAnnotatedClass clazz, Object target, Protocol annotation) {
        if (!IProtocol.class.isAssignableFrom(clazz.getElement())) {
            throw new IllegalStateException("Cannot register non-IProtocol protocol");
        }

        this.service.registerProtocol(annotation.value(), (IProtocol) ((ISingletonAnnotatedClass) clazz).getInstance());
    }

    @Override
    public IWinryContext getContext() {
        return this.context;
    }
}
