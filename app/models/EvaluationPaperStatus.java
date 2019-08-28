package models;

import java.util.List;
import java.util.Arrays;

public enum EvaluationPaperStatus {
	WAITING("Pendente"),
    STARTED("Iniciada"),
    LATE("Atrasada"),
    FINISHED("Concluída"),
    CANCELED("Cancelada");
    
    public String name;

    private EvaluationPaperStatus(String description) {
        this.name = description;
    }

    public static List<EvaluationPaperStatus> list() {
        return Arrays.asList(EvaluationPaperStatus.values());
    }

    @Override
    public String toString() {
        return name;
    }
}
