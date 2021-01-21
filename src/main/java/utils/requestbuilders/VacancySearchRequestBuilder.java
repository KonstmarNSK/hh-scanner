package utils.requestbuilders;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.javatuples.Pair;
import utils.Constants;
import utils.HHRequest;
import utils.dataobjects.hhsearchparams.Experience;
import utils.dataobjects.hhsearchparams.area.Area;
import utils.dataobjects.vacancy.Vacancy;
import utils.json.JsonListIter;
import utils.json.JsonObjectResponseParser;

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

    public VacancySearchRequestBuilder inArea(Area area){
        queryParams.add(Pair.with("area", String.valueOf(area.id)));
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

        return new HHRequest<>(
                uri,
                response -> JsonObjectResponseParser.parseArray("items", response.body(), Vacancy.class));
    }
}
