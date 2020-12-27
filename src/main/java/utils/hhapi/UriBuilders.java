package utils.hhapi;

import org.javatuples.Pair;
import utils.Constants;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


class UriBuilders {

    public static VacancySearchUriBuilder findJob(){
        return new VacancySearchUriBuilder();
    }
}

interface UriBuilder{
    public URI build();
}

class VacancySearchUriBuilder implements UriBuilder{

    private List<Pair<String, String>> queryParams = new ArrayList<>();
    private StringBuilder rawUri;

    {
        rawUri = new StringBuilder();
        rawUri.append(Constants.HH_API_URL);
        rawUri.append("vacancies?");
    }

    public VacancySearchUriBuilder setText(String query){
        queryParams.add(Pair.with("text", query));
        return this;
    }


    @Override
    public URI build() {
        rawUri.append(queryParams.stream()
                .map(pair -> pair.getValue0() + "=" + pair.getValue1())
                .collect(Collectors.joining("&")));

        return URI.create(rawUri.toString());
    }
}