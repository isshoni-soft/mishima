package tv.isshoni.mishima.annotation.processor.http.method;

import tv.isshoni.mishima.annotation.http.method.POST;
import tv.isshoni.mishima.protocol.http.MIMEType;
import tv.isshoni.mishima.protocol.http.handler.HTTPService;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.api.meta.IAnnotatedMethod;

public class POSTProcessor extends SimpleHTTPMethodProcessor<POST> {

    public POSTProcessor(@Inject HTTPService service, @Inject IWinryContext context) {
        super(service, context, POST.class);
    }

    @Override
    protected RuntimeException validate(IAnnotatedMethod method, Object target, POST annotation) {
        if (method.getReturnType().equals(void.class)) {
            return new IllegalStateException("Cannot make void return type HTTP GET method!");
        }

        return null;
    }

    @Override
    public String getPath(POST annotation) {
        return annotation.value();
    }

    @Override
    public MIMEType getMIMEType(POST annotation) {
        return annotation.resultType();
    }
}
