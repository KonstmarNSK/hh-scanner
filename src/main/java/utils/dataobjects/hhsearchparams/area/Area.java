package utils.dataobjects.hhsearchparams.area;

import java.util.Objects;

public class Area {
    public final String name;
    public final Long id;

    protected Area(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Area area = (Area) o;
        return name.equals(area.name) &&
                id.equals(area.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }
}
