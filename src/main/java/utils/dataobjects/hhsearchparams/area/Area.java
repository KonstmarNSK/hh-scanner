package utils.dataobjects.hhsearchparams.area;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Area {
    public final String name;
    public final Long id;

    @JsonAlias("areas")
    private final List<Area> children;


    private Area() {
        this.name = "not initialized";
        this.id = -1L;
        children = new ArrayList<>();
    }

    public List<Area> getChildren() {
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
