package tv.isshoni.mishima.protocol.http.handler;

import tv.isshoni.araragi.exception.Exceptions;
import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.mishima.annotation.exception.StatusCode;
import tv.isshoni.mishima.exception.HTTPProtocolException;
import tv.isshoni.mishima.exception.parameter.MissingBodyException;
import tv.isshoni.mishima.exception.parameter.MissingRequiredParameterException;
import tv.isshoni.mishima.exception.parameter.NeedsContentTypeException;
import tv.isshoni.mishima.protocol.Connection;
import tv.isshoni.mishima.protocol.http.HTTPHeaders;
import tv.isshoni.mishima.protocol.http.HTTPRequest;
import tv.isshoni.mishima.protocol.http.HTTPResponse;
import tv.isshoni.mishima.protocol.http.HTTPStatus;
import tv.isshoni.mishima.protocol.http.MIMEType;
import tv.isshoni.mishima.protocol.http.IHTTPProtocol;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.Logger;
import tv.isshoni.winry.api.annotation.exception.Handler;
import tv.isshoni.winry.api.exception.IExceptionHandler;
import tv.isshoni.winry.api.service.ObjectFactory;

@Handler(HTTPProtocolException.class)
public class HTTPProtocolExceptionHandler implements IExceptionHandler<HTTPProtocolException> {

    @Logger("HTTP Exception") private AraragiLogger logger;

    @Inject private ObjectFactory objectFactory;

    @Override
    public void handle(HTTPProtocolException exception) {
        Throwable e = exception.getCause();
        HTTPRequest request = exception.getRequest();
        Connection connection = exception.getConnection();
        IHTTPProtocol protocol = exception.getProtocol();
        HTTPResponse response = null;
        HTTPStatus status = getStatusFromException(e);

        if (e instanceof MissingRequiredParameterException cast) {
            response = new HTTPResponse(status, MIMEType.TEXT,
                    this.objectFactory.construct(HTTPHeaders.class),
                    "Missing required parameter: " + cast.getParameter());
        } else if (e instanceof MissingBodyException) {
            response = new HTTPResponse(status, MIMEType.TEXT,
                    this.objectFactory.construct(HTTPHeaders.class),
                    "Missing request body!");
        } else if (e instanceof NeedsContentTypeException cast) {
            response = new HTTPResponse(status, MIMEType.TEXT,
                    this.objectFactory.construct(HTTPHeaders.class),
                    "Request requires Content-Type: " + cast.getMimeType().getSerialized());
        }

        if (response == null) {
            response = new HTTPResponse(getStatusFromException(e), MIMEType.TEXT,
                    this.objectFactory.construct(HTTPHeaders.class), "Internal Server Error");
        }

        this.logger.error("Recovered from error while executing path: " + request.getPath());
        this.logger.error(Exceptions.toString(e));

        protocol.respond(request, response, connection);
    }

    private HTTPStatus getStatusFromException(Throwable throwable) {
        if (!throwable.getClass().isAnnotationPresent(StatusCode.class)) {
            return HTTPStatus.INTERNAL_SERVER;
        }

        return throwable.getClass().getAnnotation(StatusCode.class).value();
    }
}
