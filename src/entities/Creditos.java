package entities;

import TADS.Hashmap.HashMap;
import TADS.list.linked.MyLinkedListImpl;

public class Creditos {
    private int movieId;                           // ID de la película
    private MyLinkedListImpl<Miembro> miembros;           // cast: clave = id del actor
    private MyLinkedListImpl<Actor> actores;           // crew: clave = id del miembro

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public MyLinkedListImpl<Miembro> getMiembros() {
        return miembros;
    }

    public void setMiembros(MyLinkedListImpl<Miembro> miembros) {
        this.miembros = miembros;
    }

    public MyLinkedListImpl<Actor> getActores() {
        return actores;
    }

    public void setActores(MyLinkedListImpl<Actor> actores) {
        this.actores = actores;
    }

    public Creditos(int movieId, MyLinkedListImpl<Miembro> miembros, MyLinkedListImpl<Actor> actores) {
        this.movieId = movieId;
        this.miembros = miembros;
        this.actores = actores;
    }



}
