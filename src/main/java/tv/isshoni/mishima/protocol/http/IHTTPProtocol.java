package tv.isshoni.mishima.protocol.http;

import tv.isshoni.mishima.protocol.Connection;
import tv.isshoni.mishima.protocol.http.HTTPRequest;
import tv.isshoni.mishima.protocol.http.HTTPResponse;

// TODO maybe just make this some type of marker class for a kitted handler..?
public interface IHTTPProtocol {

//    void handleConnection(HTTPRequest request, Connection connection);

    void respond(HTTPRequest request, HTTPResponse response, Connection connection);
}
