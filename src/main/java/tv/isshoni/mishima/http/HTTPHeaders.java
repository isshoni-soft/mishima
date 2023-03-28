package tv.isshoni.mishima.http;

import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.service.VersionService;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class HTTPHeaders {

    public static final String KEEP_ALIVE = "Keep-Alive";
    public static final String VARY = "Vary";
    public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
    public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String CONNECTION = "Connection";
    public static final String ORIGIN = "Origin";
    public static final String ACCEPT = "Accept";
    public static final String ALLOW = "Allow";
    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String HOST = "Host";
    public static final String DATE = "Date";
    public static final String SERVER = "Server";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";

    private final Map<String, String> headers;

    public HTTPHeaders() {
        this.headers = new HashMap<>();
    }

    public HTTPHeaders(@Inject VersionService versionService) {
        this();

        addHeader(DATE, new Date().toString());
        addHeader(SERVER, "Mishima/v" + versionService.getVersion("mishima").get());
    }

    public boolean hasHeader(String header) {
        return this.headers.containsKey(header);
    }

    public void forEach(BiConsumer<String, String> consumer) {
        this.headers.forEach(consumer);
    }

    public void addHeader(String header, String value) {
        this.headers.put(header, value);
    }

    public String getHeader(String header) {
        return this.headers.get(header);
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(this.headers);
    }
}
