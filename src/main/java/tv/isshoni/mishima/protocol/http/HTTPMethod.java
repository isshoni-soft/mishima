package tv.isshoni.mishima.protocol.http;

public enum HTTPMethod {
    OPTIONS(false, false),
    GET(true, true),
    HEAD(false, false),
    POST(true, true),
    PUT(true, true),
    DELETE(false, true),
    TRACE(false, false),
    CONNECT(false, false);

    private final boolean incomingBody;
    private final boolean outgoingBody;

    HTTPMethod(boolean incomingBody, boolean outgoingBody) {
        this.incomingBody = incomingBody;
        this.outgoingBody = outgoingBody;
    }

    public boolean hasIncomingBody() {
        return this.incomingBody;
    }

    public boolean hasOutgoingBody() {
        return this.outgoingBody;
    }
}
