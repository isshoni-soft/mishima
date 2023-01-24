package tv.isshoni.mishima.http.protocol;

import tv.isshoni.mishima.http.IncomingHTTPRequest;

public interface IProtocol {

    void handleConnection(IncomingHTTPRequest request);
}
