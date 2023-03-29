package tv.isshoni.mishima.http;

import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.service.ObjectFactory;

public class HTTPResponse {

    private final HTTPStatus code;

    private final MIMEType mimeType;

    private final HTTPHeaders headers;

    private final String body;

    public HTTPResponse(HTTPStatus code, MIMEType mimeType, HTTPHeaders headers, String body) {
        this.code = code;
        this.mimeType = mimeType;
        this.headers = headers;
        this.body = body;
    }

    public HTTPResponse(@Inject ObjectFactory factory, HTTPStatus code, MIMEType mimeType, String body) {
        this(code, mimeType, factory.construct(HTTPHeaders.class), body);
    }

    public HTTPHeaders getHeaders() {
        return this.headers;
    }

    public MIMEType getMIMEType() {
        return this.mimeType;
    }

    public HTTPStatus getStatus() {
        return this.code;
    }

    public int getCode() {
        return this.code.getCode();
    }

    public String getBody() {
        return this.body;
    }
}
