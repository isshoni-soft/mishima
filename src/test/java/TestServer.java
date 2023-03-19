import com.google.gson.JsonObject;
import tv.isshoni.araragi.logging.AraragiLogger;
import tv.isshoni.araragi.logging.model.level.Level;
import tv.isshoni.mishima.Mishima;
import tv.isshoni.mishima.annotation.http.method.GET;
import tv.isshoni.mishima.annotation.http.parameter.PathParameter;
import tv.isshoni.mishima.annotation.http.parameter.QueryParameter;
import tv.isshoni.mishima.event.MishimaConfigEvent;
import tv.isshoni.winry.api.annotation.Bootstrap;
import tv.isshoni.winry.api.annotation.Event;
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

    @GET("/login")
    public JsonObject login(@QueryParameter("user") String user) {
        JsonObject object = new JsonObject();
        object.addProperty("new_user", user);

        return object;
    }

    @GET("/users/{userId}")
    public String getUser(@PathParameter("userId") String userId) {
        return "User: " + userId;
    }

    @GET("/users/{userId}/verify")
    public String verifyUser(@PathParameter("userId") String userId,
                             @QueryParameter(value = "token", optional = true) String token) {
        return "Verified user: " + userId + " with token: " + token;
    }

    @GET("/{other}/user")
    public String getOtherUser() {
        return "Other user!";
    }
}
