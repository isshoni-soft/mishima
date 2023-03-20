package tv.isshoni.mishima.http;

@FunctionalInterface
public interface IHTTPDeserializer<O> {
    O deserialize(String data);
}
