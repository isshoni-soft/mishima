package tv.isshoni.mishima.annotation.processor.http;

import tv.isshoni.araragi.data.Constant;
import tv.isshoni.mishima.annotation.http.Path;
import tv.isshoni.mishima.protocol.http.PathService;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.parameter.Context;
import tv.isshoni.winry.api.annotation.processor.IWinryAnnotationProcessor;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.api.meta.IAnnotatedClass;
import tv.isshoni.winry.internal.model.meta.bytebuddy.IWrapperGenerator;

public class PathProcessor implements IWinryAnnotationProcessor<Path> {

    private final Constant<IWinryContext> context;

    private final PathService service;

    public PathProcessor(@Context IWinryContext context, @Inject PathService service) {
        this.context = new Constant<>(context);
        this.service = service;
    }

    @Override
    public void transformClass(IAnnotatedClass classMeta, IWrapperGenerator generator, Path annotation) {
        this.service.register(classMeta, annotation);
    }

    @Override
    public Constant<IWinryContext> getContext() {
        return this.context;
    }
}
