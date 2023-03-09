package tv.isshoni.mishima.http;

import java.util.HashMap;
import java.util.Map;

// TODO: Add query parameters here and do processing when this is constructed
public class HTTPRequest {

    public static final String QUERY_PARAMETER_DATA_PREFIX = "QUERY_PARAMETER_";

    private final HTTPMethod method;

    private final Map<String, String> data;

    private final String httpVersion;
    private final String path;

    public HTTPRequest(HTTPMethod method, String path, String httpVersion, Map<String, String> queryProps) {
        this.method = method;
        this.path = path;
        this.httpVersion = httpVersion;
        this.data = new HashMap<>() {{
            queryProps.forEach((k, v) -> put(QUERY_PARAMETER_DATA_PREFIX + k, v));
        }};
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
