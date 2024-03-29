package tv.isshoni.mishima.annotation.processor.http.method;

import tv.isshoni.mishima.annotation.http.method.HEAD;
import tv.isshoni.mishima.protocol.http.HTTP;
import tv.isshoni.mishima.protocol.http.HTTPMethod;
import tv.isshoni.mishima.protocol.http.MIMEType;
import tv.isshoni.mishima.protocol.http.PathService;
import tv.isshoni.mishima.protocol.http.handler.HTTPService;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.parameter.Context;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.api.meta.IAnnotatedMethod;

public class HEADProcessor extends SimpleHTTPMethodProcessor<HEAD> {

    public HEADProcessor(@Context IWinryContext context, @Inject HTTPService service, @Inject HTTP http,
                         @Inject PathService pathService) {
        super(service, http, pathService, context, HEAD.class);
    }

    @Override
    protected RuntimeException validate(IAnnotatedMethod method, Object target, HEAD annotation) {
        if (!this.httpService.hasHandler(HTTPMethod.GET, getPath(annotation))) {
            return new IllegalStateException("Cannot register HEAD method for path that doesn't have GET: "
                    + getPath(annotation));
        }

        return super.validate(method, target, annotation);
    }

    @Override
    public String getPath(HEAD annotation) {
        return annotation.value();
    }

    @Override
    public MIMEType getMIMEType(HEAD annotation) {
        return null;
    }
}
