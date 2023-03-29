package tv.isshoni.mishima.event.config;

import java.util.List;

public class ReadonlyMishimaConfig {

    private final MishimaConfigEvent event;

    public ReadonlyMishimaConfig(MishimaConfigEvent event) {
        this.event = event;
    }

    public int getPort() {
        return this.event.getPort();
    }

    public long getCORSMaxAge() {
        return this.event.getCORSMaxAge();
    }

    public String getCORSAllowOrigin() {
        return this.event.getCORSAllowOrigin();
    }

    public List<String> getCORSAllowedHeaders() {
        return this.event.getCORSAllowedHeaders();
    }

    public boolean isTLS() {
        return this.event.isTLS();
    }

    public boolean isCORS() {
        return this.event.isCORS();
    }
}
