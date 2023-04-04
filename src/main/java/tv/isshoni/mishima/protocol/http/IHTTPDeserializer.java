package tv.isshoni.mishima.protocol.http;

@FunctionalInterface
public interface IHTTPDeserializer<O> {
    O deserialize(String data);
}
