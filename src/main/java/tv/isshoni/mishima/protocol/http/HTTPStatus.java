package tv.isshoni.mishima.protocol.http;

public enum HTTPStatus {
    OK(200),
    CREATED(201),
    ACCEPTED(202),
    NO_CONTENT(204),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    NOT_ALLOWED(405),
    NOT_ACCEPTABLE(406),
    INTERNAL_SERVER(500);

    private final int code;

    HTTPStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
