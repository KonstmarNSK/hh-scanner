package utils.dataobjects.vacancy;

import java.util.ArrayList;
import java.util.List;

public class Location {
    public final String city;
    public final String street;
    public final String building;
    public final String description;
    public final float lat;
    public final float lng;
    public final List<MetroStation> metro_stations;

    public Location() {
        this.city = "city";
        this.street = "street";
        this.building = "building";
        this.description = "description";
        this.lat = -0.1f;
        this.lng = -0.1f;
        this.metro_stations = new ArrayList<>();
    }
}
