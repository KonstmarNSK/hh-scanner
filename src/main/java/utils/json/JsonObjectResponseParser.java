package utils.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.http.HttpResponse;

public class JsonObjectResponseParser {
    public static <T> T parseJson(HttpResponse<InputStream> response, Class<T> objectType){
        JsonFactory factory = new JsonFactory();
        JsonParser parser = null;
        ObjectMapper mapper = null;

        try {
            parser = factory.createParser(response.body());
            mapper = new ObjectMapper(factory);

            return mapper.readValue(parser, objectType);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
