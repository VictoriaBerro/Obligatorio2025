package entities;

public class Pelicula {
    private String idPelicula;
    private String tituloPelicula;
    private String idiomaOriginal;
    private int totalCalificaciones;
    private String[][] generoPelicula;
    private Coleccion perteneceSaga;
    private double presupuesto;
    private String link;
    private Director director;

    public Pelicula(String idPelicula, String tituloPelicula, String idiomaOriginal, int totalCalificaciones, String[][] generoPelicula, Coleccion perteneceSaga, double presupuesto, String link, Director director) {
        this.idPelicula = idPelicula;
        this.tituloPelicula = tituloPelicula;
        this.idiomaOriginal = idiomaOriginal;
        this.totalCalificaciones = totalCalificaciones;
        this.generoPelicula = generoPelicula;
        this.perteneceSaga = perteneceSaga;
        this.presupuesto = presupuesto;
        this.link = link;
        this.director = director;
    }


    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public String getIdPelicula() {
        return idPelicula;
    }

    public void setIdPelicula(String idPelicula) {
        this.idPelicula = idPelicula;
    }

    public String getTituloPelicula() {
        return tituloPelicula;
    }

    public void setTituloPelicula(String tituloPelicula) {
        this.tituloPelicula = tituloPelicula;
    }

    public String getIdiomaOriginal() {
        return idiomaOriginal;
    }

    public void setIdiomaOriginal(String idiomaOriginal) {
        this.idiomaOriginal = idiomaOriginal;
    }

    public int getTotalCalificaciones() {
        return totalCalificaciones;
    }

    public void setTotalCalificaciones(int totalCalificaciones) {
        this.totalCalificaciones = totalCalificaciones;
    }

    public String[][] getGeneroPelicula() {
        return generoPelicula;
    }

    public void setGeneroPelicula(String[][] generoPelicula) {
        this.generoPelicula = generoPelicula;
    }

    public Coleccion getPerteneceSaga() {
        return perteneceSaga;
    }

    public void setPerteneceSaga(Coleccion perteneceSaga) {
        this.perteneceSaga = perteneceSaga;
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
