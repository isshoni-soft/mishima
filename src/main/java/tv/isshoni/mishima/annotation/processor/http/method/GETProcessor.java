package tv.isshoni.mishima.annotation.processor.http.method;

import tv.isshoni.mishima.annotation.http.method.GET;
import tv.isshoni.mishima.http.HTTPMethod;
import tv.isshoni.mishima.http.HTTPResponse;
import tv.isshoni.mishima.http.HTTPService;
import tv.isshoni.mishima.http.MIMEType;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.api.meta.IAnnotatedMethod;

public class GETProcessor extends SimpleHTTPMethodProcessor<GET> {

    public GETProcessor(@Inject HTTPService service, @Inject IWinryContext context) {
        super(service, context, GET.class);
    }

    @Override
    protected RuntimeException validate(IAnnotatedMethod method, Object target, GET annotation) {
        if (method.getReturnType().equals(void.class)) {
            return new IllegalStateException("Cannot make void return type HTTP GET method!");
        }

        Class<?> returnType = method.getReturnType();

        if (HTTPResponse.class.isAssignableFrom(returnType)) {
            return null;
        }

        if (!this.service.hasSerializer(returnType)) {
            return new IllegalStateException("No HTTPSerializer found for type: " + returnType);
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

    @Override
    public MIMEType getMIMEType(GET annotation) {
        return annotation.mimeType();
    }
}
