package tv.isshoni.mishima.event;

import tv.isshoni.winry.api.annotation.Event;

@Event(value = "mishima-config")
public class MishimaConfigEvent {

    private int port = -1;

    public MishimaConfigEvent port(int port) {
        if (port <= 0) {
            throw new IllegalArgumentException("cannot host webserver on port <= 0");
        }

        this.port = port;

        return this;
    }

    public int getPort() {
        return this.port;
    }

    public boolean isValid() {
        return this.port != -1;
    }
}
