package models;

import java.util.List;
import java.util.Arrays;

public enum TrackType {
	PI("Projeto Integrador"),
	TCC("Trabalho de Conclusão de Curso"),
	SOFTWARE("Software"),
	SHORTPAPER("Artigos Resumidos"),
	FULLPAPER("Artigos Completos"),
    WORKSHOP("Oficinas"),
    SHORTCOURSE("Minicursos");

    public String name;

    private TrackType(String description) {
        this.name = description;
    }

    public static List<TrackType> list() {
        return Arrays.asList(TrackType.values());
    }
}
