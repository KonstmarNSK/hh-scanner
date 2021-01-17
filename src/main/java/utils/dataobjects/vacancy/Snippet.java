package utils.dataobjects.vacancy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Snippet {
    public final String requirement;
    public final String responsibility;

    public Snippet() {
        this.requirement = "no-requirements";
        this.responsibility = "no-responsibilities";
    }
}
