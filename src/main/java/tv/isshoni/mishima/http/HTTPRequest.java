package tv.isshoni.mishima.http;

import java.io.Reader;
import java.net.Socket;

public class HTTPRequest {

    private final HTTPMethod method;

    private final String httpVersion;
    private final String path;

    private final Socket clientSocket;
    private final Reader clientReader;

    public HTTPRequest(HTTPMethod method, String path, String httpVersion, Socket clientSocket, Reader clientReader) {
        this.method = method;
        this.path = path;
        this.httpVersion = httpVersion;
        this.clientSocket = clientSocket;
        this.clientReader = clientReader;
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

    public Socket getClientSocket() {
        return this.clientSocket;
    }

    public Reader getClientReader() {
        return this.clientReader;
    }
}
