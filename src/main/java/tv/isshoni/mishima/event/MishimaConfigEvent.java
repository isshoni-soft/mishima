package tv.isshoni.mishima.event;

import tv.isshoni.winry.api.annotation.Event;

@Event(value = "mishima-config")
public class MishimaConfigEvent {

    private int port;

    private boolean tls;

    public MishimaConfigEvent() {
        this.port = -1;
        this.tls = false;
    }

    public MishimaConfigEvent useTLS() {
        this.tls = true;

        return this;
    }

    public MishimaConfigEvent port(int port) {
        if (port <= 0) {
            throw new IllegalArgumentException("cannot host webserver on port <= 0");
        }

        this.port = port;

        return this;
    }

    public boolean isTLS() {
        return this.tls;
    }

    public int getPort() {
        return this.port;
    }

    public boolean isValid() {
        return this.port != -1;
    }
}
