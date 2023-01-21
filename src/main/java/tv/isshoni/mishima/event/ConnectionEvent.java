package tv.isshoni.mishima.event;

import tv.isshoni.winry.api.annotation.Event;

import java.net.Socket;

@Event("connection-event")
public class ConnectionEvent {

    private final Socket client;

    public ConnectionEvent(Socket client) {
        this.client = client;
    }

    public Socket getClient() {
        return this.client;
    }
}
