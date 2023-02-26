package tv.isshoni.mishima.http;

import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.service.ObjectFactory;

public class SerializedHTTPResponse extends HTTPResponse {

    private final Object body;

    private final HTTPService service;

    public SerializedHTTPResponse(@Inject HTTPService service, HTTPStatus code, MIMEType mimeType, HTTPHeaders headers,
                                  Object body) {
        super(code, mimeType, headers, null);

        this.service = service;
        this.body = body;

        if (!this.service.hasSerializer(this.body.getClass())) {
            throw new IllegalStateException("No serializer for type: " + this.body.getClass());
        }
    }

    public SerializedHTTPResponse(@Inject HTTPService service, @Inject ObjectFactory factory, HTTPStatus code,
                                  Object body) {
        this(service, code, MIMEType.TEXT, factory.construct(HTTPHeaders.class), body);
    }

    @Override
    public String getBody() {
        return ((IHTTPSerializer<Object>) this.service.getSerializer(this.body.getClass())).serialize(this.body);
    }
}
