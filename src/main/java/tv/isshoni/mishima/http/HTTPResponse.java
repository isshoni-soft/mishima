package tv.isshoni.mishima.http;

public class HTTPResponse {

    private final HTTPStatus code;

    private final String response;

    public HTTPResponse(HTTPStatus code, String response) {
        this.code = code;
        this.response = response;
    }

    public HTTPStatus getStatus() {
        return this.code;
    }

    public int getCode() {
        return this.code.getCode();
    }

    public String getResponse() {
        return this.response;
    }
}
