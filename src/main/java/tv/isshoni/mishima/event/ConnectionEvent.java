package tv.isshoni.mishima.event;

import tv.isshoni.araragi.logging.model.ILoggerFactory;
import tv.isshoni.mishima.protocol.Connection;
import tv.isshoni.winry.api.annotation.Event;

import java.io.IOException;
import java.net.Socket;

@Event("connection-event")
public class ConnectionEvent {

    private final Connection connection;

    public ConnectionEvent(Socket client, ILoggerFactory loggerFactory) {
        try {
            this.connection = new Connection(client, loggerFactory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        return this.connection;
    }
}
