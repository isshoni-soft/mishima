package tv.isshoni.mishima.exception;

import tv.isshoni.mishima.protocol.Connection;
import tv.isshoni.mishima.protocol.http.HTTPRequest;
import tv.isshoni.mishima.protocol.http.IHTTPProtocol;

public class HTTPProtocolException extends RuntimeException {

    private final IHTTPProtocol protocol;

    private final HTTPRequest request;

    private final Connection connection;

    public HTTPProtocolException(IHTTPProtocol protocol, Connection connection, HTTPRequest request, Throwable throwable) {
        super(throwable);

        this.protocol = protocol;
        this.connection = connection;
        this.request = request;
    }

    public IHTTPProtocol getProtocol() {
        return this.protocol;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public HTTPRequest getRequest() {
        return this.request;
    }
}
