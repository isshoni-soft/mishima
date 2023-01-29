package tv.isshoni.mishima.annotation.processor.http.method;

import tv.isshoni.mishima.Mishima;
import tv.isshoni.mishima.annotation.http.method.GET;
import tv.isshoni.mishima.http.HTTPMethod;
import tv.isshoni.mishima.http.HTTPService;
import tv.isshoni.mishima.http.MIMEType;
import tv.isshoni.winry.api.annotation.processor.IWinryAnnotationProcessor;
import tv.isshoni.winry.internal.model.meta.IAnnotatedMethod;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

public abstract class SimpleHTTPMethodProcessor<A extends Annotation> implements IWinryAnnotationProcessor<A> {

    private static final List<Class<? extends Annotation>> INCOMPATIBLE = new LinkedList<>() {{
        add(GET.class);
    }};

    protected final List<Class<? extends Annotation>> incompatible;

    protected final HTTPService service;

    public SimpleHTTPMethodProcessor(HTTPService service, Class<A> clazz) {
        this.service = service;
        this.incompatible = INCOMPATIBLE.stream().filter(c -> !c.equals(clazz)).toList();
    }

    protected RuntimeException validate(IAnnotatedMethod method, Object target, A annotation) {
        return null;
    }

    public abstract HTTPMethod getHTTPMethod();

    public abstract String getPath(A annotation);

    public abstract MIMEType getMIMEType(A annotation);

    @Override
    public void executeMethod(IAnnotatedMethod method, Object target, A annotation) {
        String path = getPath(annotation);

        if (!Mishima.PATH_LEGAL.matcher(path).matches()) {
            throw new IllegalStateException(path + " is not a valid http path");
        }

        RuntimeException except = validate(method, target, annotation);
        if (except != null) {
            throw except;
        }

        this.service.registerHTTPHandler(getHTTPMethod(), getMIMEType(annotation), target, method, path);
    }

    @Override
    public List<Class<? extends Annotation>> getIncompatibleWith(A annotation) {
        return this.incompatible;
    }
}
