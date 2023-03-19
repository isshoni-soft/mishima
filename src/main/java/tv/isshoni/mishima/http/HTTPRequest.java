package tv.isshoni.mishima.http;

import java.util.HashMap;
import java.util.Map;

public class HTTPRequest {

    public static final String QUERY_PARAMETER_DATA_PREFIX = "QUERY_PARAMETER_";
    public static final String PATH_PARAMETER_DATA_PREFIX = "PATH_PARAMETER_";

    private final HTTPMethod method;

    private final Map<String, String> data;

    private final String httpVersion;
    private final String path;

    public HTTPRequest(HTTPMethod method, String path, String httpVersion, Map<String, String> queryProps,
                       Map<String, String> pathParams) {
        this.method = method;
        this.path = path;
        this.httpVersion = httpVersion;
        this.data = new HashMap<>() {{
            queryProps.forEach((k, v) -> put(QUERY_PARAMETER_DATA_PREFIX + k, v));
            pathParams.forEach((k, v) -> put(PATH_PARAMETER_DATA_PREFIX + k, v));
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
