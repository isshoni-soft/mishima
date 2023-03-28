package tv.isshoni.mishima.event;

import tv.isshoni.winry.api.annotation.Event;

@Event(value = "mishima-config")
public class MishimaConfigEvent {

    private int port;

    private String keystorePath = null;
    private String keystorePassword;

    public MishimaConfigEvent() {
        this.port = -1;
    }

    public MishimaConfigEvent useTLS(String keystorePath, String keystorePassword) {
        this.keystorePath = keystorePath;
        this.keystorePassword = keystorePassword;

        return this;
    }

    public MishimaConfigEvent port(int port) {
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

    public boolean isTLS() {
        return this.keystorePath != null && !this.keystorePath.isEmpty();
    }

    public int getPort() {
        return this.port;
    }

    public boolean isValid() {
        if (isTLS() && (this.keystorePassword == null || this.keystorePassword.length() < 6)) {
            return false;
        }

        return this.port != -1;
    }
}
