package tv.isshoni.mishima.http.protocol;

import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.mishima.annotation.http.Protocol;
import tv.isshoni.mishima.http.HTTPRequest;
import tv.isshoni.winry.api.annotation.Logger;

@Protocol("1.1")
public class HTTP1 implements IProtocol {

    @Logger("HTTPv1.1") private AraragiLogger logger;

    @Override
    public void handleConnection(HTTPRequest request) {
        this.logger.debug("Handoff successful, using HTTP Protocol: " + getVersion());
    }

    @Override
    public String getVersion() {
        return "1.1";
    }
}
