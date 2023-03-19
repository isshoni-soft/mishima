package tv.isshoni.mishima.annotation.processor.http.method;

import tv.isshoni.mishima.annotation.http.method.GET;
import tv.isshoni.mishima.http.HTTPMethod;
import tv.isshoni.mishima.http.MIMEType;
import tv.isshoni.mishima.http.handler.HTTPService;
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
