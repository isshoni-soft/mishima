package tv.isshoni.mishima.event;

import tv.isshoni.mishima.http.HTTPConnection;
import tv.isshoni.winry.api.annotation.Event;

import java.io.IOException;
import java.net.Socket;

@Event("connection-event")
public class ConnectionEvent {

    private final HTTPConnection connection;

    public ConnectionEvent(Socket client) {
        try {
            this.connection = new HTTPConnection(client);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HTTPConnection getConnection() {
        return this.connection;
    }
}
