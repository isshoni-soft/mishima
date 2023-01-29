package tv.isshoni.mishima.http.protocol;

import tv.isshoni.araragi.exception.Exceptions;
import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.mishima.annotation.http.Protocol;
import tv.isshoni.mishima.event.HTTPErrorEvent;
import tv.isshoni.mishima.http.HTTPConnection;
import tv.isshoni.mishima.http.HTTPErrorType;
import tv.isshoni.mishima.http.HTTPHandler;
import tv.isshoni.mishima.http.HTTPHeaders;
import tv.isshoni.mishima.http.HTTPRequest;
import tv.isshoni.mishima.http.HTTPResponse;
import tv.isshoni.mishima.http.HTTPService;
import tv.isshoni.mishima.http.HTTPStatus;
import tv.isshoni.mishima.http.IHTTPSerializer;
import tv.isshoni.mishima.http.MIMEType;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.Logger;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.api.exception.EventExecutionException;
import tv.isshoni.winry.api.service.VersionService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Protocol("1.1")
public class HTTP1 implements IProtocol {

    @Logger("HTTPv1.1") private AraragiLogger logger;

    @Inject private HTTPService service;

    @Inject private VersionService versionService;

    @Inject private IWinryContext context;

    @Override
    public void handleConnection(HTTPRequest request, HTTPConnection connection) {
        this.logger.debug("Handoff successful, using HTTP Protocol: 1.1");

        Map<String, Object> data = new HashMap<>();
        HTTPHeaders responseHeaders = new HTTPHeaders(this.versionService);
        if (request.getPath().matches("[?]")) {
            logger.info("Request with query parameters found!");
        }

        if (!this.service.hasHandler(request.getMethod(), request.getPath())) {
            logger.warn("No handler registered for: " + request);

            HTTPErrorEvent errorEvent;
            try {
                errorEvent = this.context.getEventBus().fire(new HTTPErrorEvent(HTTPErrorType.NOT_FOUND, request));
            } catch (EventExecutionException e) {
                throw Exceptions.rethrow(e);
            }

            if (!errorEvent.isCancelled()) {
                respond(request, new HTTPResponse(HTTPStatus.NOT_FOUND, MIMEType.TEXT, responseHeaders,
                        "Not Found"), connection);
            }

            return;
        }

        logger.info("Exec: " + request);
        Object result = this.service.execute(request.getMethod(), request.getPath(), data);

        HTTPResponse response = null;
        if (HTTPResponse.class.isAssignableFrom(result.getClass())) {
            response = (HTTPResponse) result;
        }

        if (response == null && this.service.hasSerializer(result.getClass())) {
            HTTPHandler handler = this.service.getHandler(request.getMethod(), request.getPath());
            response = new HTTPResponse(HTTPStatus.OK, handler.getMIMEType(), responseHeaders,
                    ((IHTTPSerializer<Object>) this.service.getSerializer(result.getClass())).serialize(result));
        }

        respond(request, response, connection);
    }

    @Override
    public void respond(HTTPRequest request, HTTPResponse response, HTTPConnection connection) {
        if (response == null) {
            throw new NullPointerException("Unable to serialize response for request: " + request);
        }

        response.getHeaders().addHeader(HTTPHeaders.CONTENT_TYPE, response.getMIMEType().getSerialized());

        connection.write("HTTP/1.1 " + response.getCode() + " " + response.getStatus().name());

        response.getHeaders().forEach((header, value) -> connection.write(header + ": " + value));

        connection.write(""); // needs blank line between headers & content
        connection.write(response.getBody());

        try {
            connection.close();
        } catch (IOException e) {
            throw Exceptions.rethrow(e);
        }
    }
}
