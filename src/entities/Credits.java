package entities;

import java.util.List;

public class Credits {
    private int movieId;
    private List<Actor> cast;
    private List<Miembro> crew;

    public Credits(int movieId, List<Actor> cast, List<Miembro> crew) {
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

    public List<Actor> getCast() {
        return cast;
    }

    public void setCast(List<Actor> cast) {
        this.cast = cast;
    }

    public List<Miembro> getCrew() {
        return crew;
    }

    public void setCrew(List<Miembro> crew) {
        this.crew = crew;
    }

    @Override
    public String toString() {
        return "Película ID: " + movieId + "\nCAST:\n" + cast + "\nCREW:\n" + crew;
    }
}
