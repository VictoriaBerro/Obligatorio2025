package entities;

import TADS.list.linked.MyLinkedListImpl;

public class Director {
    String name;
    MyLinkedListImpl<Pelicula> peliculas;


    public Director(String name) {
        this.name = name;
        this.peliculas = new MyLinkedListImpl<Pelicula>();
    }
}
