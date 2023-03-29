package tv.isshoni.mishima.event.config.readonly;

import tv.isshoni.mishima.event.config.MishimaHTTPConfigEvent;

import java.util.List;

public class ReadonlyMishimaHTTPConfig {

    private final MishimaHTTPConfigEvent event;

    public ReadonlyMishimaHTTPConfig(MishimaHTTPConfigEvent event) {
        this.event = event;
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

    public boolean isCORS() {
        return this.event.isCORS();
    }
}
