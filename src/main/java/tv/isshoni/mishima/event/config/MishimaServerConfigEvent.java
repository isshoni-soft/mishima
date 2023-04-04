package tv.isshoni.mishima.event.config;

import tv.isshoni.winry.api.annotation.Event;

@Event(value = "mishima-server-config")
public class MishimaServerConfigEvent {

    private int port;

    private Type type;

    private String keystorePath;
    private String keystorePassword;

    public MishimaServerConfigEvent() {
        this.port = -1;
        this.type = Type.EVENT;
        this.keystorePath = null;
    }

    public MishimaServerConfigEvent http() {
        this.type = Type.HTTP;

        return this;
    }

    public MishimaServerConfigEvent event() {
        this.type = Type.EVENT;

        return this;
    }

    public MishimaServerConfigEvent packet() {
        this.type = Type.PACKET;

        return this;
    }

    public MishimaServerConfigEvent useTLS(String keystorePath, String keystorePassword) {
        this.keystorePath = keystorePath;
        this.keystorePassword = keystorePassword;

        return this;
    }

    public MishimaServerConfigEvent port(int port) {
        if (port <= 0) {
            throw new IllegalArgumentException("cannot host webserver on port <= 0");
        }

        this.port = port;

        return this;
    }

    public String getKeystorePassword() {
        return this.keystorePassword;
    }

    public String getKeystorePath() {
        return this.keystorePath;
    }

    public boolean isHTTP() {
        return this.type == Type.HTTP;
    }

    public boolean isPacket() {
        return this.type == Type.PACKET;
    }

    public boolean isEvent() {
        return this.type == Type.EVENT;
    }

    public boolean isTLS() {
        return this.keystorePath != null && !this.keystorePath.isEmpty();
    }

    public int getPort() {
        return this.port;
    }

    public boolean isValid() {
        if (isTLS() && this.keystorePassword == null) {
            return false;
        }

        return this.port != -1;
    }

    public enum Type {
        HTTP,
        PACKET,
        EVENT
    }
}
