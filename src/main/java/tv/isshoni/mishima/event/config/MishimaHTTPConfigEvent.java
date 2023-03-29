package tv.isshoni.mishima.event.config;

import tv.isshoni.winry.api.annotation.Event;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Event("mishima-http-config")
public class MishimaHTTPConfigEvent {

    private long corsMaxAge;

    private String corsAllowOrigin;

    private final List<String> allowHeaders;

    public MishimaHTTPConfigEvent() {
        this.corsMaxAge = -1;
        this.corsAllowOrigin = null;
        this.allowHeaders = new LinkedList<>();
    }

    public MishimaHTTPConfigEvent corsAllowedHeaders(String... header) {
        this.allowHeaders.addAll(Arrays.asList(header));

        return this;
    }

    public MishimaHTTPConfigEvent corsMaxAge(long maxAge) {
        if (maxAge < 0) {
            maxAge = 0;
        }

        this.corsMaxAge = maxAge;

        return this;
    }

    public MishimaHTTPConfigEvent corsAllowOrigin(String allowOrigin) {
        this.corsAllowOrigin = allowOrigin;

        return this;
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

    public long getCORSMaxAge() {
        return this.corsMaxAge;
    }
}
