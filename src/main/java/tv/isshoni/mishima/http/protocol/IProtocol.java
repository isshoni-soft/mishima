package tv.isshoni.mishima.http.protocol;

import tv.isshoni.mishima.http.HTTPConnection;
import tv.isshoni.mishima.http.HTTPResponse;
import tv.isshoni.mishima.http.HTTPRequest;

public interface IProtocol {

    void handleConnection(HTTPRequest request, HTTPConnection connection);

    void send(HTTPRequest request, HTTPResponse response, HTTPConnection connection);
}
