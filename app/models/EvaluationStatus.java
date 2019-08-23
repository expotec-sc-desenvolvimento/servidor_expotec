package models;

import java.util.List;
import java.util.Arrays;

public enum EvaluationStatus {
    OPENED("Abertas"),
    CLOSED("Encerradas"),
    SOON("Em breve"),
    DRAFT("Rascunho");

    public String name;

    private EvaluationStatus(String description) {
        this.name = description;
    }

    public static List<EvaluationStatus> list() {
        return Arrays.asList(EvaluationStatus.values());
    }

    @Override
    public String toString() {
        return name;
    }
}
