package models;

import java.util.List;
import java.util.Arrays;

public enum EvalStatus {
	WAITING("Pendente"),
    STARTED("Iniciada"),
    LATE("Atrasada"),
    FINISHED("Concluída"),
    CANCELED("Cancelada");
    
    public String name;

    private EvalStatus(String description) {
        this.name = description;
    }

    public static List<EvalStatus> list() {
        return Arrays.asList(EvalStatus.values());
    }

}
