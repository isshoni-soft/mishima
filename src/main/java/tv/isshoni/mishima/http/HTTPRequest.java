package tv.isshoni.mishima.http;

import tv.isshoni.mishima.event.config.readonly.ReadonlyMishimaHTTPConfig;

import java.util.HashMap;
import java.util.Map;

public class HTTPRequest {

    public static final String QUERY_PARAMETER_DATA_PREFIX = "QUERY_PARAMETER_";
    public static final String PATH_PARAMETER_DATA_PREFIX = "PATH_PARAMETER_";
    public static final String HEADER_PARAMETER_DATA_PREFIX = "HEADER_PARAMETER_";
    public static final String BODY_PARAMETER_KEY = "BODY_PARAMETER";

    private final ReadonlyMishimaHTTPConfig config;

    private final HTTPMethod method;

    private final HTTPHeaders headers;

    private final Map<String, String> data;

    private final String httpVersion;
    private final String path;

    public HTTPRequest(HTTPMethod method, String path, String httpVersion, Map<String, String> queryProps,
                       Map<String, String> pathParams, HTTPHeaders headers, String body, ReadonlyMishimaHTTPConfig config) {
        this.config = config;
        this.method = method;
        this.headers = headers;
        this.path = path;
        this.httpVersion = httpVersion;
        this.data = new HashMap<>() {{
            queryProps.forEach((k, v) -> put(QUERY_PARAMETER_DATA_PREFIX + k, v));
            pathParams.forEach((k, v) -> put(PATH_PARAMETER_DATA_PREFIX + k, v));
            headers.forEach((k, v) -> put(HEADER_PARAMETER_DATA_PREFIX + k, v));

            if (body != null) {
                put(BODY_PARAMETER_KEY, body);
            }
        }};
    }

    public HTTPRequest(HTTPMethod method, String path, String httpVersion, Map<String, String> queryProps,
                       Map<String, String> pathParams, HTTPHeaders headers, ReadonlyMishimaHTTPConfig config) {
        this(method, path, httpVersion, queryProps, pathParams, headers, null, config);
    }

    public ReadonlyMishimaHTTPConfig getConfig() {
        return this.config;
    }

    public HTTPHeaders getHeaders() {
        return this.headers;
    }

    public Map<String, String> getData() {
        return this.data;
    }

    public HTTPMethod getMethod() {
        return this.method;
    }

    public String getHTTPVersion() {
        return this.httpVersion;
    }

    public String getPath() {
        return this.path;
    }

    @Override
    public String toString() {
        return this.method + " " + this.path;
    }
}
