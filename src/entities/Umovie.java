package entities;

import Interfaz.UmovieImpl;
import LinkedList.LinkedList;

import java.io.*;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
import org.json.JSONArray;



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
    public void cargarPeliculas(String rutaCsv) throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream("movies_metadata.csv");
        if (input == null) {
            System.out.println("❌ No se encontró el archivo movies_metadata.csv");
            return;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {//leo linea por linea
            String linea;
            br.readLine(); // Saltar la primera fila, no me interesan los nombres de las columnas

            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                //divido en columnas

                if (campos.length > 17) {
                    try {
                        // ID
                        String id = campos[5].trim();

                        // Título
                        String titulo = campos[17].trim();

                        // Idioma original
                        String idioma = campos[7].trim();
                        //el .trim() elimina espacios en blanco

                        // Revenue
                        int revenue = 0;
                        try {
                            revenue = Integer.parseInt(campos[13].trim());
                        } catch (NumberFormatException e) {
                            // revenue = 0 por defecto
                        }

                        // belongs_to_collection (parsear JSON con name)
                        String coleccionTexto = campos[1].trim();
                        String coleccion = null;
                        if (!coleccionTexto.isEmpty()) {
                            coleccionTexto = coleccionTexto.replace("'", "\"");
                            try {
                                JSONObject colJson = new JSONObject(coleccionTexto);
                                coleccion = colJson.getString("name");
                            } catch (Exception e) {
                                // dejar coleccion como null
                            }
                        }

                        // genres (JSONArray de objetos con "name")
                        String generosTexto = campos[3].trim();
                        String[] generos = new String[0];
                        if (!generosTexto.isEmpty()) {
                            generosTexto = generosTexto.replace("'", "\"");
                            try {
                                JSONArray generosJson = new JSONArray(generosTexto);
                                generos = new String[generosJson.length()];
                                for (int i = 0; i < generosJson.length(); i++) {
                                    generos[i] = generosJson.getJSONObject(i).getString("name");
                                }
                            } catch (Exception e) {
                                // dejar generos vacío
                            }
                        }

                        // Crear objeto Pelicula
                        Pelicula pelicula = new Pelicula(id, titulo, idioma, coleccion, revenue, generos);
                        peliculas.add(pelicula);

                    } catch (Exception e) {
                        // Si esta línea falla, seguir con la siguiente
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer movies_metadata.csv");
            e.printStackTrace();
            ;
        }
    }

    @Override
    public void cargarCalificaciones(String rutaCsv) {
        InputStream input = getClass().getClassLoader().getResourceAsStream(rutaCsv);
        if (input == null) {
            System.out.println("❌ No se encontró el archivo " + rutaCsv);
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            String linea;
            br.readLine(); // Saltar cabecera


            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(",");
                try {
                    int userId = Integer.parseInt(campos[0].trim());
                    int movieId = Integer.parseInt(campos[1].trim());
                    double rating = Double.parseDouble(campos[2].trim());
                    long timestamp = Long.parseLong(campos[3].trim());

                    Evaluacion evaluacion = new Evaluacion(userId, movieId, rating, timestamp);
                    evaluaciones.add(evaluacion);
                } catch (Exception e) {
                    // Si la línea está mal formada, se ignora
                }
            }

        } catch (IOException e) {
            System.out.println("❌ Error al leer el archivo " + rutaCsv);
            e.printStackTrace();
        }
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
