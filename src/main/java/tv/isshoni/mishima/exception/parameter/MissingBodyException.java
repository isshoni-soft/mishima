package tv.isshoni.mishima.exception.parameter;

import tv.isshoni.mishima.annotation.exception.StatusCode;
import tv.isshoni.mishima.protocol.http.HTTPStatus;

@StatusCode(HTTPStatus.BAD_REQUEST)

public class MissingBodyException extends RuntimeException {

    public MissingBodyException() {
        super("missing request body");
    }
}
