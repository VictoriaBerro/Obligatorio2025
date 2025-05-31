package entities;

public class Pelicula {
    private String idPelicula;
    private String tituloPelicula;
    private String idiomaOriginal;
    private int totalCalificaciones;
    private String[][] generoPelicula;
    private Coleccion perteneceSaga;
    private int presupuesto;
    private String link;

    public Pelicula(String idPelicula, String tituloPelicula, String idiomaOriginal, int totalCalificaciones, String[][] generoPelicula, Coleccion perteneceSaga, int presupuesto, String link) {
        this.idPelicula = idPelicula;
        this.tituloPelicula = tituloPelicula;
        this.idiomaOriginal = idiomaOriginal;
        this.totalCalificaciones = totalCalificaciones;
        this.generoPelicula = generoPelicula;
        this.perteneceSaga = perteneceSaga;
        this.presupuesto = presupuesto;
        this.link = link;
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

    public int getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(int presupuesto) {
        this.presupuesto = presupuesto;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
