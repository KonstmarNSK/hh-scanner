package utils.dataobjects.hhsearchparams.area;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryPart extends Area{

    @JsonAlias("areas")
    private final List<CountryPart> children;


    private CountryPart() {
        super(null, -1L);

        children = new ArrayList<>();
    }

    public List<CountryPart> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return "Area{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", areas=" + children +
                '}';
    }
}
