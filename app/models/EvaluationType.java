package models;

import java.util.List;
import java.util.Arrays;

public enum EvaluationType {
    SUBMISSION("Submissão"),
    PRESENTATION("Apresentação");

    public String name;

    private EvaluationType(String description) {
        this.name = description;
    }

    public static List<EvaluationType> list() {
        return Arrays.asList(EvaluationType.values());
    }

    @Override
    public String toString() {
        return name;
    }
}
