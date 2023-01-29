package tv.isshoni.mishima.http;

import tv.isshoni.winry.api.service.VersionService;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class HTTPHeaders {

    public static final String DATE = "Date";
    public static final String SERVER = "Server";
    public static final String CONTENT_TYPE = "Content-type";

    private final Map<String, String> headers;

    public HTTPHeaders(VersionService versionService) {
        this.headers = new HashMap<>();

        addHeader(DATE, new Date().toString());
        addHeader(SERVER, "Mishima/v" + versionService.getVersion("mishima").get());
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
