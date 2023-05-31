package tv.isshoni.mishima.annotation.processor.http;

import tv.isshoni.araragi.data.Constant;
import tv.isshoni.mishima.annotation.http.Overseer;
import tv.isshoni.mishima.protocol.http.OverseerService;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.processor.IWinryAnnotationProcessor;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.api.meta.IAnnotatedClass;
import tv.isshoni.winry.internal.model.meta.bytebuddy.IWrapperGenerator;

public class OverseerProcessor implements IWinryAnnotationProcessor<Overseer> {

    private final Constant<IWinryContext> context;

    private final OverseerService service;

    public OverseerProcessor(@Inject IWinryContext context, @Inject OverseerService service) {
        this.context = new Constant<>(context);
        this.service = service;
    }

    @Override
    public void transformClass(IAnnotatedClass classMeta, IWrapperGenerator generator, Overseer annotation) {
        this.service.register(classMeta, annotation);
    }

    @Override
    public Constant<IWinryContext> getContext() {
        return this.context;
    }
}
