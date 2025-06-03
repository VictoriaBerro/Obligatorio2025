package entities;

import LinkedList.LinkedList;

public class Director {
    String name;
    LinkedList<Pelicula> peliculas;


    public Director(String name) {
        this.name = name;
        this.peliculas = new LinkedList<Pelicula>();
    }
}
