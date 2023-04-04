package tv.isshoni.mishima.protocol.http.handler;

import tv.isshoni.araragi.data.Pair;
import tv.isshoni.araragi.stream.Streams;
import tv.isshoni.mishima.protocol.http.HTTPHeaders;
import tv.isshoni.mishima.protocol.http.HTTPMethod;
import tv.isshoni.mishima.protocol.http.HTTPRequest;
import tv.isshoni.mishima.protocol.http.HTTPResponse;
import tv.isshoni.mishima.protocol.http.HTTPStatus;
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

        Set<HTTPMethod> t = new HashSet<>();
        String allow = Streams.to(handlers)
                .collapse((p, s) -> {
                    if (t.contains(p.getFirst())) {
                        return s;
                    }

                    t.add(p.getFirst());

                    String str = p.getFirst().name();
                    if (s == null) {
                        return str;
                    } else {
                        return s + ", " + str;
                    }
                });

        HTTPHeaders headers = this.objectFactory.construct(HTTPHeaders.class);
        headers.addHeader(HTTPHeaders.ALLOW, allow);

        if (request.getConfig().isCORS()) {
            headers.addHeader(HTTPHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, request.getConfig().getCORSAllowOrigin());
            headers.addHeader(HTTPHeaders.ACCESS_CONTROL_ALLOW_METHODS, allow);

            if (request.getConfig().getCORSMaxAge() >= 0) {
                headers.addHeader(HTTPHeaders.ACCESS_CONTROL_MAX_AGE, Long.toString(request.getConfig().getCORSMaxAge()));
            }

            if (!request.getConfig().getCORSAllowedHeaders().isEmpty()) {
                Set<String> a = new HashSet<>();

                headers.addHeader(HTTPHeaders.ACCESS_CONTROL_REQUEST_HEADERS, Streams.to(request.getConfig().getCORSAllowedHeaders())
                        .collapse((p, s) -> {
                            if (a.contains(p)) {
                                return s;
                            }

                            a.add(p);

                            if (s == null) {
                                return p;
                            } else {
                                return s + ", " + p;
                            }
                        }));
            }
        }

        return new HTTPResponse(HTTPStatus.OK, null, headers, "");
    }
}
