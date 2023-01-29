package tv.isshoni.mishima.http;

@FunctionalInterface
public interface IHTTPSerializer<O> {

    String serialize(O object);
}
