package tv.isshoni.mishima.http;

public enum HTTPStatus {
    OK(200),
    NO_CONTENT(204),
    BAD_REQUEST(400),
    NOT_FOUND(404),
    INTERNAL_SERVER(500);

    private final int code;

    HTTPStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
