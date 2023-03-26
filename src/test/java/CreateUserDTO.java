import com.google.gson.annotations.SerializedName;

public class CreateUserDTO {

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.username;
    }
}
