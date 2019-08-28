package models;

import java.util.List;
import java.util.Arrays;

public enum CalculationType {
	ARITHMETIC_AVG("Média Aritmética"),
	WEIGHTED_AVG("Média Ponderada");

    public String name;

    private CalculationType(String description) {
        this.name = description;
    }

    public static List<CalculationType> list() {
        return Arrays.asList(CalculationType.values());
    }

    @Override
    public String toString() {
        return name;
    }
}
