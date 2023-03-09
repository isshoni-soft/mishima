package tv.isshoni.mishima.exception.parameter;

import tv.isshoni.mishima.annotation.exception.StatusCode;
import tv.isshoni.mishima.http.HTTPStatus;

@StatusCode(HTTPStatus.BAD_REQUEST)
public class MissingRequiredParameterException extends RuntimeException {

    private final String parameter;

    public MissingRequiredParameterException(String parameter) {
        super("missing required query parameter: " + parameter);

        this.parameter = parameter;
    }

    public String getParameter() {
        return this.parameter;
    }
}
