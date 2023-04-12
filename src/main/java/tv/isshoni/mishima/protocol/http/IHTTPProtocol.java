package tv.isshoni.mishima.protocol.http;

import tv.isshoni.mishima.protocol.Connection;

// TODO maybe just make this some type of marker class for a kitted handler..?
public interface IHTTPProtocol {

//    void handleConnection(HTTPRequest request, Connection connection);

    void respond(HTTPResponse response, Connection connection);
}
