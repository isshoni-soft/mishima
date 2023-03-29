package tv.isshoni.mishima.event;

import tv.isshoni.mishima.event.config.ReadonlyMishimaConfig;
import tv.isshoni.mishima.http.HTTPConnection;
import tv.isshoni.winry.api.annotation.Event;
import tv.isshoni.winry.api.context.ILoggerFactory;

import java.io.IOException;
import java.net.Socket;

@Event("connection-event")
public class ConnectionEvent {

    private final HTTPConnection connection;

    private final ReadonlyMishimaConfig config;

    public ConnectionEvent(Socket client, ILoggerFactory loggerFactory, ReadonlyMishimaConfig config) {
        try {
            this.config = config;
            this.connection = new HTTPConnection(client, loggerFactory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ReadonlyMishimaConfig getConfig() {
        return this.config;
    }

    public HTTPConnection getConnection() {
        return this.connection;
    }
}
