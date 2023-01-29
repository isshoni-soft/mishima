import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.araragi.logging.model.level.Level;
import tv.isshoni.mishima.Mishima;
import tv.isshoni.mishima.annotation.http.method.GET;
import tv.isshoni.mishima.event.MishimaConfigEvent;
import tv.isshoni.mishima.http.HTTPResponse;
import tv.isshoni.mishima.http.HTTPService;
import tv.isshoni.mishima.http.SerializedHTTPResponse;
import tv.isshoni.winry.api.annotation.Bootstrap;
import tv.isshoni.winry.api.annotation.Event;
import tv.isshoni.winry.api.annotation.Inject;
import tv.isshoni.winry.api.annotation.Listener;
import tv.isshoni.winry.api.annotation.Loader;
import tv.isshoni.winry.api.annotation.Logger;

@Bootstrap(name = "Test Server",
           loader = @Loader(
           manualLoad = { Mishima.class }),
           defaultLevel = Level.DEBUG)
public class TestServer {

    @Logger("TestServer") private AraragiLogger logger;

    @Listener(MishimaConfigEvent.class)
    public void configureMishima(@Event MishimaConfigEvent event) {
        event.port(8080);
    }

    @GET("/")
    public String index() {
        return "Hello, World!";
    }

    @GET("/test")
    public HTTPResponse test(@Inject HTTPService service) {
        return new SerializedHTTPResponse(service, )
    }
}
