package entities;

public enum Genero {
    MUSIC(10402, "Music"),
    ROMANCE(10749, "Romance"),
    COMEDY(35, "Comedy"),
    FANTASY(14, "Fantasy"),
    DOCUMENTARY(99, "Documentary"),
    HORROR(27, "Horror"),
    WESTERN(37, "Western"),
    TV_MOVIE(10770, "TV Movie"),
    MYSTERY(9648, "Mystery"),
    FAMILY(10751, "Family"),
    ADVENTURE(12, "Adventure"),
    CRIME(80, "Crime"),
    FOREIGN(10769, "Foreign"),
    HISTORY(36, "History"),
    ACTION(28, "Action"),
    SCIENCE_FICTION(878, "Science Fiction"),
    WAR(10752, "War"),
    ANIMATION(16, "Animation"),
    DRAMA(18, "Drama"),
    THRILLER(53, "Thriller"),
    ;

    private final int id;
    private final String name;

    Genero(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Genero fromId(int id) {
        for (Genero genero : values()) {
            if (genero.id == id) {
                return genero;
            }
        }
        throw new IllegalArgumentException("Género con id " + id + " no está definido en el enum Genero.");
    }

    @Override
    public String toString() {
        return name + " (id: " + id + ")";
    }
}

