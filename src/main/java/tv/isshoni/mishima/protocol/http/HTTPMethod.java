package tv.isshoni.mishima.protocol.http;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public enum HTTPMethod {
    OPTIONS(false, false, tv.isshoni.mishima.annotation.http.method.OPTIONS.class),
    GET(true, true, tv.isshoni.mishima.annotation.http.method.GET.class),
    HEAD(false, false, tv.isshoni.mishima.annotation.http.method.HEAD.class),
    POST(true, true, tv.isshoni.mishima.annotation.http.method.POST.class),
    PUT(true, true, tv.isshoni.mishima.annotation.http.method.PUT.class),
    DELETE(false, true, tv.isshoni.mishima.annotation.http.method.DELETE.class),
//    TRACE(false, false),
//    CONNECT(false, false)
    ;

    private static final Map<Class<? extends Annotation>, HTTPMethod> METHODS_BY_ANNOTATION = new HashMap<>();

    static {
        for (HTTPMethod method : values()) {
            METHODS_BY_ANNOTATION.put(method.annotation, method);
        }
    }

    public static HTTPMethod getFromAnnotation(Class<? extends Annotation> annotation) {
        return METHODS_BY_ANNOTATION.get(annotation);
    }

    public static Collection<Class<? extends Annotation>> getAnnotations() {
        return METHODS_BY_ANNOTATION.keySet();
    }

    private final boolean incomingBody;
    private final boolean outgoingBody;

    private final Class<? extends Annotation> annotation;

    HTTPMethod(boolean incomingBody, boolean outgoingBody, Class<? extends Annotation> annotation) {
        this.incomingBody = incomingBody;
        this.outgoingBody = outgoingBody;
        this.annotation = annotation;
    }

    public boolean hasIncomingBody() {
        return this.incomingBody;
    }

    public boolean hasOutgoingBody() {
        return this.outgoingBody;
    }

    public Class<? extends Annotation> getAnnotation() {
        return this.annotation;
    }
}
