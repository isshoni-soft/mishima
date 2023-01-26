package tv.isshoni.mishima.http.protocol;

import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.mishima.annotation.http.Protocol;
import tv.isshoni.mishima.event.HTTPErrorEvent;
import tv.isshoni.mishima.http.HTTPErrorType;
import tv.isshoni.mishima.http.HTTPResponse;
import tv.isshoni.mishima.http.HTTPService;
import tv.isshoni.mishima.http.HTTPRequest;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.Logger;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.api.exception.EventExecutionException;

import java.util.HashMap;
import java.util.Map;

@Protocol("1.1")
public class HTTP1 implements IProtocol {

    @Logger("HTTPv1.1") private AraragiLogger logger;

    @Inject private HTTPService service;

    @Inject private IWinryContext context;

    @Override
    public void handleConnection(HTTPRequest request) {
        this.logger.debug("Handoff successful, using HTTP Protocol: 1.1");

        Map<String, Object> data = new HashMap<>();
        if (request.getPath().matches("[?]")) {
            logger.info("Request with query parameters found!");
        }

        if (!this.service.hasHandler(request.getMethod(), request.getPath())) {
            logger.warn("No handler registered for: " + request);

            HTTPErrorEvent errorEvent;
            try {
                errorEvent = this.context.getEventBus().fire(new HTTPErrorEvent(HTTPErrorType.NOT_FOUND, request));
            } catch (EventExecutionException e) {
                throw new RuntimeException(e);
            }

            if (!errorEvent.isCancelled()) {
                // TODO: Send back error code 404 & close client socket
            }

            return;
        }

        logger.info("Exec: " + request);
        Object result = this.service.execute(request.getMethod(), request.getPath(), data);


    }

    @Override
    public void send(HTTPResponse response) {

    }
}
