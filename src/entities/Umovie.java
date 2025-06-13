package entities;

import Interfaz.UmovieImpl;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Queue;

import TADS.Hashmap.HashMap;
import org.json.JSONObject;
import org.json.JSONArray;



public class Umovie implements UmovieImpl {
    private HashMap<Integer, Pelicula> peliculas;
    private HashMap<Integer, Evaluacion> evaluaciones;
    private HashMap<Integer, Miembro> miembros;

    public Umovie() {
        this.peliculas = new HashMap<>(100000000);     // Ver el tamańo
        this.evaluaciones = new HashMap<>(1000000000);
        this.miembros = new HashMap<>(100000000);
    }



    @Override
    public void cargarPeliculas(String rutaCsv) throws IOException {
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
                        peliculas.put(pelicula.hashCode(),pelicula);


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
            System.out.println(" No se encontró el archivo " + rutaCsv);
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
                    evaluaciones.put(evaluacion.hashCode(),evaluacion);
                } catch (Exception e) {
                    // Si la línea está mal formada, se ignora
                }
            }

        } catch (IOException e) {
            System.out.println(" Error al leer el archivo " + rutaCsv);
            e.printStackTrace();
        }
    }


    @Override
    public void cargarCreditos(String rutaCsv) {

    }

    @Override
    public void consulta1() {
//        // Paso 1: Calcular total de evaluaciones por película
//        HashMap<Integer, Integer> evaluacionesPorPelicula = new HashMap<>(1000000); // mapa donde la clave es el id de la peli, lo otro es cant de evaluaciones
//        for (Evaluacion e : evaluaciones) {
//            int idPelicula = e.getMovieId(); // Recorre todas las evaluaciones para obtener el ID de la película evaluada.
//            if (evaluacionesPorPelicula.pertenece(idPelicula)) {
//                int actual = evaluacionesPorPelicula.get(idPelicula);
//                evaluacionesPorPelicula.insertar(idPelicula, actual + 1);
//            } else {
//                evaluacionesPorPelicula.insertar(idPelicula, 1);
//            }
//        }
//
//        // Paso 2: Agrupar películas por idioma
//        MyHashMap<String, Queue<Pelicula>> peliculasPorIdioma = new MyHashMap<>(20);
//        for (int i = 0; i < peliculas.size(); i++) {
//            Pelicula p = peliculas.get(i);
//            String idioma = p.getIdiomaOriginal();
//
//            if (!peliculasPorIdioma.pertenece(idioma)) {
//                peliculasPorIdioma.insertar(idioma, new LinkedList<Pelicula>());
//            }
//            peliculasPorIdioma.get(idioma).add(p);
//        }
//
//        // Paso 3: Recorrer idiomas y mostrar top 5 por evaluaciones
//        Lista<String> idiomas = peliculasPorIdioma.keys();
//        for (int i = 0; i < idiomas.size(); i++) {
//            String idioma = idiomas.get(i);
//            Lista<Pelicula> lista = peliculasPorIdioma.get(idioma);
//
//            // Convertir a array para ordenamiento
//            Pelicula[] pelis = new Pelicula[lista.size()];
//            for (int j = 0; j < lista.size(); j++) {
//                pelis[j] = lista.get(j);
//            }
//
//            // Ordenar por cantidad de evaluaciones (burbuja simple)
//            for (int j = 0; j < pelis.length - 1; j++) {
//                for (int k = 0; k < pelis.length - j - 1; k++) {
//                    int eval1 = evaluacionesPorPelicula.get(pelis[k].getId());
//                    int eval2 = evaluacionesPorPelicula.get(pelis[k + 1].getId());
//                    if (eval1 < eval2) {
//                        Pelicula temp = pelis[k];
//                        pelis[k] = pelis[k + 1];
//                        pelis[k + 1] = temp;
//                    }
//                }
//            }
//
//            // Imprimir top 5
//            System.out.println("Idioma: " + idioma);
//            for (int j = 0; j < Math.min(5, pelis.length); j++) {
//                Pelicula p = pelis[j];
//                int total = evaluacionesPorPelicula.get(p.getId());
//                System.out.printf("ID: %d, Título: %s, Evaluaciones: %d, Idioma: %s%n",
//                        p.getId(), p.getTitulo(), total, p.getIdiomaOriginal());
//            }
//            System.out.println();
//        }
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
