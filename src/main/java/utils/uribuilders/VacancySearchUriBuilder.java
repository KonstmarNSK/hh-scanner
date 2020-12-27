package utils.uribuilders;

import org.javatuples.Pair;
import utils.Constants;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VacancySearchUriBuilder implements UriBuilder {

    private List<Pair<String, String>> queryParams = new ArrayList<>();
    private StringBuilder rawUri;

    {
        rawUri = new StringBuilder();
        rawUri.append(Constants.HH_API_URL);
        rawUri.append("vacancies?");
    }

    public VacancySearchUriBuilder setText(String query) {
        queryParams.add(Pair.with("text", query));
        return this;
    }


    @Override
    public URI build() {

        String query = queryParams.stream()
                .map(pair -> {
                    String key = URLEncoder.encode(pair.getValue0(), StandardCharsets.UTF_8);
                    String value = URLEncoder.encode(pair.getValue1(), StandardCharsets.UTF_8);
                    return key + "=" + value;
                })
                .collect(Collectors.joining("&"));


        rawUri.append(query);

        return URI.create(rawUri.toString());
    }
}
