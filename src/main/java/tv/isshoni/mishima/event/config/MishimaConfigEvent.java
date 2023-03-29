package tv.isshoni.mishima.event.config;

import tv.isshoni.winry.api.annotation.Event;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Event(value = "mishima-config")
public class MishimaConfigEvent {

    private int port;

    private long corsMaxAge;

    private String keystorePath;
    private String keystorePassword;
    private String corsAllowOrigin;

    private final List<String> allowHeaders;

    public MishimaConfigEvent() {
        this.port = -1;
        this.corsMaxAge = -1;
        this.keystorePath = null;
        this.corsAllowOrigin = null;
        this.allowHeaders = new LinkedList<>();
    }

    public MishimaConfigEvent corsAllowedHeaders(String... header) {
        this.allowHeaders.addAll(Arrays.asList(header));

        return this;
    }

    public MishimaConfigEvent corsMaxAge(long maxAge) {
        if (maxAge < 0) {
            maxAge = 0;
        }

        this.corsMaxAge = maxAge;

        return this;
    }

    public MishimaConfigEvent corsAllowOrigin(String allowOrigin) {
        this.corsAllowOrigin = allowOrigin;

        return this;
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

    public String getCORSAllowOrigin() {
        return this.corsAllowOrigin;
    }

    public List<String> getCORSAllowedHeaders() {
        return new LinkedList<>(this.allowHeaders);
    }

    public boolean isCORS() {
        return this.corsAllowOrigin != null;
    }

    public boolean isTLS() {
        return this.keystorePath != null && !this.keystorePath.isEmpty();
    }

    public long getCORSMaxAge() {
        return this.corsMaxAge;
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
