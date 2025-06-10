package entities;
import TADS.Hashmap.HashMap;

public class Credits {
    private int movieId;
    private HashMap<Integer, Actor> cast;
    private HashMap<Integer, Miembro> crew;

    public Credits(int movieId, HashMap<Integer, Actor> cast, HashMap<Integer, Miembro> crew) {
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

    public HashMap<Integer, Actor> getCast() {
        return cast;
    }

    public void setCast(HashMap<Integer, Actor> cast) {
        this.cast = cast;
    }

    public HashMap<Integer, Miembro> getCrew() {
        return crew;
    }

    public void setCrew(HashMap<Integer, Miembro> crew) {
        this.crew = crew;
    }

    @Override
    public String toString() {
        return "Película ID: " + movieId +
                "\nCAST:\n" + cast.values() +
                "\nCREW:\n" + crew.values();
    }
}
