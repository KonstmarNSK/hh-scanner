package utils.dataobjects;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import utils.dataobjects.hhsearchparams.Experience;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchParamsDictionary {
    @JsonAlias("experience")
    private List<Experience> experienceLevels;

    public List<Experience> getExperienceLevels() {
        return new ArrayList<>(experienceLevels);
    }

}
