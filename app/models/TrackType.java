package models;

import java.util.List;
import java.util.Arrays;

public enum TrackType {
	PI("Projeto Integrador"),
	TCC("Trabalho de Conclusão de Curso"),
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
}
