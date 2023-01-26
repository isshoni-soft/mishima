package tv.isshoni.mishima.annotation.processor.http.method;

import tv.isshoni.mishima.annotation.http.method.GET;
import tv.isshoni.mishima.http.HTTPMethod;
import tv.isshoni.mishima.http.HTTPService;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.internal.model.meta.IAnnotatedMethod;

public class GETProcessor extends SimpleHTTPMethodProcessor<GET> {

    public GETProcessor(@Inject HTTPService service) {
        super(service, GET.class);
    }

    @Override
    protected RuntimeException validate(IAnnotatedMethod method, Object target, GET annotation) {
        if (method.getReturnType().equals(void.class)) {
            return new IllegalStateException("Cannot make void return type HTTP GET method!");
        }

        if (method.getReturnType().equals(String.class)) {
            return null;
        }

        if (this.service.hasSerializer(method.getReturnType())) {
            return new IllegalStateException("No HTTPSerializer found for type: " + method.getReturnType());
        }

        return null;
    }

    @Override
    public HTTPMethod getHTTPMethod() {
        return HTTPMethod.GET;
    }

    @Override
    public String getPath(GET annotation) {
        return annotation.value();
    }
}
