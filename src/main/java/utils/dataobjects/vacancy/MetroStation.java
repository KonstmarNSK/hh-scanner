package utils.dataobjects.vacancy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MetroStation {
    public final float station_id;
    public final String station_name;
    public final int line_id;
    public final String line_name;
    public final float lat;
    public final float lng;

    public MetroStation() {
        this.station_id = -0.1f;
        this.station_name = "no-name";
        this.line_id = -1;
        this.line_name = "line name";
        this.lat = -1;
        this.lng = -1;
    }
}
