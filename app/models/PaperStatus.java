package models;

import java.util.List;
import java.util.Arrays;

public enum PaperStatus {
    DRAFT("Rascunho"),
    SUBMITTED("Submetido"),
    INEVALUATION("Em Avaliação"),
    APROVED("Aprovado"),
    APROVEDBUT("Aprovado com ressalvas"),
    FAILED("Reprovado"),
    PRESENTED("Apresentado"),
    NOPRESENTED("Não Apresentado");

    public String name;

    private PaperStatus(String description) {
        this.name = description;
    }

    public static List<PaperStatus> list() {
        return Arrays.asList(PaperStatus.values());
    }


}
