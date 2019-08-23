package models;

import java.util.List;
import java.util.Arrays;

public enum ActivityStatus {
    OPENED("Abertas"),
    CLOSED("Encerradas"),
    SOON("Em breve"),
    DRAFT("Rascunho");

    public String name;

    private ActivityStatus(String description) {
        this.name = description;
    }

    public static List<ActivityStatus> list() {
        return Arrays.asList(ActivityStatus.values());
    }

    @Override
    public String toString() {
        return name;
    }
}
