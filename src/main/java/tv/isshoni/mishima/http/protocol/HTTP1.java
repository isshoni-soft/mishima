package tv.isshoni.mishima.http.protocol;

import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.mishima.annotation.http.Protocol;
import tv.isshoni.mishima.http.HTTPService;
import tv.isshoni.mishima.http.IncomingHTTPRequest;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.Logger;

import java.util.HashMap;
import java.util.Map;

@Protocol("1.1")
public class HTTP1 implements IProtocol {

    @Logger("HTTPv1.1") private AraragiLogger logger;

    @Inject private HTTPService service;

    @Override
    public void handleConnection(IncomingHTTPRequest request) {
        this.logger.debug("Handoff successful, using HTTP Protocol: " + getVersion());

        Map<String, Object> data = new HashMap<>();

        this.service.execute(request.getMethod(), request.getPath(), data);
    }

    @Override
    public String getVersion() {
        return "1.1";
    }
}
