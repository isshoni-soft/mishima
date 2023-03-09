package tv.isshoni.mishima.http;

import tv.isshoni.mishima.annotation.exception.StatusCode;
import tv.isshoni.mishima.exception.HTTPProtocolException;
import tv.isshoni.mishima.exception.parameter.MissingRequiredParameterException;
import tv.isshoni.mishima.http.protocol.IProtocol;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.exception.Handler;
import tv.isshoni.winry.api.exception.IExceptionHandler;
import tv.isshoni.winry.api.service.ObjectFactory;

@Handler(HTTPProtocolException.class)
public class HTTPProtocolExceptionHandler implements IExceptionHandler<HTTPProtocolException> {

    @Inject private ObjectFactory objectFactory;

    @Override
    public void handle(HTTPProtocolException exception) {
        Throwable e = exception.getCause();
        HTTPRequest request = exception.getRequest();
        HTTPConnection connection = exception.getConnection();
        IProtocol protocol = exception.getProtocol();

        HTTPResponse response = null;
        if (e instanceof MissingRequiredParameterException cast) {
            response = new HTTPResponse(getStatusFromException(cast), MIMEType.TEXT,
                    this.objectFactory.construct(HTTPHeaders.class),
                    "Missing required parameter: " + cast.getParameter());
        }

        if (response != null) {
            protocol.respond(request, response, connection);
        }
    }

    private HTTPStatus getStatusFromException(Throwable throwable) {
        if (!throwable.getClass().isAnnotationPresent(StatusCode.class)) {
            throw new NullPointerException("Cannot find @StatusCode on " + throwable.getClass());
        }

        return throwable.getClass().getAnnotation(StatusCode.class).value();
    }
}
