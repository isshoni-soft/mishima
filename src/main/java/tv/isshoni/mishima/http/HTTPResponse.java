package tv.isshoni.mishima.http;

public class HTTPResponse {

    private final int code;

    private final String response;

    public HTTPResponse(int code, String response) {
        this.code = code;
        this.response = response;
    }
}
