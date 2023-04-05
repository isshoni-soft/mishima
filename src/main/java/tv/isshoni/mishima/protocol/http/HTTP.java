package tv.isshoni.mishima.protocol.http;

import tv.isshoni.araragi.exception.Exceptions;
import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.araragi.stream.Streams;
import tv.isshoni.mishima.annotation.http.HTTPSerializer;
import tv.isshoni.mishima.annotation.http.method.DELETE;
import tv.isshoni.mishima.annotation.http.method.GET;
import tv.isshoni.mishima.annotation.http.method.OPTIONS;
import tv.isshoni.mishima.annotation.http.method.POST;
import tv.isshoni.mishima.annotation.http.method.PUT;
import tv.isshoni.mishima.annotation.http.parameter.Body;
import tv.isshoni.mishima.annotation.http.parameter.Path;
import tv.isshoni.mishima.annotation.http.parameter.Query;
import tv.isshoni.mishima.event.ConnectionEvent;
import tv.isshoni.mishima.event.config.MishimaHTTPConfigEvent;
import tv.isshoni.mishima.event.config.readonly.ReadonlyMishimaHTTPConfig;
import tv.isshoni.mishima.exception.HTTPFormatException;
import tv.isshoni.mishima.exception.HTTPProtocolException;
import tv.isshoni.mishima.protocol.Connection;
import tv.isshoni.mishima.protocol.http.handler.HTTPHandler;
import tv.isshoni.mishima.protocol.http.handler.HTTPProtocolExceptionHandler;
import tv.isshoni.mishima.protocol.http.handler.HTTPService;
import tv.isshoni.winry.api.annotation.Event;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.Injected;
import tv.isshoni.winry.api.annotation.Listener;
import tv.isshoni.winry.api.annotation.Logger;
import tv.isshoni.winry.api.annotation.exception.ExceptionHandler;
import tv.isshoni.winry.api.annotation.parameter.Context;
import tv.isshoni.winry.api.annotation.transformer.Async;
import tv.isshoni.winry.api.context.IEventBus;
import tv.isshoni.winry.api.context.IExceptionManager;
import tv.isshoni.winry.api.context.IWinryContext;
import tv.isshoni.winry.api.exception.EventExecutionException;
import tv.isshoni.winry.api.service.ObjectFactory;
import tv.isshoni.winry.api.service.VersionService;
import tv.isshoni.winry.internal.model.annotation.IWinryAnnotationManager;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Injected
public class HTTP implements IHTTPProtocol {

    @Logger("HTTP") private AraragiLogger logger;

    @Inject private HTTPService service;

    @Inject private VersionService versionService;

    @Inject private IEventBus eventBus;

    @Inject private ObjectFactory objectFactory;

    @Inject private IExceptionManager exceptionManager;

    private final MishimaHTTPConfigEvent httpConfig;

    public HTTP(@Context IWinryContext context) {
        this.httpConfig = new MishimaHTTPConfigEvent();

        IWinryAnnotationManager annotationManager = context.getAnnotationManager();
        annotationManager.discoverAnnotation(HTTPSerializer.class);
        annotationManager.discoverAnnotation(GET.class);
        annotationManager.discoverAnnotation(POST.class);
        annotationManager.discoverAnnotation(OPTIONS.class);
        annotationManager.discoverAnnotation(PUT.class);
        annotationManager.discoverAnnotation(DELETE.class);
        annotationManager.discoverAnnotation(Body.class);
        annotationManager.discoverAnnotation(Path.class);
        annotationManager.discoverAnnotation(Query.class);

        try {
            context.getEventBus().fire(this.httpConfig);
        } catch (EventExecutionException e) {
            context.getExceptionManager().toss(e);
        }
    }

    @Listener(ConnectionEvent.class)
    @Async
    public void handleNewConnection(@Event ConnectionEvent event, @Inject ObjectFactory factory) throws IOException {
        Connection connection = event.getConnection();

        // process first line
        String line = connection.readLine();
        String[] tokens = line.split(" ");

        if (tokens.length != 3) {
            throw new HTTPFormatException("malformed first line");
        }

        HTTPMethod method;
        try {
            method = HTTPMethod.valueOf(tokens[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new HTTPFormatException(tokens[0] + " is not a parsable HTTP method");
        }

        HTTPHeaders requestHeaders = new HTTPHeaders();

        // read all headers, read until newline between headers & body is encountered.
        String dataLine;
        do {
            dataLine = connection.readLine();

            if (dataLine.contains(":")) {
                String[] headerTokens = dataLine.split(": ");

                requestHeaders.addHeader(headerTokens[0], headerTokens[1]);
            }
        } while (dataLine.length() != 0);

        // find content-length header & read body.
        String body = "";
        if (method.hasIncomingBody() && requestHeaders.hasHeader(HTTPHeaders.CONTENT_LENGTH)) {
            int contentLength = Integer.parseInt(requestHeaders.getHeader(HTTPHeaders.CONTENT_LENGTH));

            body = connection.read(contentLength);
        }

        String[] versionTokens = tokens[2].split("/");

        if (versionTokens.length != 2) {
            throw new HTTPFormatException(tokens[2] + " is not a parsable HTTP version!");
        }

        String httpVersion = versionTokens[1];
        String path = tokens[1]; // TODO: Apply basic URL regex checks
        Map<String, String> queryParameters = new HashMap<>();

        // parse query parameters
        if (path.contains("?")) {
            int endPath = path.lastIndexOf("?");
            String queryParams = path.substring(endPath + 1);
            String[] serializedEntries = queryParams.split("&");

            Streams.to(serializedEntries).forEach(e -> {
                String[] t = e.split("=");

                if (t.length != 2) {
                    throw new HTTPFormatException("Malformed query parameter!");
                }

                queryParameters.put(t[0], t[1]);
            });

            path = path.substring(0, path.lastIndexOf("?"));
        }

        if (!path.endsWith("/")) {
            path = path + "/";
        }

        Map<String, String> pathParams = new HashMap<>();
        if (this.service.hasHandler(method, path)) {
            this.service.getTokenMapForMethod(method).getTokenized(path).getSecond().forEach(t ->
                    pathParams.put(t.getKey(), t.getReplacement()));
        }

        ReadonlyMishimaHTTPConfig readonlyConfig = new ReadonlyMishimaHTTPConfig(this.httpConfig);
        HTTPRequest request;
        if (body.length() > 0) {
            request = new HTTPRequest(method, path, httpVersion, queryParameters, pathParams, requestHeaders,
                    body.trim(), readonlyConfig);
        } else {
            request = new HTTPRequest(method, path, httpVersion, queryParameters, pathParams, requestHeaders,
                    readonlyConfig);
        }

        handleConnection(request, connection);
    }

    @ExceptionHandler(HTTPProtocolExceptionHandler.class)
    public void handleConnection(HTTPRequest request, Connection connection) {
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

    public void respond(HTTPRequest request, HTTPResponse response, Connection connection) {
        if (response == null) {
            throw new NullPointerException("Unable to serialize response for request: " + request);
        }

        if (response.getMIMEType() != null) {
            response.getHeaders().addHeader(HTTPHeaders.CONTENT_TYPE, response.getMIMEType().getSerialized());
        }

        connection.write("HTTP/1.1 " + response.getCode() + " " + response.getStatus().name());

        response.getHeaders().forEach((header, value) -> connection.write(header + ": " + value));

        connection.write(""); // needs blank line between headers & content

        if (response.getBody().length() > 0) {
            connection.write(response.getBody());
        }

        try {
            connection.close();
        } catch (IOException e) {
            throw Exceptions.rethrow(e);
        }
    }
}
