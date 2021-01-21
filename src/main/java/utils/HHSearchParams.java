package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import utils.dataobjects.hhsearchparams.Experience;
import utils.dataobjects.SearchParamsDictionary;
import utils.dataobjects.hhsearchparams.area.Country;
import utils.json.JsonObjectResponseParser;

import java.net.URI;
import java.util.List;

import static utils.Constants.HH_API_URL;

public class HHSearchParams {
    private final SearchParamsDictionary searchParamsDictionary;
    private final List<Country> countries;

    public HHSearchParams(Client client) {
        URI dictionariesUri = URI.create(HH_API_URL + "dictionaries");

        var searchParamsFuture = client.makeRequest(dictionariesUri).
                thenApply(response -> JsonObjectResponseParser.parseObject(response, SearchParamsDictionary.class));

        try {
            searchParamsDictionary = searchParamsFuture.get();
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        URI countriesUri = URI.create(HH_API_URL + "areas/countries");

        var countriesFuture = client.makeRequest(countriesUri).
                thenApply(response -> JsonObjectResponseParser.parseObject(response, new TypeReference<List<Country>>() {}));

        try {
            countries = countriesFuture.get();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public List<Experience> getAllPossibleExperienceLevels(){
        return searchParamsDictionary.getExperienceLevels();
    }

    public List<Country> getCountries(){
        return countries;
    }
}
