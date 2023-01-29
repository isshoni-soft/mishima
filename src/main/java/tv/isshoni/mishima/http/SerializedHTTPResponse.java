package tv.isshoni.mishima.http;

public class SerializedHTTPResponse extends HTTPResponse {

    private final Object body;

    private final HTTPService service;

    public SerializedHTTPResponse(HTTPService service, HTTPStatus code, MIMEType mimeType, HTTPHeaders headers,
                                  Object body) {
        super(code, mimeType, headers, null);
        this.service = service;
        this.body = body;

        if (!this.service.hasSerializer(this.body.getClass())) {
            throw new IllegalStateException("No serializer for type: " + this.body.getClass());
        }
    }

    public SerializedHTTPResponse(HTTPService service, HTTPStatus code, Object body) {
        super(code, null, new HTTPHeaders(), body);
    }

    @Override
    public String getBody() {
        return ((IHTTPSerializer<Object>) this.service.getSerializer(this.body.getClass())).serialize(this.body);
    }
}
