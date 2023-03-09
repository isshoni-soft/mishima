package tv.isshoni.mishima.exception;

import tv.isshoni.mishima.http.HTTPConnection;
import tv.isshoni.mishima.http.HTTPRequest;
import tv.isshoni.mishima.http.protocol.IProtocol;

public class HTTPProtocolException extends RuntimeException {

    private final IProtocol protocol;

    private final HTTPRequest request;

    private final HTTPConnection connection;

    public HTTPProtocolException(IProtocol protocol, HTTPConnection connection, HTTPRequest request, Throwable throwable) {
        super(throwable);

        this.protocol = protocol;
        this.connection = connection;
        this.request = request;
    }

    public IProtocol getProtocol() {
        return this.protocol;
    }

    public HTTPConnection getConnection() {
        return this.connection;
    }

    public HTTPRequest getRequest() {
        return this.request;
    }
}
