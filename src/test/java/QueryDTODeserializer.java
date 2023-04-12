import tv.isshoni.mishima.annotation.http.Serialization;
import tv.isshoni.mishima.protocol.http.IHTTPDeserializer;

@Serialization(QueryDTO.class)
public class QueryDTODeserializer implements IHTTPDeserializer<QueryDTO> {

    @Override
    public QueryDTO deserialize(String data) {
        if (!data.startsWith("s:")) {
            return null;
        }

        return new QueryDTO(data.substring(data.indexOf(":") + 1));
    }
}
