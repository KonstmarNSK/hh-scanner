package utils.dataobjects.hhsearchparams;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Experience {

    public final String id;
    @JsonAlias("name")
    public final String humanReadableString;

    private Experience(){
        id = "not initialized";
        humanReadableString = "not initialized";
    }
}
