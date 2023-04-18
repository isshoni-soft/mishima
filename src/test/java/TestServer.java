import com.google.gson.JsonObject;
import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.araragi.logging.model.level.Level;
import tv.isshoni.mishima.Mishima;
import tv.isshoni.mishima.annotation.http.method.GET;
import tv.isshoni.mishima.annotation.http.method.POST;
import tv.isshoni.mishima.annotation.http.parameter.Body;
import tv.isshoni.mishima.annotation.http.parameter.Query;
import tv.isshoni.mishima.event.config.MishimaHTTPConfigEvent;
import tv.isshoni.mishima.event.config.MishimaServerConfigEvent;
import tv.isshoni.mishima.protocol.http.MIMEType;
import tv.isshoni.winry.api.annotation.Bootstrap;
import tv.isshoni.winry.api.annotation.Event;
import tv.isshoni.winry.api.annotation.Listener;
import tv.isshoni.winry.api.annotation.Loader;
import tv.isshoni.winry.api.annotation.Logger;

@Bootstrap(name = "Test Server",
           loader = @Loader(
           manualLoad = { Mishima.class, QueryDTODeserializer.class, UsersOverseer.class }),
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
    public String index(@Query("v") QueryDTO dto) {
        if (dto == null) {
            return "Could not find user!";
        }

        return "Hello, " + dto.getV();
    }

    @POST("/")
    public String postIndex(@Body(MIMEType.TEXT) String body) {
        return "Echo: " + body;
    }

    @GET("/login")
    public JsonObject login(@Body(MIMEType.TEXT) String user) {
        JsonObject object = new JsonObject();
        object.addProperty("new_user", user);

        return object;
    }

    @GET("/testDTO")
    public TestDTO testDTO(@Query("value") String value) {
        return new TestDTO(value);
    }

    @GET("/{other}/user")
    public String getOtherUser() {
        return "Other user!";
    }

    // TODO: BROKEN, DOESN'T WORK, IDK WHY
    // TODO: this functionality is preventing unit testing, once shutdown works unit testing will be possible
//    @GET("/shutdown")
//    public String shutdown(@Inject IWinryContext context) {
//        context.shutdown();
//
//        return "Shutdown!";
//    }
}
