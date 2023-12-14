import com.google.gson.JsonObject;
import tv.isshoni.mishima.annotation.http.Path;
import tv.isshoni.mishima.annotation.http.method.GET;
import tv.isshoni.mishima.annotation.http.method.POST;
import tv.isshoni.mishima.annotation.http.parameter.BodyParam;
import tv.isshoni.mishima.annotation.http.parameter.PathParam;
import tv.isshoni.mishima.annotation.http.parameter.QueryParam;
import tv.isshoni.mishima.protocol.http.MIMEType;

@Path("/users/")
public class UsersPath {

    @POST(value = "/create")
    public JsonObject create(@BodyParam(MIMEType.JSON) CreateUserDTO dto) {
        JsonObject object = new JsonObject();
        object.addProperty("status", "successfully created");
        object.addProperty("user", dto.getUsername());

        return object;
    }

    @GET("/{userId}")
    public String getUser(@PathParam("userId") String userId) {
        return "User: " + userId;
    }

    @GET("/{userId}/verify")
    public String verifyUser(@PathParam("userId") String userId,
                             @QueryParam(value = "token", optional = true) String token) {
        return "Verified user: " + userId + " with token: " + token;
    }
}
