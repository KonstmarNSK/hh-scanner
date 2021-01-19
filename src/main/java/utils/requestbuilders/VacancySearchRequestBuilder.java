package utils.requestbuilders;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.javatuples.Pair;
import utils.Constants;
import utils.HHRequest;
import utils.dataobjects.hhsearchparams.Experience;
import utils.dataobjects.vacancy.Vacancy;
import utils.json.JsonListIter;

import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class VacancySearchRequestBuilder {

    private List<Pair<String, String>> queryParams = new ArrayList<>();
    private StringBuilder rawUri;

    {
        rawUri = new StringBuilder();
        rawUri.append(Constants.HH_API_URL);
        rawUri.append("vacancies?");
    }

    public VacancySearchRequestBuilder findTextOccurrences(String query) {
        queryParams.add(Pair.with("text", query));
        return this;
    }

    public VacancySearchRequestBuilder withExperience(Experience experience){
        queryParams.add(Pair.with("experience", experience.id));
        return this;
    }


    public HHRequest<Stream<Vacancy>> build() {

        String query = queryParams.stream()
                .map(pair -> {
                    String key = URLEncoder.encode(pair.getValue0(), StandardCharsets.UTF_8);
                    String value = URLEncoder.encode(pair.getValue1(), StandardCharsets.UTF_8);
                    return key + "=" + value;
                })
                .collect(Collectors.joining("&"));


        rawUri.append(query);

        URI uri = URI.create(rawUri.toString());

        return new HHRequest<>(uri, VacancySearchRequestBuilder::mapRawResponseIntoVacancies);
    }

    private static Stream<Vacancy> mapRawResponseIntoVacancies(HttpResponse<InputStream> rawResp) {
        JsonFactory factory = new JsonFactory();

        JsonParser parser = null;
        ObjectMapper mapper = null;

        try {
            parser = factory.createParser(rawResp.body());
            mapper = new ObjectMapper(factory);

            JsonToken nextToken = null;
            boolean foundVacanciesList = false;

            while (!foundVacanciesList) {
                while (nextToken != JsonToken.FIELD_NAME) {
                    nextToken = parser.nextToken();
                }

                // vacancies list named "items"
                if (parser.getCurrentName().equals("items")
                        && parser.nextToken() == JsonToken.START_ARRAY) {
                    foundVacanciesList = true;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        var objIter = new JsonListIter<Vacancy>(parser, mapper, Vacancy.class);
        var spliterator = Spliterators.spliteratorUnknownSize(objIter, Spliterator.NONNULL);

        return StreamSupport.stream(spliterator, false);
    }
}
