package entities;

import Interfaz.UmovieImpl;
import TADS.Hashmap.HashMap;
import TADS.exceptions.ListOutOfIndex;
import TADS.heap.MyHeapImpl;
import TADS.list.MyArrayListImpl;
import TADS.list.linked.MyLinkedListImpl;
import TADS.list.MyList;
import TADS.util.Tuple;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import com.sun.jdi.Value;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;

import static java.lang.reflect.Array.get;
import static javax.swing.UIManager.put;

public class Umovie implements UmovieImpl {
        private HashMap<Integer, Pelicula> peliculas;
        private HashMap<Integer, Evaluacion> evaluaciones;
        private HashMap<String, Director> directores;
        private HashMap<String, Actor> actores;
        private HashMap<String,String> generos;
        private HashMap<String, MyLinkedListImpl<String>> actoresConPeliculas;
        private HashMap<Integer, MyLinkedListImpl<String>> directoresConPeliculas;
        private HashMap<Integer, Creditos> creditos;
        private HashMap<String, MyLinkedListImpl<Integer>> directorMovies;
        private MyLinkedListImpl<Tuple<Integer, Tuple<Integer, Integer>>> datosPeliculas;
        private HashMap<Integer, ColeccionContador> colecciones = new HashMap<>(100000);


        public Umovie() {
            this.peliculas = new HashMap<>(100000);
            this.evaluaciones = new HashMap<>(1000000);
            this.directores = new HashMap<>(100000);
            this.actores = new HashMap<>(100000);
            this.generos = new HashMap<>(100000);
            this.actoresConPeliculas = new HashMap<>(100000);
            this.directoresConPeliculas = new HashMap<>(100000);
            this.creditos = new HashMap<>(100000);
            this.datosPeliculas = new MyLinkedListImpl<>();
;


        }


        @Override
        public void cargarPeliculas(String rutaCsv) throws IOException {
            int peliculasCargadas = 0;

            InputStream input = getClass().getClassLoader().getResourceAsStream("movies_metadata.csv");
            if (input == null) return;

            try (CSVReader csvReader = new CSVReader(new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8)))) {
                String[] headers = csvReader.readNext();
                int columnasEsperadas = headers.length;

                String[] campos;
                while ((campos = csvReader.readNext()) != null) {
                    if (campos.length < columnasEsperadas) continue;

                    try {
                        // Parseo de campos clave
                        int id = Integer.parseInt(campos[5].trim());
                        String titulo = campos[18].trim();
                        String idioma = campos[7].trim();

                        // Revenue
                        int revenue = 0;
                        String revenueTexto = campos[13].trim();
                        if (!revenueTexto.isEmpty()) {
                            try {
                                revenue = (int) Double.parseDouble(revenueTexto);
                            } catch (NumberFormatException ignored) {}
                        }

                        // Colección
                        String coleccionTexto = campos[1].trim();
                        String coleccion = "[]";
                        if (!coleccionTexto.isEmpty()) {
                            try {
                                String cleaned = coleccionTexto.replace("'", "\"");
                                JSONObject colJson = new JSONObject(cleaned);
                                if (colJson.has("id") && colJson.has("name")) {
                                    int idColeccion = colJson.getInt("id");
                                    String nombreColeccion = colJson.getString("name");
                                    coleccion = idColeccion + "/" + nombreColeccion;

                                    ColeccionContador existente = colecciones.get(idColeccion);
                                    if (existente != null) {
                                        existente.incrementar();
                                        existente.sumarRevenue(revenue);
                                    } else {
                                        colecciones.put(idColeccion, new ColeccionContador(idColeccion, nombreColeccion, revenue));
                                    }
                                }
                            } catch (Exception e) {
                                coleccion = "[]";
                            }
                        }

                        // Géneros
                        String generosTexto = campos[3].trim();
                        String[] generos = new String[0];
                        if (!generosTexto.isEmpty()) {
                            try {
                                String cleaned = generosTexto.replace("'", "\"");
                                JSONArray generosJson = new JSONArray(cleaned);
                                generos = new String[generosJson.length()];
                                for (int i = 0; i < generosJson.length(); i++) {
                                    JSONObject generoObj = generosJson.getJSONObject(i);
                                    if (generoObj.has("name")) {
                                        generos[i] = generoObj.getString("name");
                                        this.generos.put(generos[i], generos[i]);
                                    }
                                }
                            } catch (Exception ignored) {}
                        }

                        // Guardar película
                        Pelicula pelicula = new Pelicula(String.valueOf(id), titulo, idioma, coleccion, revenue, generos);
                        peliculas.put(pelicula.hashCode(), pelicula);
                        peliculasCargadas++;

                    } catch (Exception ignored) {
                        // Fila con error → se salta
                    }
                }

            } catch (CsvValidationException | IOException e) {
                System.out.println("Error al leer o parsear el archivo: " + e.getMessage());
            }
            System.out.println("Películas cargadas: " + peliculasCargadas);
        }



    @Override
    public void cargarCalificaciones(String rutaCsv) {
            int calificacionesCargadas = 0;
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
                    calificacionesCargadas++;
                } catch (Exception e) {
                    // Si la línea está mal formada, se ignora
                }
            }

        } catch (IOException e) {
            System.out.println(" Error al leer el archivo " + rutaCsv);
            e.printStackTrace();
        }
        System.out.println("🎞️ Créditos cargados: " + calificacionesCargadas);

    }

    @Override
    public void cargarCreditos(String rutaCsv) {
            int creditsCargadas = 0;
        // Initialize maps for actors, directors, and directors with movies
        HashMap<String, Actor> actoresHashMap = new HashMap<>(100000);
        TADS.Hashmap.HashMap<String, Director> directoresHashMap = new TADS.Hashmap.HashMap<>(100000);
        TADS.Hashmap.HashMap<String, MyLinkedListImpl<Integer>> directorMoviesMap = new TADS.Hashmap.HashMap<>(100000); // Director ID -> List of movie IDs

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(rutaCsv);
             Reader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {

            if (input == null) {
                System.out.println("No se encontró el archivo " + rutaCsv);
                return;
            }

            List<String[]> records = csvReader.readAll();

            Gson gson = new Gson();
            Type castListType = new TypeToken<List<Actor>>(){}.getType();
            Type crewListType = new TypeToken<List<Miembro>>(){}.getType();

            int processedRows = 0;
            int skippedRows = 0;

            for (String[] record : records) {
                try {
                    if (record.length < 3) {
                        skippedRows++;
                        continue;
                    }

                    String castStr = record[0].trim();
                    String crewStr = record[1].trim();
                    int movieId = Integer.parseInt(record[2].trim());

                    if (castStr.isEmpty() || crewStr.isEmpty()) {
                        skippedRows++;
                        continue;
                    }

                    List<Actor> castList = gson.fromJson(castStr, castListType);
                    List<Miembro> crewList = gson.fromJson(crewStr, crewListType);
                    if (castList == null || crewList == null) {
                        skippedRows++;
                        continue;
                    }

                    MyLinkedListImpl<Actor> actores = new MyLinkedListImpl<>();
                    MyLinkedListImpl<Miembro> miembros = new MyLinkedListImpl<>();

                    // Load actors into the map
                    for (Actor actor : castList) {
                        actoresHashMap.put(actor.getId(), actor);
                        actores.add(actor);
                    }

                    // Load directors into the map and track their movies
                    for (Miembro miembro : crewList) {
                        if ("Directing".equalsIgnoreCase(miembro.getJob()) || "Director".equalsIgnoreCase(miembro.getJob())) {
                            String directorId = String.valueOf(miembro.getId());
                            Director director = new Director(Integer.parseInt(directorId));
                            directoresHashMap.put(directorId, director);

                            // Add movie to the director's movie list
                            MyLinkedListImpl<Integer> movies = directorMoviesMap.get(directorId);
                            if (movies == null) {
                                movies = new MyLinkedListImpl<>();
                                directorMoviesMap.put(directorId, movies);
                            }
                            movies.add(movieId);
                            miembros.add(miembro);
                        }
                    }

                    Creditos credito = new Creditos(movieId, miembros, actores);
                    creditos.put(movieId, credito);
                    creditsCargadas++;

                } catch (Exception e) {
                    skippedRows++;
                }
            }

            this.actores = actoresHashMap;
            this.directores = directoresHashMap;
            this.directorMovies = directorMoviesMap;

        } catch (IOException | CsvException e) {
            System.out.println("Error leyendo el archivo: " + e.getMessage());
        }
        System.out.println("Créditos cargados: " + creditsCargadas);
    }












        @Override
        public void consulta1() {
            long inicio = System.currentTimeMillis();

            // Paso 1: Contar evaluaciones por película
            HashMap<Integer, Integer> evaluacionesPorPelicula = new HashMap<>(10000);
            for (Evaluacion e : evaluaciones.values()) {
                int idPelicula = e.getMovieId();
                evaluacionesPorPelicula.put(idPelicula, evaluacionesPorPelicula.getOrDefault(idPelicula, 0) + 1);
            }

            // Paso 2: Agrupar películas por idioma
            HashMap<String, List<Pelicula>> peliculasPorIdioma = new HashMap<>(100000);
            for (Pelicula p : peliculas.values()) {
                String idioma = p.getIdiomaOriginal();
                peliculasPorIdioma.computeIfAbsent(idioma, k -> new ArrayList<>()).add(p);
            }

            // Paso 3: Idiomas filtrados
            Set<String> idiomasValidos = Set.of("en", "fr", "it", "es", "pt");

            for (String idioma : idiomasValidos) {
                List<Pelicula> lista = peliculasPorIdioma.get(idioma);
                if (lista == null || lista.isEmpty()) continue;

                // Usamos PriorityQueue para ordenar por cantidad de evaluaciones (mayor primero)
                PriorityQueue<Pelicula> topPeliculas = new PriorityQueue<>((p1, p2) -> {
                    int eval1 = evaluacionesPorPelicula.getOrDefault(Integer.parseInt(p1.getId()), 0);
                    int eval2 = evaluacionesPorPelicula.getOrDefault(Integer.parseInt(p2.getId()), 0);
                    return Integer.compare(eval2, eval1); // orden descendente
                });

                topPeliculas.addAll(lista);

                // Imprimimos top 5
                for (int i = 0; i < 5 && !topPeliculas.isEmpty(); i++) {
                    Pelicula p = topPeliculas.poll();
                    int total = evaluacionesPorPelicula.getOrDefault(Integer.parseInt(p.getId()), 0);
                    System.out.printf("%s,%s,%d,%s%n", p.getId(), p.getTitulo(), total, idioma);
                }
            }

            long fin = System.currentTimeMillis(); // ⏱ Fin del tiempo
            System.out.println("⏱ Tiempo de ejecución de la consulta: " + (fin - inicio) + " ms");
        }


    public Value getOrDefault(Key key, Value defaultValue) {
        Value value = get(key);
        return (value != null) ? value : defaultValue;
    }

    public Value computeIfAbsent(Key key, java.util.function.Function<? super Key, ? extends Value> mappingFunction) {
        Value value = (Value) get(key);
        if (value == null) {
            value = mappingFunction.apply(key);
            put(key, value);
        }
        return value;
    }


        @Override
    public void consulta2() {
        long inicio = System.currentTimeMillis();

        HashMap<String, Double> sumaPorPelicula = new HashMap<>(10000000);
        HashMap<String, Integer> cantidadPorPelicula = new HashMap<>(1000000);

        for (Evaluacion e : evaluaciones.values()) {
            String id = String.valueOf(e.getMovieId());
            double puntaje = e.getRating();

            if (sumaPorPelicula.containsKey(id)) {
                sumaPorPelicula.put(id, sumaPorPelicula.get(id) + puntaje);
            } else {
                sumaPorPelicula.put(id, puntaje);
            }

            if (cantidadPorPelicula.containsKey(id)) {
                cantidadPorPelicula.put(id, cantidadPorPelicula.get(id) + 1);
            } else {
                cantidadPorPelicula.put(id, 1);
            }

        }

        MyList<Pelicula> candidatas = new MyArrayListImpl<>(100000);
        for (Pelicula p : peliculas.values()) {
            String id = p.getId();
            if (cantidadPorPelicula.containsKey(id) && cantidadPorPelicula.get(id) > 100) {
                candidatas.add(p);
            }
        }

        // Paso importante: convertir candidatas a array para poder usar Bubble Sort
        Pelicula[] pelis = new Pelicula[candidatas.getSize()];
        for (int i = 0; i < candidatas.getSize(); i++) {
            pelis[i] = candidatas.get(i);
        }

        // Ordenar por promedio (Bubble Sort)
        for (int i = 0; i < pelis.length - 1; i++) {
            for (int j = 0; j < pelis.length - i - 1; j++) {
                String id1 = pelis[j].getId();
                String id2 = pelis[j + 1].getId();

                double prom1 = sumaPorPelicula.get(id1) / cantidadPorPelicula.get(id1);
                double prom2 = sumaPorPelicula.get(id2) / cantidadPorPelicula.get(id2);

                if (prom1 < prom2) {
                    Pelicula temp = pelis[j];
                    pelis[j] = pelis[j + 1];
                    pelis[j + 1] = temp;
                }
            }
        }

        // Mostrar top 10
        System.out.println("Top 10 de películas con mejor promedio (más de 100 calificaciones):");
        for (int i = 0; i < Math.min(10, pelis.length); i++) {
            Pelicula p = pelis[i];
            String id = p.getId();
            double promedio = sumaPorPelicula.get(id) / cantidadPorPelicula.get(id);
            System.out.printf("ID: %s, Título: %s, Promedio: %.2f%n", id, p.getTitulo(), promedio);
        }

        long fin = System.currentTimeMillis();
        System.out.println("Tiempo de ejecución de la consulta: " + (fin - inicio) + " ms");
    }

        public class ColeccionContador {
            private int id;
            private String nombre;
            private int contador;
            private int revenueTotal;

            public ColeccionContador(int id, String nombre, int revenueInicial) {
                this.id = id;
                this.nombre = nombre;
                this.contador = 1;
                this.revenueTotal = revenueInicial;
            }

            public void incrementar() {
                this.contador++;
            }

            public void sumarRevenue(int revenue) {
                this.revenueTotal += revenue;
            }

            public void setRevenue(int nuevoRevenue) {
                this.revenueTotal = nuevoRevenue;
            }

            public int getRevenue() {
                return this.revenueTotal;
            }

            public int getId() {
                return id;
            }

            public String getNombre() {
                return nombre;
            }

            public int getContador() {
                return contador;
            }

            @Override
            public String toString() {
                return "📦 ID: " + id + " | Nombre: " + nombre +
                        " | 🎯 Películas: " + contador +
                        " | 💰 Revenue total: " + revenueTotal;
            }
        }

        public class SagaExtendida {
            private int idSaga;
            private String nombre;
            private int contador;
            private int revenueTotal;
            private MyArrayListImpl<String> idsPeliculas;

            public SagaExtendida(int idSaga, String nombre, int revenueInicial, String idPelicula) {
                this.idSaga = idSaga;
                this.nombre = nombre;
                this.contador = 1;
                this.revenueTotal = revenueInicial;
                this.idsPeliculas = new MyArrayListImpl<>(100);
                this.idsPeliculas.add(idPelicula);
            }

            public void agregarPelicula(String idPelicula, int revenue) {
                this.contador++;
                this.revenueTotal += revenue;
                this.idsPeliculas.add(idPelicula);
            }

            @Override
            public String toString() {
                return "Saga: " + nombre +
                        " | ID: " + idSaga +
                        " | 🎞️ Películas: " + contador +
                        " | 💰 Revenue: " + revenueTotal +
                        " | IDs: " + Arrays.toString(idsPeliculas.toArray());
            }

        }


        @Override
        public void consulta3() {
            long inicio = System.currentTimeMillis();


            HashMap<String, SagaExtendida> nuevasSagas = new HashMap<>(1000);

            for (Pelicula p : peliculas.values()) {
                String coleccion = p.getColeccion(); // "id/nombre" o "[]"
                String nombreSaga;
                int idSaga;

                if (coleccion.equals("[]")) {
                    nombreSaga = p.getTitulo();
                    idSaga = Integer.parseInt(p.getId());
                } else {
                    String[] partes = coleccion.split("/", 2);
                    if (partes.length < 2) continue;
                    idSaga = Integer.parseInt(partes[0]);
                    nombreSaga = partes[1];
                }

                // Coincidencia con colección registrada
                for (ColeccionContador c : colecciones.values()) {
                    if (c.getNombre().equals(nombreSaga)) {
                        idSaga = c.getId();
                        break;
                    }
                }

                // Actualizar o crear nueva entrada en nuevasSagas
                SagaExtendida existente = nuevasSagas.get(nombreSaga);
                if (existente != null) {
                    existente.agregarPelicula(p.getId(), p.getRevenue());
                } else {
                    SagaExtendida nueva = new SagaExtendida(idSaga, nombreSaga, p.getRevenue(), p.getId());
                    nuevasSagas.put(nombreSaga, nueva);
                }
            }

            // Pasar a MyArrayListImpl
            MyArrayListImpl<SagaExtendida> lista = new MyArrayListImpl<>(100000);
            for (SagaExtendida s : nuevasSagas.values()) {
                lista.add(s);
            }

            // Convertir a ArrayList para ordenar
            List<SagaExtendida> temp = new ArrayList<>();
            for (int i = 0; i < lista.getSize(); i++) {
                temp.add(lista.get(i));
            }

            // Ordenar por revenue descendente
            temp.sort((a, b) -> Integer.compare(b.revenueTotal, a.revenueTotal));

            // Mostrar top 5
            System.out.println("\n🔥 TOP 5 sagas con mayor revenue:");
            for (int i = 0; i < Math.min(5, temp.size()); i++) {
                System.out.println((i + 1) + ". " + temp.get(i));
            }
            long fin = System.currentTimeMillis();
            System.out.println("Tiempo de ejecución de la consulta: " + (fin - inicio) + " ms");
        }




    @Override
    public void consulta4() {// me da error porque directores tiene sus idpelicula vacio, tengo q agregarlo al descargar directores
        long ini = System.currentTimeMillis();
        MyLinkedListImpl<Tuple<Integer, Double>> directorPromedios = new MyLinkedListImpl<>();

        for (Director d : directores.values()) {
            int nombre = d.getIdMiembro();
            String idD = String.valueOf(d.getIdMiembro());
            MyLinkedListImpl<Integer> peliculasId = directorMovies.get(idD);

            double suma = 0.0;
            int contador = 0;

            for (Integer id : peliculasId) {
                for (Evaluacion ev : evaluaciones.values()) {
                    if (ev.getMovieId() == id) {
                        suma += ev.getRating();
                        contador++;
                    }
                }
            }
            if (contador > 0) {
                double promedio = suma / contador;
                directorPromedios.add(new Tuple<>(nombre, promedio));
            }

        }


        long fin = System.currentTimeMillis();
        long tiempo = fin - ini;
        System.out.println("Top 10 de los directores con mejor promedio:");
        for (int i = 0; i < 10; i++) {
            Tuple<Integer, Double> t = directorPromedios.get(i);
            System.out.printf("%s,%.2f%n", t.getFirst(), t.getSecond());
        }
        System.out.println("Tiempo de ejecución de la consulta: " + tiempo + " ms");

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
