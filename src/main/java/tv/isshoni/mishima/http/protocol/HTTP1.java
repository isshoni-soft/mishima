package tv.isshoni.mishima.http.protocol;

import tv.isshoni.araragi.exception.Exceptions;
import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.mishima.annotation.http.Protocol;
import tv.isshoni.mishima.exception.HTTPProtocolException;
import tv.isshoni.mishima.http.HTTPConnection;
import tv.isshoni.mishima.http.handler.HTTPHandler;
import tv.isshoni.mishima.http.HTTPHeaders;
import tv.isshoni.mishima.http.handler.HTTPProtocolExceptionHandler;
import tv.isshoni.mishima.http.HTTPRequest;
import tv.isshoni.mishima.http.HTTPResponse;
import tv.isshoni.mishima.http.handler.HTTPService;
import tv.isshoni.mishima.http.HTTPStatus;
import tv.isshoni.mishima.http.IHTTPSerializer;
import tv.isshoni.mishima.http.MIMEType;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.Logger;
import tv.isshoni.winry.api.annotation.exception.ExceptionHandler;
import tv.isshoni.winry.api.context.IEventBus;
import tv.isshoni.winry.api.context.IExceptionManager;
import tv.isshoni.winry.api.service.ObjectFactory;
import tv.isshoni.winry.api.service.VersionService;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Protocol("1.1")
public class HTTP1 implements IProtocol {

    @Logger("HTTPv1.1") private AraragiLogger logger;

    @Inject private HTTPService service;

    @Inject private VersionService versionService;

    @Inject private IEventBus eventBus;

    @Inject private ObjectFactory objectFactory;

    @Inject private IExceptionManager exceptionManager;

    @Override
    @ExceptionHandler(HTTPProtocolExceptionHandler.class)
    public void handleConnection(HTTPRequest request, HTTPConnection connection) {
        Method enclosing = new Object(){}.getClass().getEnclosingMethod();

        this.logger.debug("Handoff successful, using HTTP Protocol: 1.1");

        Map<String, Object> data = new HashMap<>(request.getData());
        data.put("request", request);

        String path = request.getPath();
        HTTPHeaders responseHeaders = this.objectFactory.construct(HTTPHeaders.class);

        if (!this.service.hasHandler(request.getMethod(), path)) {
            logger.warn("No handler registered for: " + request);
            respond(request, new HTTPResponse(HTTPStatus.NOT_FOUND, MIMEType.TEXT, responseHeaders,
                    "Not Found"), connection);

            return;
        }

        logger.info(request.toString());

        Object result;
        HTTPResponse response = null;
        try {
            result = this.service.execute(request.getMethod(), request.getPath(), data);
        } catch (Exception e) {
            this.exceptionManager.toss(new HTTPProtocolException(this, connection, request, e), enclosing);
            return;
        }

        if (HTTPResponse.class.isAssignableFrom(result.getClass())) {
            response = (HTTPResponse) result;
        }

        if (response == null) {
            IHTTPSerializer<Object> serializer = (IHTTPSerializer<Object>) this.service.getSerializer(result.getClass());
            HTTPHandler handler = this.service.getHandler(request.getMethod(), request.getPath());

            String body;
            if (serializer != null) {
                body = serializer.serialize(result);
            } else {
                body = HTTPService.GSON.toJson(result);
            }

            response = new HTTPResponse(HTTPStatus.OK, handler.getMIMEType(), responseHeaders, body);
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
