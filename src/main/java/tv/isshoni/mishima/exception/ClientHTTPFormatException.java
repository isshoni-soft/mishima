package tv.isshoni.mishima.exception;

import tv.isshoni.mishima.protocol.Connection;

public class ClientHTTPFormatException extends RuntimeException {

    private final Connection connection;

    private final String error;

    public ClientHTTPFormatException(Connection connection, String error) {
        super("HTTP Client: " + connection.toString() + " has malformed http: " + error);

        this.connection = connection;
        this.error = error;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public String getError() {
        return this.error;
    }
}
