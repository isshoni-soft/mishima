package tv.isshoni.mishima.exception.parameter;

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
