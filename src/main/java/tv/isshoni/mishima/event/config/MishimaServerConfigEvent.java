package tv.isshoni.mishima.event.config;

import tv.isshoni.winry.api.annotation.Event;

@Event(value = "mishima-server-config")
public class MishimaServerConfigEvent {

    private int port;

    private boolean http;

    private String keystorePath;
    private String keystorePassword;

    public MishimaServerConfigEvent() {
        this.port = -1;
        this.http = false;
        this.keystorePath = null;
    }

    public MishimaServerConfigEvent http() {
        this.http = true;

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
        return this.http;
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
}
