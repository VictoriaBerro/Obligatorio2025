package entities;

import TADS.Hashmap.HashMap;

public class Creditos {
    private int movieId;                           // ID de la película
    private HashMap<Integer, Cast> cast;           // cast: clave = id del actor
    private HashMap<Integer, Crew> crew;           // crew: clave = id del miembro

    public Creditos(int movieId, HashMap<Integer, Cast> cast, HashMap<Integer, Crew> crew) {
        this.movieId = movieId;
        this.cast = cast;
        this.crew = crew;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public HashMap<Integer, Cast> getCast() {
        return cast;
    }

    public void setCast(HashMap<Integer, Cast> cast) {
        this.cast = cast;
    }

    public HashMap<Integer, Crew> getCrew() {
        return crew;
    }

    public void setCrew(HashMap<Integer, Crew> crew) {
        this.crew = crew;
    }
}
