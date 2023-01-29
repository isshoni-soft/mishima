package tv.isshoni.mishima.http;

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
