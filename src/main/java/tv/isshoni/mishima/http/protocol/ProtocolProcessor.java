package tv.isshoni.mishima.http.protocol;

import tv.isshoni.mishima.annotation.http.Protocol;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.processor.IWinryAnnotationProcessor;
import tv.isshoni.winry.internal.model.meta.IAnnotatedClass;

public class ProtocolProcessor implements IWinryAnnotationProcessor<Protocol> {

    private final ProtocolService service;

    public ProtocolProcessor(@Inject ProtocolService service) {
        this.service = service;
    }

    @Override
    public void executeClass(IAnnotatedClass clazz, Object target, Protocol annotation) {
        if (!IProtocol.class.isAssignableFrom(clazz.getElement())) {
            throw new IllegalStateException("Cannot register non-IProtocol protocol");
        }

        this.service.registerProtocol(annotation.value(), (IProtocol) clazz.getInstance());
    }
}
