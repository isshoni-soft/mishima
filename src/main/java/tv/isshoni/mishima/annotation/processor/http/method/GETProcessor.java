package tv.isshoni.mishima.annotation.processor.http.method;

import tv.isshoni.mishima.annotation.http.method.GET;
import tv.isshoni.mishima.protocol.http.MIMEType;
import tv.isshoni.mishima.protocol.http.handler.HTTPService;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.context.IWinryContext;

public class GETProcessor extends SimpleHTTPMethodProcessor<GET> {

    public GETProcessor(@Inject HTTPService service, @Inject IWinryContext context) {
        super(service, context, GET.class);
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
