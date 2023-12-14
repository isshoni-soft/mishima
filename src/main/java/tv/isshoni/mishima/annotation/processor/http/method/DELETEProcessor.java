package tv.isshoni.mishima.annotation.processor.http.method;

import tv.isshoni.mishima.annotation.http.method.DELETE;
import tv.isshoni.mishima.protocol.http.HTTP;
import tv.isshoni.mishima.protocol.http.MIMEType;
import tv.isshoni.mishima.protocol.http.PathService;
import tv.isshoni.mishima.protocol.http.handler.HTTPService;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.parameter.Context;
import tv.isshoni.winry.api.context.IWinryContext;

public class DELETEProcessor extends SimpleHTTPMethodProcessor<DELETE> {

    public DELETEProcessor(@Context IWinryContext context, @Inject HTTPService service, @Inject HTTP http,
                           @Inject PathService pathService) {
        super(service, http, pathService, context, DELETE.class);
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
