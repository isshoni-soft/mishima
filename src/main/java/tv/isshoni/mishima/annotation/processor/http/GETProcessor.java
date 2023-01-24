package tv.isshoni.mishima.annotation.processor.http;

import tv.isshoni.mishima.annotation.http.GET;
import tv.isshoni.mishima.http.HTTPMethod;
import tv.isshoni.mishima.http.HTTPService;
import tv.isshoni.winry.api.annotation.Inject;

public class GETProcessor extends SimpleHTTPProcessor<GET> {

    public GETProcessor(@Inject HTTPService service) {
        super(service, GET.class);
    }

    @Override
    public HTTPMethod getHTTPMethod() {
        return HTTPMethod.GET;
    }

    @Override
    public String getPath(GET annotation) {
        return annotation.value();
    }
}
