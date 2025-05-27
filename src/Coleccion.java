public class Coleccion {
    private int id;
    private String name;
    private String poster_path;
    private String backdrop_path;

    public Coleccion(int id, String name, String poster_path, String backdrop_path) {
        this.id = id;
        this.name = name;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
    }

    @Override
    public String toString() {
        return "Colección: " + name + " (id=" + id + ")";
    }
}