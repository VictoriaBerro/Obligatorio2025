package entities;

import Interfaz.UmovieImpl;


public class Umovie implements UmovieImpl {
    private ListaEnlazada<Pelicula> peliculas = new ListaEnlazada<>();
    private ListaEnlazada<Evaluacion> evaluaciones = new ListaEnlazada<>();
    private ListaEnlazada<Miembro> Miembros = new ListaEnlazada<>();

    @Override
    public void cargarPeliculas(String rutaCsv) {

    }

    @Override
    public void cargarCalificaciones(String rutaCsv) {

    }

    @Override
    public void cargarParticipantes(String rutaCsv) {

    }

    @Override
    public void consulta1() {//devuelve id, titulo, total evaluaciones, idioma original de las top 5 con
        //mas evalucaiones por idioma

    }

    @Override
    public void consulta2() {//por idioma, devolver el top 10 peliculas con mejor calificacion media
        //devuelvo id, titulo y cal media

    }

    @Override
    public void consulta3() {//top 5 sagas que mas generaron
        //devuelvo id, titulo saga, can peliculas, ids peliculas, ingresos generados

    }

    @Override
    public void consulta4() {// viendo todas las peliculas de un director, y devolver el top 10
        //directores con mejor calificacion media con al menos una pelicula y 100 clasificaciones
        //devuelvo nombre,cant peliculas y calificacion media

    }

    @Override
    public void consulta5() {//devuelvo actor con mas calificaciones por mes del anio
        //tomo las calificaciones en las q el actor participa como cal del actor
        //devuelvo mes, nombre del actor, cant peliculas vistas en el mes, cant calificaciones

    }

    @Override
    public void consulta6() {//devuelvo el top 10 generos mas evaluados con su respectivo id usuario con mas evaluaicones
        //y la cant de evaluaciones del mismo

    }
}
