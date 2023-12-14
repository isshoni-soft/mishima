import com.google.gson.JsonObject;
import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.araragi.logging.model.level.Level;
import tv.isshoni.mishima.Mishima;
import tv.isshoni.mishima.annotation.http.method.GET;
import tv.isshoni.mishima.annotation.http.method.POST;
import tv.isshoni.mishima.annotation.http.parameter.BodyParam;
import tv.isshoni.mishima.annotation.http.parameter.QueryParam;
import tv.isshoni.mishima.event.config.MishimaHTTPConfigEvent;
import tv.isshoni.mishima.event.config.MishimaServerConfigEvent;
import tv.isshoni.mishima.protocol.http.MIMEType;
import tv.isshoni.winry.api.annotation.Bootstrap;
import tv.isshoni.winry.api.annotation.Event;
import tv.isshoni.winry.api.annotation.Listener;
import tv.isshoni.winry.api.annotation.Loader;
import tv.isshoni.winry.api.annotation.Logger;
import tv.isshoni.winry.api.annotation.parameter.Context;
import tv.isshoni.winry.api.context.IWinryContext;

@Bootstrap(name = "Test Server",
           loader = @Loader(
           manualLoad = { Mishima.class, QueryDTODeserializer.class, UsersPath.class }),
           defaultLevel = Level.DEBUG)
public class TestServer {

    @Logger("TestServer") private AraragiLogger logger;

    @Listener(MishimaServerConfigEvent.class)
    public void configureMishima(@Event MishimaServerConfigEvent event) {
        event.http().useTLS("testkey.jks", "password").port(8080);
    }

    @Listener(MishimaHTTPConfigEvent.class)
    public void configureHTTP(@Event MishimaHTTPConfigEvent event) {
        event.corsAllowOrigin("*");
    }

    @GET("/")
    public String index(@QueryParam("v") QueryDTO dto) {
        if (dto == null) {
            return "Could not find user!";
        }

        return "Hello, " + dto.getV();
    }

    @POST("/")
    public String postIndex(@BodyParam(MIMEType.TEXT) String body) {
        return "Echo: " + body;
    }

    @GET("/login")
    public JsonObject login(@BodyParam(MIMEType.TEXT) String user) {
        JsonObject object = new JsonObject();
        object.addProperty("new_user", user);

        return object;
    }

    @GET("/testDTO")
    public TestDTO testDTO(@QueryParam("value") String value) {
        return new TestDTO(value);
    }

    @GET("/{other}/user")
    public String getOtherUser() {
        return "Other user!";
    }

    @GET("/shutdown")
    public String shutdown(@Context IWinryContext context) {
        context.shutdown();

        return "Shutdown!";
    }
}
