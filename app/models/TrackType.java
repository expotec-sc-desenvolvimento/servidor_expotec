package models;

import java.util.List;
import java.util.Arrays;

public enum TrackType {
    SOFTWARE("Software"),
	RESEARCH("Pesquisa"),
    PRESENTATION("Apresentação");

    public String name;

    private TrackType(String description) {
        this.name = description;
    }

    public static List<TrackType> list() {
        return Arrays.asList(TrackType.values());
    }

    @Override
    public String toString() {
        return name;
    }
}
