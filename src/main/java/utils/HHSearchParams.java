package utils;

import utils.dataobjects.hhsearchparams.Experience;
import utils.dataobjects.SearchParamsDictionary;
import utils.json.JsonObjectResponseParser;

import java.net.URI;
import java.util.List;

import static utils.Constants.HH_API_URL;

public class HHSearchParams {
    private final SearchParamsDictionary searchParamsDictionary;

    public HHSearchParams(Client client) {
        URI dictionariesUri = URI.create(HH_API_URL + "dictionaries");

        var searchParamsFuture = client.makeRequest(dictionariesUri).
                thenApply(response -> JsonObjectResponseParser.parseJson(response, SearchParamsDictionary.class));

        try {
            searchParamsDictionary = searchParamsFuture.get();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public List<Experience> getAllPossibleExperienceLevels(){
        return searchParamsDictionary.getExperienceLevels();
    }
}
