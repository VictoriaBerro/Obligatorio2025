public class Genero {
    private int id;
    private String name;

    public Genero(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " (id: " + id + ")";
    }


}
