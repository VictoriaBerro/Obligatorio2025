package entities;

public class Pelicula {
    private String id;
    private String titulo;
    private String idiomaOriginal;
    private String coleccion;  // Puede ser null si no pertenece a ninguna
    private int revenue;
    private String[] generos;

    public Pelicula(String id, String titulo, String idiomaOriginal, String coleccion, int revenue, String[] generos) {
        this.id = id;
        this.titulo = titulo;
        this.idiomaOriginal = idiomaOriginal;
        this.coleccion = coleccion;
        this.revenue = revenue;
        this.generos = generos;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdiomaOriginal() {
        return idiomaOriginal;
    }

    public void setIdiomaOriginal(String idiomaOriginal) {
        this.idiomaOriginal = idiomaOriginal;
    }

    public String getColeccion() {
        return coleccion;
    }

    public void setColeccion(String coleccion) {
        this.coleccion = coleccion;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public String[] getGeneros() {
        return generos;
    }

    public void setGeneros(String[] generos) {
        this.generos = generos;
    }

    @Override
    public String toString() {
        return id + " - " + titulo + " (" + idiomaOriginal + "), Colección: " + coleccion + ", Revenue: " + revenue;
    }
}
