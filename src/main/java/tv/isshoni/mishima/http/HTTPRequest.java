package tv.isshoni.mishima.http;

public class HTTPRequest {

    private final HTTPMethod method;

    private final String httpVersion;
    private final String path;

    public HTTPRequest(HTTPMethod method, String path, String httpVersion) {
        this.method = method;
        this.path = path;
        this.httpVersion = httpVersion;
    }

    public HTTPMethod getMethod() {
        return this.method;
    }

    public String getHTTPVersion() {
        return this.httpVersion;
    }

    public String getPath() {
        return this.path;
    }

    @Override
    public String toString() {
        return this.method + " : " + this.path;
    }
}
