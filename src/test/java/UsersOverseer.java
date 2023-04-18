import com.google.gson.JsonObject;
import tv.isshoni.mishima.annotation.http.Overseer;
import tv.isshoni.mishima.annotation.http.method.GET;
import tv.isshoni.mishima.annotation.http.method.POST;
import tv.isshoni.mishima.annotation.http.parameter.Body;
import tv.isshoni.mishima.annotation.http.parameter.Path;
import tv.isshoni.mishima.annotation.http.parameter.Query;
import tv.isshoni.mishima.protocol.http.MIMEType;

@Overseer("/users/")
public class UsersOverseer {

    @POST(value = "/create")
    public JsonObject create(@Body(MIMEType.JSON) CreateUserDTO dto) {
        JsonObject object = new JsonObject();
        object.addProperty("status", "successfully created");
        object.addProperty("user", dto.getUsername());

        return object;
    }

    @GET("/{userId}")
    public String getUser(@Path("userId") String userId) {
        return "User: " + userId;
    }

    @GET("/{userId}/verify")
    public String verifyUser(@Path("userId") String userId,
                             @Query(value = "token", optional = true) String token) {
        return "Verified user: " + userId + " with token: " + token;
    }
}
