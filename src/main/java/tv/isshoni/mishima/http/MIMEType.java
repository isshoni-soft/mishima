package tv.isshoni.mishima.http;

public enum MIMEType {
    TEXT("text/plain"),
    JPG("image/jpg"),
    JSON("application/json");

    private final String serialized;

    MIMEType(String serialized) {
        this.serialized = serialized;
    }

    public String getSerialized() {
        return this.serialized;
    }
}
