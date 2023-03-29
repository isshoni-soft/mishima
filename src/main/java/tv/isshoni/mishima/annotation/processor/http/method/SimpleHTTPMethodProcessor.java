package tv.isshoni.mishima.annotation.processor.http.method;

import tv.isshoni.araragi.stream.Streams;
import tv.isshoni.araragi.string.format.StringFormatter;
import tv.isshoni.araragi.string.format.StringToken;
import tv.isshoni.mishima.Mishima;
import tv.isshoni.mishima.annotation.http.method.CONNECT;
import tv.isshoni.mishima.annotation.http.method.DELETE;
import tv.isshoni.mishima.annotation.http.method.GET;
import tv.isshoni.mishima.annotation.http.method.HEAD;
import tv.isshoni.mishima.annotation.http.method.POST;
import tv.isshoni.mishima.annotation.http.method.PUT;
import tv.isshoni.mishima.annotation.http.method.TRACE;
import tv.isshoni.mishima.http.HTTPMethod;
import tv.isshoni.mishima.http.handler.HTTPService;
import tv.isshoni.mishima.http.MIMEType;
import tv.isshoni.winry.api.annotation.processor.IWinryAnnotationProcessor;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.api.meta.IAnnotatedMethod;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class SimpleHTTPMethodProcessor<A extends Annotation> implements IWinryAnnotationProcessor<A> {

    private static final Map<Class<? extends Annotation>, HTTPMethod> ANNOTATION_TO_METHOD = new HashMap<>() {{
        put(GET.class, HTTPMethod.GET);
        put(CONNECT.class, HTTPMethod.CONNECT);
        put(DELETE.class, HTTPMethod.DELETE);
        put(HEAD.class, HTTPMethod.HEAD);
        put(POST.class, HTTPMethod.POST);
        put(PUT.class, HTTPMethod.PUT);
        put(TRACE.class, HTTPMethod.TRACE);
    }};

    protected final List<Class<? extends Annotation>> incompatible;

    protected final IWinryContext context;

    protected final HTTPService service;

    protected final Class<A> clazz;

    public SimpleHTTPMethodProcessor(HTTPService service, IWinryContext context, Class<A> clazz) {
        this.service = service;
        this.context = context;
        this.clazz = clazz;
        this.incompatible = ANNOTATION_TO_METHOD.keySet().stream().filter(c -> !c.equals(clazz)).toList();
    }

    protected RuntimeException validate(IAnnotatedMethod method, Object target, A annotation) {
        return null;
    }

    public HTTPMethod getHTTPMethod() {
        return ANNOTATION_TO_METHOD.get(this.clazz);
    }

    public abstract String getPath(A annotation);

    public abstract MIMEType getMIMEType(A annotation);

    @Override
    public void executeMethod(IAnnotatedMethod method, Object target, A annotation) {
        String path = getPath(annotation);
        StringFormatter formatter = HTTPService.makeNewFormatter();
        Streams.to(HTTPService.makeNewFormatter().tokenize(path))
                .mapToPair(StringToken::getKey, t -> (Supplier<String>) () -> "placeholder")
                .forEach(p -> formatter.registerSupplier(p.getFirst(), p.getSecond()));

        String formatted = formatter.format(path);

        if (!Mishima.PATH_LEGAL.matcher(formatted).matches()) {
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

    @Override
    public IWinryContext getContext() {
        return this.context;
    }
}
