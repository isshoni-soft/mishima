import com.google.gson.JsonObject;
import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.araragi.logging.model.level.Level;
import tv.isshoni.mishima.Mishima;
import tv.isshoni.mishima.annotation.http.method.GET;
import tv.isshoni.mishima.annotation.http.method.POST;
import tv.isshoni.mishima.annotation.http.parameter.Body;
import tv.isshoni.mishima.annotation.http.parameter.Path;
import tv.isshoni.mishima.annotation.http.parameter.Query;
import tv.isshoni.mishima.event.config.MishimaHTTPConfigEvent;
import tv.isshoni.mishima.event.config.MishimaServerConfigEvent;
import tv.isshoni.mishima.protocol.http.MIMEType;
import tv.isshoni.mishima.protocol.HTTP1;
import tv.isshoni.mishima.service.ProtocolService;
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

    @Listener(MishimaServerConfigEvent.class)
    public void configureMishima(@Event MishimaServerConfigEvent event) {
        event.http().useTLS("testkey.jks", "password").port(8080);
    }

    @Listener(MishimaHTTPConfigEvent.class)
    public void configureHTTP(@Event MishimaHTTPConfigEvent event, @Inject ProtocolService protocolService) {
        event.corsAllowOrigin("*");
        protocolService.useProtocol(HTTP1.class);
    }

    @GET("/")
    public String index() {
        return "Hello, World!";
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

    @POST(value = "/users/create")
    public JsonObject create(@Body(MIMEType.JSON) CreateUserDTO dto) {
        JsonObject object = new JsonObject();
        object.addProperty("status", "successfully created");
        object.addProperty("user", dto.getUsername());

        return object;
    }

    @GET("/users/{userId}")
    public String getUser(@Path("userId") String userId) {
        return "User: " + userId;
    }

    @GET("/users/{userId}/verify")
    public String verifyUser(@Path("userId") String userId,
                             @Query(value = "token", optional = true) String token) {
        return "Verified user: " + userId + " with token: " + token;
    }

    @GET("/testDTO")
    public TestDTO testDTO(@Query("value") String value) {
        return new TestDTO(value);
    }

    @GET("/{other}/user")
    public String getOtherUser() {
        return "Other user!";
    }
}
