import com.google.gson.JsonObject;
import tv.isshoni.mishima.annotation.http.Path;
import tv.isshoni.mishima.annotation.http.method.GET;
import tv.isshoni.mishima.annotation.http.method.POST;
import tv.isshoni.mishima.annotation.http.parameter.Body;
import tv.isshoni.mishima.annotation.http.parameter.Query;
import tv.isshoni.mishima.protocol.http.MIMEType;

@Path("/users/")
public class UsersPath {

    @POST(value = "/create")
    public JsonObject create(@Body(MIMEType.JSON) CreateUserDTO dto) {
        JsonObject object = new JsonObject();
        object.addProperty("status", "successfully created");
        object.addProperty("user", dto.getUsername());

        return object;
    }

    @GET("/{userId}")
    public String getUser(@tv.isshoni.mishima.annotation.http.parameter.Path("userId") String userId) {
        return "User: " + userId;
    }

    @GET("/{userId}/verify")
    public String verifyUser(@tv.isshoni.mishima.annotation.http.parameter.Path("userId") String userId,
                             @Query(value = "token", optional = true) String token) {
        return "Verified user: " + userId + " with token: " + token;
    }
}
