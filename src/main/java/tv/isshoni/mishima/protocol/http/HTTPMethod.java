package tv.isshoni.mishima.protocol.http;

public enum HTTPMethod {
    OPTIONS(false),
    GET(false),
    HEAD(false),
    POST(true),
    PUT(false),
    DELETE(false),
    TRACE(false),
    CONNECT(false);

    private final boolean hasBody;

    HTTPMethod(boolean hasBody) {
        this.hasBody = hasBody;
    }

    public boolean hasBody() {
        return this.hasBody;
    }
}
