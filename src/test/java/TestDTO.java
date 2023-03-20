import com.google.gson.annotations.SerializedName;

public class TestDTO {

    @SerializedName("the_value")
    private String value;

    public TestDTO(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
