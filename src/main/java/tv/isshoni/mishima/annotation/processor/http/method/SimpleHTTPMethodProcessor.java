package tv.isshoni.mishima.annotation.processor.http.method;

import tv.isshoni.araragi.stream.Streams;
import tv.isshoni.araragi.string.format.StringFormatter;
import tv.isshoni.araragi.string.format.StringToken;
import tv.isshoni.mishima.Mishima;
import tv.isshoni.mishima.annotation.http.Overseer;
import tv.isshoni.mishima.protocol.http.HTTPMethod;
import tv.isshoni.mishima.protocol.http.MIMEType;
import tv.isshoni.mishima.protocol.http.OverseerService;
import tv.isshoni.mishima.protocol.http.handler.HTTPService;
import tv.isshoni.winry.api.annotation.processor.IWinryAnnotationProcessor;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.api.meta.IAnnotatedMethod;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class SimpleHTTPMethodProcessor<A extends Annotation> implements IWinryAnnotationProcessor<A> {

    protected final List<Class<? extends Annotation>> incompatible;

    protected final IWinryContext context;

    protected final HTTPService httpService;

    protected final OverseerService overseerService;

    protected final Class<A> clazz;

    public SimpleHTTPMethodProcessor(HTTPService httpService, OverseerService overseerService, IWinryContext context, Class<A> clazz) {
        this.httpService = httpService;
        this.overseerService = overseerService;
        this.context = context;
        this.clazz = clazz;
        this.incompatible = Streams.to(HTTPMethod.getAnnotations())
                .filter(c -> !c.equals(clazz))
                .toList();
    }

    protected RuntimeException validate(IAnnotatedMethod method, Object target, A annotation) {
        HTTPMethod httpMethod = getHTTPMethod();

        if (method.getReturnType().equals(void.class) && httpMethod.hasOutgoingBody()) {
            return new IllegalStateException("Cannot make void return type HTTP " + httpMethod.name() + " method!");
        }

        return null;
    }

    public HTTPMethod getHTTPMethod() {
        return HTTPMethod.getFromAnnotation(this.clazz);
    }

    public abstract String getPath(A annotation);

    public abstract MIMEType getMIMEType(A annotation);

    @Override
    public void executeMethod(IAnnotatedMethod method, Object target, A annotation) {
        String prefix = Optional.ofNullable(this.overseerService.getPath(method.getDeclaringClass()))
                .map(Overseer::value).orElse("");
        String path = getPath(annotation);

        if (!prefix.endsWith("/")) {
            prefix += "/";
        }

        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        path = prefix + path;
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

        this.httpService.registerHTTPHandler(getHTTPMethod(), getMIMEType(annotation), target, method, path);
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
