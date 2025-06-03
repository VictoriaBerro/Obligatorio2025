package entities;

import Interfaz.UmovieImpl;
import LinkedList.LinkedList;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class Umovie implements UmovieImpl {
    private LinkedList<Pelicula> peliculas;
    private LinkedList<Evaluacion> evaluaciones;
    private LinkedList<Miembro> miembros;

    public Umovie() {
        this.peliculas = new LinkedList<Pelicula>();
        this.evaluaciones = new LinkedList<Evaluacion>();
        this.miembros = new LinkedList<Miembro>();
    }

    @Override
    public void cargarPeliculas(String rutaCsv) throws FileNotFoundException {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaCsv))) {
            String linea;
            br.readLine(); // saltar cabecera

            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                if (campos.length > 17) {
                    try {
                        String coleccion = campos[1].trim();
                        if (coleccion.isEmpty()) coleccion = null;

                        String generosTexto = campos[3].trim();
                        String[] generos = parsearGeneros(generosTexto);

                        String id = campos[5].trim();
                        String idioma = campos[7].trim();
                        String titulo = campos[17].trim();

                        int revenue = 0;
                        try {
                            revenue = Integer.parseInt(campos[13].trim());
                        } catch (NumberFormatException e) {
                            // revenue queda en 0 si no es numérico
                        }

                        Pelicula pelicula = new Pelicula(id, titulo, idioma, coleccion, revenue, generos);
                        peliculas.add(pelicula);

                    } catch (Exception e) {
                        // Salteo línea problemática
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error leyendo movies_metadata.csv");
            throw new RuntimeException(e);
        }
    }

    private String[] parsearGeneros(String texto) {
        // Ejemplo de entrada: "[{'id': 16, 'name': 'Animation'}, {'id': 35, 'name': 'Comedy'}]"
        texto = texto.replace("[", "").replace("]", "");
        String[] partes = texto.split("},");
        for (int i = 0; i < partes.length; i++) {
            String[] tokens = partes[i].split("'name':");
            if (tokens.length > 1) {
                partes[i] = tokens[1].replace("'", "").replace("}", "").trim();
            } else {
                partes[i] = "Desconocido";
            }
        }
        return partes;
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
