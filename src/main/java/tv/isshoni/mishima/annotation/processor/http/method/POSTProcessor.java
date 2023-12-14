package tv.isshoni.mishima.annotation.processor.http.method;

import tv.isshoni.mishima.annotation.http.method.POST;
import tv.isshoni.mishima.protocol.http.HTTP;
import tv.isshoni.mishima.protocol.http.MIMEType;
import tv.isshoni.mishima.protocol.http.PathService;
import tv.isshoni.mishima.protocol.http.handler.HTTPService;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.parameter.Context;
import tv.isshoni.winry.api.context.IWinryContext;

public class POSTProcessor extends SimpleHTTPMethodProcessor<POST> {

    public POSTProcessor(@Context IWinryContext context, @Inject HTTPService service, @Inject HTTP http,
                         @Inject PathService pathService) {
        super(service, http, pathService, context, POST.class);
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
