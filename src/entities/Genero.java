package entities;

public enum Genero {
    COMEDY(35, "Comedy"),
    ANIMATION(16, "Animation"),
// FALTA PONERLOS BIEN
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

