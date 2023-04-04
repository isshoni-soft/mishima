package tv.isshoni.mishima.protocol.http;

@FunctionalInterface
public interface IHTTPSerializer<O> {

    String serialize(O object);
}
