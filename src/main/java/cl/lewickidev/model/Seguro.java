package cl.lewickidev.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Seguro {

    private int id;
    private String name;

    public Seguro(int id, String name) {
        this.id = id;
        this.name = name;
    }

}