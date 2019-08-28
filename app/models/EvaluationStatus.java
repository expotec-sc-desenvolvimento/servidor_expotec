package models;

import java.util.List;
import java.util.Arrays;

public enum EvaluationStatus {
	WAITING("Pendente"),
    STARTED("Iniciada"),
    LATE("Atrasada"),
    FINISHED("Concluída"),
    CANCELED("Cancelada");
    
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
