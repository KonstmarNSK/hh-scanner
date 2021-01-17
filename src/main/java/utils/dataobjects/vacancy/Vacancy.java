package utils.dataobjects.vacancy;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Vacancy {

    public final Long id;
    public final String name;
    public final Snippet snippet;
    public final Salary salary;
    @JsonAlias("address")
    public final Location location;

    // for jackson
    public Vacancy() {
        id = -1L;
        name = "no-title";
        snippet = null;
        salary = null;
        location = null;
    }


    @Override
    public String toString() {
        return "Vacancy{" +
                "id=" + id +
                ", title='" + name + '\'' +
                ", description='" + snippet + '\'' +
                '}';
    }
}
