package models;

import java.util.List;
import java.util.Arrays;

public enum TrackStatus {
    OPENED("Aberta"),
    CLOSED("Encerrada"),
    SOON("Em breve"),
    DRAFT("Rascunho");

    public String name;

    private TrackStatus(String description) {
        this.name = description;
    }

    public static List<TrackStatus> list() {
        return Arrays.asList(TrackStatus.values());
    }

    @Override
    public String toString() {
        return name;
    }
}
