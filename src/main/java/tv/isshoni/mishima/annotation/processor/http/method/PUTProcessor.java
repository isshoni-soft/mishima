package tv.isshoni.mishima.annotation.processor.http.method;

import tv.isshoni.mishima.annotation.http.method.PUT;
import tv.isshoni.mishima.protocol.http.HTTP;
import tv.isshoni.mishima.protocol.http.MIMEType;
import tv.isshoni.mishima.protocol.http.OverseerService;
import tv.isshoni.mishima.protocol.http.handler.HTTPService;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.context.IWinryContext;

public class PUTProcessor extends SimpleHTTPMethodProcessor<PUT> {

    public PUTProcessor(@Inject HTTPService service, @Inject HTTP http, @Inject OverseerService overseerService,
                        @Inject IWinryContext context) {
        super(service, http, overseerService, context, PUT.class);
    }

    @Override
    public String getPath(PUT annotation) {
        return annotation.value();
    }

    @Override
    public MIMEType getMIMEType(PUT annotation) {
        return annotation.resultType();
    }
}
