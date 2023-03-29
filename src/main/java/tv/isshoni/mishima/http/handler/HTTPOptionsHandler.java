package tv.isshoni.mishima.http.handler;

import tv.isshoni.araragi.data.Pair;
import tv.isshoni.araragi.stream.Streams;
import tv.isshoni.mishima.http.HTTPHeaders;
import tv.isshoni.mishima.http.HTTPMethod;
import tv.isshoni.mishima.http.HTTPRequest;
import tv.isshoni.mishima.http.HTTPResponse;
import tv.isshoni.mishima.http.HTTPStatus;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.api.service.ObjectFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HTTPOptionsHandler extends HTTPHandler {

    protected final HTTPService service;

    protected final ObjectFactory objectFactory;

    public HTTPOptionsHandler(IWinryContext context, HTTPService service, ObjectFactory objectFactory) {
        super(context, null, null, null);

        this.service = service;
        this.objectFactory = objectFactory;
    }

    @Override
    public Object execute(Map<String, Object> provided) {
        HTTPRequest request = (HTTPRequest) provided.get("request");

        List<Pair<HTTPMethod, HTTPHandler>> handlers = this.service.getHandlersForPath(request.getPath());

        Set<HTTPMethod> already = new HashSet<>();

        HTTPHeaders headers = this.objectFactory.construct(HTTPHeaders.class);
        headers.addHeader(HTTPHeaders.ALLOW, Streams.to(handlers)
                .collapse((p, s) -> {
                    if (already.contains(p.getFirst())) {
                        return s;
                    }

                    already.add(p.getFirst());

                    String str = p.getFirst().name();
                    if (s == null) {
                        return str;
                    } else {
                        return s + ", " + str;
                    }
                }));

        return new HTTPResponse(HTTPStatus.OK, null, headers, "");
    }
}
