package models;

import java.util.List;
import java.util.Arrays;

public enum Permission {
    ADMIN("Administrador"),
    EXPERT("Especialista"),
    ATTENDANT("Atendente"),
    ATTENDEE("Participante");
    
    public String name;

    private Permission(String name) {
        this.name = name;
    }

    public static List<Permission> list() {
        return Arrays.asList(Permission.values());
    }
}
