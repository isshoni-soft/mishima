package tv.isshoni.mishima.http.protocol;

import tv.isshoni.mishima.http.HTTPRequest;

public interface IProtocol {

    void handleConnection(HTTPRequest request);

    String getVersion();
}
