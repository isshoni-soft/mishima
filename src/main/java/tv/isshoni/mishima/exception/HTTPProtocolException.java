package tv.isshoni.mishima.exception;

import tv.isshoni.mishima.protocol.Connection;
import tv.isshoni.mishima.protocol.http.HTTPRequest;
import tv.isshoni.mishima.protocol.IProtocol;

public class HTTPProtocolException extends RuntimeException {

    private final IProtocol protocol;

    private final HTTPRequest request;

    private final Connection connection;

    public HTTPProtocolException(IProtocol protocol, Connection connection, HTTPRequest request, Throwable throwable) {
        super(throwable);

        this.protocol = protocol;
        this.connection = connection;
        this.request = request;
    }

    public IProtocol getProtocol() {
        return this.protocol;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public HTTPRequest getRequest() {
        return this.request;
    }
}
