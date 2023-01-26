package tv.isshoni.mishima.http;

public interface IHTTPSerializer<O> {

    String serialize(O object);
}
