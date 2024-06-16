package cl.lewickidev.model;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Cliente {
    private int id;
    private String rut;
    private String name;

    public Cliente(int id, String rut, String name) {
        this.id = id;
        this.rut = rut;
        this.name = name;
    }
}
