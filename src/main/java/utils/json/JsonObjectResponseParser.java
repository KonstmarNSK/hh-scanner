package utils.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import utils.dataobjects.vacancy.Vacancy;

import java.io.InputStream;
import java.net.http.HttpResponse;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JsonObjectResponseParser {
    public static <T> T parseObject(HttpResponse<InputStream> response, Class<T> objectType) {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = null;
        ObjectMapper mapper = null;

        try {
            parser = factory.createParser(response.body());
            mapper = new ObjectMapper(factory);

            return mapper.readValue(parser, objectType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObject(HttpResponse<InputStream> response, TypeReference<T> typeReference) {
        JsonFactory factory = new JsonFactory();
        JsonParser parser = null;
        ObjectMapper mapper = null;

        try {
            parser = factory.createParser(response.body());
            mapper = new ObjectMapper(factory);

            return mapper.readValue(parser, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Stream<T> parseArray(String arrayFieldName, InputStream rawStream, Class<T> tClass) {
        JsonFactory factory = new JsonFactory();

        JsonParser parser = null;
        ObjectMapper mapper = null;

        try {
            parser = factory.createParser(rawStream);
            mapper = new ObjectMapper(factory);

            JsonToken nextToken = null;

            while (nextToken != JsonToken.FIELD_NAME
                    || !arrayFieldName.equals(parser.getCurrentName())
                    || !(parser.nextToken() == JsonToken.START_ARRAY)) {

                nextToken = parser.nextToken();
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        var objIter = new JsonListIter<T>(parser, mapper, tClass);
        var spliterator = Spliterators.spliteratorUnknownSize(objIter, Spliterator.NONNULL);

        return StreamSupport.stream(spliterator, false);
    }
}
