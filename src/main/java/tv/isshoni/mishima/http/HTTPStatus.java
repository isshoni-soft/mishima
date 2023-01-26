package tv.isshoni.mishima.http;

public enum HTTPStatus {
    OK(200),
    BAD_REQUEST(400),
    NOT_FOUND(404);

    private final int code;

    HTTPStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
