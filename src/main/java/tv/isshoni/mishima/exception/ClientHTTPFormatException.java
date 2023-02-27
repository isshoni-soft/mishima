package tv.isshoni.mishima.exception;

import tv.isshoni.mishima.http.HTTPConnection;

public class ClientHTTPFormatException extends RuntimeException {

    private final HTTPConnection connection;

    private final String error;

    public ClientHTTPFormatException(HTTPConnection connection, String error) {
        super("HTTP Client: " + connection.toString() + " has malformed http: " + error);

        this.connection = connection;
        this.error = error;
    }

    public HTTPConnection getConnection() {
        return this.connection;
    }

    public String getError() {
        return this.error;
    }
}
