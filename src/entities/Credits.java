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

    @Override
    public String toString() {
        return "Película ID: " + movieId + "\nCAST:\n" + cast + "\nCREW:\n" + crew;
    }
}
