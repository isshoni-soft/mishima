package tv.isshoni.mishima.annotation.processor.http;

import tv.isshoni.mishima.annotation.http.GET;
import tv.isshoni.mishima.http.HTTPMethod;
import tv.isshoni.mishima.http.HTTPService;
import tv.isshoni.winry.api.annotation.processor.IWinryAnnotationProcessor;
import tv.isshoni.winry.internal.model.meta.IAnnotatedMethod;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

public abstract class SimpleHTTPProcessor<A extends Annotation> implements IWinryAnnotationProcessor<A> {

    private static final List<Class<? extends Annotation>> INCOMPATIBLE = new LinkedList<>() {{
        add(GET.class);
    }};

    private final List<Class<? extends Annotation>> incompatible;

    private final HTTPService service;

    public SimpleHTTPProcessor(HTTPService service, Class<A> clazz) {
        this.service = service;
        this.incompatible = INCOMPATIBLE.stream().filter(c -> c.equals(clazz)).toList();
    }

    public abstract HTTPMethod getHTTPMethod();

    public abstract String getPath(A annotation);

    @Override
    public void executeMethod(IAnnotatedMethod method, Object target, A annotation) {
        this.service.registerHTTPHandler(getHTTPMethod(), target, method, getPath(annotation));
    }

    @Override
    public List<Class<? extends Annotation>> getIncompatibleWith(A annotation) {
        return this.incompatible;
    }
}
