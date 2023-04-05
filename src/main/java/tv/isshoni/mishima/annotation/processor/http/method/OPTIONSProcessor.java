package tv.isshoni.mishima.annotation.processor.http.method;

import tv.isshoni.mishima.annotation.http.method.OPTIONS;
import tv.isshoni.mishima.protocol.http.MIMEType;
import tv.isshoni.mishima.protocol.http.handler.HTTPService;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.context.IWinryContext;

public class OPTIONSProcessor extends SimpleHTTPMethodProcessor<OPTIONS> {

    public OPTIONSProcessor(@Inject HTTPService service, @Inject IWinryContext context) {
        super(service, context, OPTIONS.class);
    }

    @Override
    public String getPath(OPTIONS annotation) {
        return annotation.value();
    }

    @Override
    public MIMEType getMIMEType(OPTIONS annotation) {
        return null;
    }
}