package tv.isshoni.mishima.annotation.processor.http.method;

import tv.isshoni.mishima.annotation.http.method.DELETE;
import tv.isshoni.mishima.protocol.http.MIMEType;
import tv.isshoni.mishima.protocol.http.OverseerService;
import tv.isshoni.mishima.protocol.http.handler.HTTPService;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.context.IWinryContext;

public class DELETEProcessor extends SimpleHTTPMethodProcessor<DELETE> {

    public DELETEProcessor(@Inject HTTPService service, @Inject OverseerService overseerService, @Inject IWinryContext context) {
        super(service, overseerService, context, DELETE.class);
    }

    @Override
    public String getPath(DELETE annotation) {
        return annotation.value();
    }

    @Override
    public MIMEType getMIMEType(DELETE annotation) {
        return null;
    }
}
