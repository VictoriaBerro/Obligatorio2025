package entities;

import java.util.Arrays;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pelicula pelicula = (Pelicula) o;
        return revenue == pelicula.revenue && Objects.equals(id, pelicula.id) && Objects.equals(titulo, pelicula.titulo) && Objects.equals(idiomaOriginal, pelicula.idiomaOriginal) && Objects.equals(coleccion, pelicula.coleccion) && Objects.deepEquals(generos, pelicula.generos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, idiomaOriginal, coleccion, revenue, Arrays.hashCode(generos));
    }

    public void setColeccion(String coleccion) {
        this.coleccion = coleccion;
    }

    public String getColeccion() {
        return coleccion;
    }

    public class PeliculaConEvaluaciones implements Comparable<PeliculaConEvaluaciones> {
        private Pelicula pelicula;
        private int cantidadEvaluaciones;

        public PeliculaConEvaluaciones(Pelicula pelicula, int cantidadEvaluaciones) {
            this.pelicula = pelicula;
            this.cantidadEvaluaciones = cantidadEvaluaciones;
        }

        public Pelicula getPelicula() {
            return pelicula;
        }

        public int getCantidadEvaluaciones() {
            return cantidadEvaluaciones;
        }

        @Override
        public int compareTo(PeliculaConEvaluaciones otra) {
            return Integer.compare(this.cantidadEvaluaciones, otra.cantidadEvaluaciones); // Para usar min-heap
        }
    }

}
