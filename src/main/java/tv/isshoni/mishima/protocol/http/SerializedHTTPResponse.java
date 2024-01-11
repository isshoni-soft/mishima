package tv.isshoni.mishima.protocol.http;

import tv.isshoni.mishima.protocol.http.handler.HTTPService;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.parameter.New;

public class SerializedHTTPResponse extends HTTPResponse {

    private final Object body;

    private final HTTPService service;

    public SerializedHTTPResponse(@Inject HTTPService service, HTTPStatus code, MIMEType mimeType, @New HTTPHeaders headers,
                                  Object body) {
        super(code, mimeType, headers, null);

        this.service = service;
        this.body = body;
    }

    public SerializedHTTPResponse(@Inject HTTPService service, @New HTTPHeaders headers, HTTPStatus code, Object body) {
        this(service, code, MIMEType.JSON, headers, body);
    }

    @Override
    public String getBody() {
        return ((IHTTPSerializer<Object>) this.service.getSerializer(this.body.getClass())).serialize(this.body);
    }
}
