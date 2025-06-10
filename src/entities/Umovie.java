package entities;

import Interfaz.UmovieImpl;
import LinkedList.LinkedList;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONArray;



public class Umovie implements UmovieImpl {
    private HashMap<Integer,Pelicula> peliculas;
    private Map<Integer, Evaluacion> evaluaciones;
    private Map<Integer, Credits> creditos;


    public Umovie() {
        //this.peliculas = new Map<idPelicula, Pelicula>();
        //this.evaluaciones = new Map<idEvaluacion, Evaluacion>();
        //this.creditos = new Map<idCredito, Miembro>();
    }

    @Override
    public void cargarPeliculas(String rutaCsv) throws IOException {
        HashMap peliculas = new HashMap<Integer, Pelicula>();
        InputStream input = getClass().getClassLoader().getResourceAsStream("movies_metadata.csv");
        if (input == null) {
            System.out.println(" No se encontró el archivo movies_metadata.csv");
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
                        peliculas.put(id, pelicula);//cambiar add por add de hashmap

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


        try (BufferedReader br = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            String linea;

            // Saltear cabecera
            br.readLine();

            // Leer cada línea del archivo
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                System.out.println("Línea leída: " + linea);


                try {
                    int userId = Integer.parseInt(partes[0].trim());
                    int movieId = Integer.parseInt(partes[1].trim());
                    double rating = Double.parseDouble(partes[2].trim());
                    long timestamp = Long.parseLong(partes[3].trim());

                    Evaluacion evaluacion = new Evaluacion(userId, movieId, rating, timestamp);
                    evaluaciones.put(userId, evaluacion);

                } catch (NumberFormatException e) {
                    System.out.println("Error al parsear línea: " + linea);
                }
            }


        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + rutaCsv);
            e.printStackTrace();
        }
    }



    @Override
    public void cargarCreditos(String rutaCsv) throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream(rutaCsv);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            String linea;
            br.readLine(); // Saltar encabezado

            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);


                    try {
                        String castTexto = campos[0].trim();
                        String crewTexto = campos[1].trim();
                        String idTexto = campos[2].trim();


                        castTexto = castTexto.replace("'", "\"");
                        crewTexto = crewTexto.replace("'", "\"");

                        JSONArray castArray = new JSONArray(castTexto);
                        JSONArray crewArray = new JSONArray(crewTexto);
                        int id = Integer.parseInt(idTexto);

                        Map<Integer, JSONObject> castMap = new HashMap<>();
                        for (int i = 0; i < castArray.length(); i++) {
                            JSONObject actor = castArray.getJSONObject(i);
                            castMap.put(actor.getInt("id"), actor);
                        }

                        Map<Integer, JSONObject> crewMap = new HashMap<>();
                        for (int i = 0; i < crewArray.length(); i++) {
                            JSONObject miembro = crewArray.getJSONObject(i);
                            crewMap.put(miembro.getInt("id"), miembro);
                        }

                        //Credits credito = new Credits(id, castMap, crewMap);
                        //creditos.put(id, credito);

                    } catch (Exception e) {
                    }

            }
        }
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
