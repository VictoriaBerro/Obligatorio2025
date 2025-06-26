package entities;

import Interfaz.UmovieImpl;
import TADS.Hashmap.HashMap;
import TADS.list.MyArrayListImpl;
import TADS.list.linked.MyLinkedListImpl;
import TADS.list.MyList;
import TADS.util.Tuple;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

    public class Umovie implements UmovieImpl {
        private HashMap<Integer, Pelicula> peliculas;
        private HashMap<Integer, Evaluacion> evaluaciones;
        private HashMap<String, Director> directores;
        private HashMap<String, Actor> actores;
        private HashMap<String,String> generos;
        private HashMap<String, MyLinkedListImpl<String>> actoresConPeliculas;
        private HashMap<String, MyLinkedListImpl<String>> directoresConPeliculas;
        private HashMap<Integer, Creditos> creditos;
        private HashMap<String, MyLinkedListImpl<Integer>> directorMovies;
        private MyLinkedListImpl<Tuple<Integer, Tuple<Integer, Integer>>> datosPeliculas;


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

        }

        public class InfoSaga {
            public int idColeccion;
            public String nombreColeccion;
            public int revenueAcumulado;
            public int cantidadPeliculas;

            public InfoSaga(int idColeccion, String nombreColeccion, int revenue) {
                this.idColeccion = idColeccion;
                this.nombreColeccion = nombreColeccion;
                this.revenueAcumulado = revenue;
                this.cantidadPeliculas = 1;
            }
        }

        @Override
        public void cargarPeliculas(String rutaCsv) throws IOException {
            InputStream input = getClass().getClassLoader().getResourceAsStream("movies_metadata.csv");
            if (input == null) {
                System.out.println("No se encontró el archivo movies_metadata.csv");
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
                            String idTexto = campos[5].trim();

                            // Título
                            String titulo = campos[17].trim();

                            // Idioma original
                            String idioma = campos[7].trim();
                            //el .trim() elimina espacios en blanco

                            // Revenue
                            int revenue = 0;
                            int id;
                            try {
                                id = Integer.parseInt(idTexto);
                            } catch (NumberFormatException e) {
                                continue; // si no es un ID numérico, ignorar esa línea
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
                                try {
                                    // 👉 Arreglamos las comillas simples por dobles para que sea JSON válido
                                    generosTexto = generosTexto.replace("'", "\"");

                                    JSONArray generosJson = new JSONArray(generosTexto.substring(1, generosTexto.length() - 1));
                                    generos = new String[generosJson.length()];
                                    for (int i = 0; i < generosJson.length(); i++) {
                                        JSONObject generoObj = generosJson.getJSONObject(i);
                                        if (generoObj.has("name")) {
                                            generos[i] = generoObj.getString("name");
                                            this.generos.put(generos[i],generos[i]);
                                        }
                                    }
                                } catch (Exception e) {

                                }
                            }


                            // Crear objeto Pelicula
                            String idd = String.valueOf(id);
                            Pelicula pelicula = new Pelicula(idd, titulo, idioma, coleccion, revenue, generos);
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


//    private static HashMap<String, String> parsearObjeto(String texto) {
//        HashMap<String, String> mapa = new HashMap<>(10000);
//        Matcher m = Pattern.compile("'(\\w+)'\\s*:\\s*'?(.*?)'?(,|$)").matcher(texto);
//        while (m.find()) {
//            mapa.put(m.group(1), m.group(2));
//        }
//        return mapa;
//    }
        }

    @Override
    public void cargarCreditos(String rutaCsv) {
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
                    processedRows++;

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
    }





    @Override
    public void consulta1() {
        long inicio = System.currentTimeMillis(); // ⏱ Inicio del tiempo
//        // Paso 1: Calcular total de evaluaciones por película
        HashMap<Integer, Integer> evaluacionesPorPelicula = new HashMap<>(10000); // mapa donde la clave es el id de la peli, lo otro es cant de evaluaciones

        for (Evaluacion e : evaluaciones.values()) { //Es como si dijeras: "dame solo las evaluaciones, no me importa la clave del mapa".
            int idPelicula = e.getMovieId(); // Obtenemos el ID de la película evaluada

            if (evaluacionesPorPelicula.containsKey(idPelicula)) { // Si la película ya fue evaluada antes, incrementamos su contador
                int actual = evaluacionesPorPelicula.get(idPelicula); // Obtenemos el conteo actual
                evaluacionesPorPelicula.put(idPelicula, actual + 1); // Sumamos una nueva evaluación
            } else {
                evaluacionesPorPelicula.put(idPelicula, 1);         // Si es la primera evaluación de esa película, la inicializamos con 1
            }
        }

        // Paso 2: Agrupar películas por idioma
        HashMap<String, Queue<Pelicula>> peliculasPorIdioma = new HashMap<>(10000); //clave es el idioma original de la película y  y el valor es una cola (LinkedList) con todas las películas que tienen ese idioma.

        // Recorremos todas las películas del hashmap (solo los valores)
        for (Pelicula p : peliculas.values()) {
            String idioma = p.getIdiomaOriginal(); // Obtenemos el idioma original de la película

            // Si todavía no hay una entrada para ese idioma, se crea una lista vacía
            if (!peliculasPorIdioma.containsKey(idioma)) {
                peliculasPorIdioma.put(idioma, new LinkedList<>()); // Inicializa la cola
            }

            // Agregamos la película a la lista correspondiente a ese idioma
            peliculasPorIdioma.get(idioma).add(p);
        }


// Paso 3: Recorrer idiomas y mostrar top 5 películas más evaluadas por idioma

    // Obtenemos la lista de idiomas (claves del mapa peliculasPorIdioma)
        MyLinkedListImpl<String> idiomas = peliculasPorIdioma.keys();


        for (int i = 0; i < idiomas.getSize(); i++) { //tomas cada idioma y accedes a su lista de peliculas
            String idioma = idiomas.get(i);
            // FILTRAR IDIOMAS
            if (!idioma.equals("en") && !idioma.equals("fr") && !idioma.equals("it") && !idioma.equals("es") && !idioma.equals("pt")) {
                continue; // lo salteamos si no está en la lista
            }
            Queue<Pelicula> lista = peliculasPorIdioma.get(idioma);

        // Convertís la Queue en un array, porque no podés ordenar directamente una Queue. Entonces, copiás las películas una por una al array pelis[].
            Pelicula[] pelis = new Pelicula[lista.size()];
            LinkedList<Pelicula> linkedList = (LinkedList<Pelicula>) lista;
            for (int j = 0; j < linkedList.size(); j++) {
                pelis[j] = linkedList.get(j);
            }

        // Ordenamos las películas por cantidad de evaluaciones (de mayor a menor) usando bubble sort
            for (int j = 0; j < pelis.length - 1; j++) {
                for (int k = 0; k < pelis.length - j - 1; k++) {
                    // Obtenemos las evaluaciones, y si no hay, asumimos 0
                    int eval1 = evaluacionesPorPelicula.get(Integer.parseInt(pelis[k].getId())) != null
                            ? evaluacionesPorPelicula.get(Integer.parseInt(pelis[k].getId()))
                            : 0;  // parseInt lo pasa a Integer

                    int eval2 = evaluacionesPorPelicula.get(Integer.parseInt(pelis[k + 1].getId())) != null
                            ? evaluacionesPorPelicula.get(Integer.parseInt(pelis[k + 1].getId())) : 0;

                    // Si eval1 < eval2, las intercambiamos
                    if (eval1 < eval2) {
                        Pelicula temp = pelis[k];
                        pelis[k] = pelis[k + 1];
                        pelis[k + 1] = temp;
                    }}}

        // Imprimimos el top 5 de ese idioma

            for (int j = 0; j < Math.min(5, pelis.length); j++) {
                Pelicula p = pelis[j];
                int total = evaluacionesPorPelicula.get(Integer.parseInt(p.getId())) != null
                        ? evaluacionesPorPelicula.get(Integer.parseInt(p.getId()))
                        : 0;

                // 👉 Cambio de formato de salida:
                System.out.printf("%s,%s,%d,%s%n",
                        p.getId(), p.getTitulo(), total, p.getIdiomaOriginal());
            }
        }

        long fin = System.currentTimeMillis(); // ⏱ Fin del tiempo
        System.out.println("Tiempo de ejecución de la consulta: " + (fin - inicio) + " ms");
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


        @Override
        public void consulta3() {
            class SagaInfo {
                String nombre;
                int cantidadPeliculas = 0;
                int ingresosTotales = 0;
                MyLinkedListImpl<String> idsPeliculas = new MyLinkedListImpl<>();

                SagaInfo(String nombre) {
                    this.nombre = nombre;
                }
            }

            HashMap<String, SagaInfo> sagas = new HashMap<>(100000);

            for (Pelicula p : peliculas.values()) {
                String coleccionStr = p.getColeccion();
                String nombreSaga;

                if (coleccionStr != null && !coleccionStr.trim().isEmpty() && !coleccionStr.trim().equals("{}")) {
                    try {
                        JSONObject col = new JSONObject(coleccionStr.replace("'", "\""));
                        nombreSaga = col.getString("name");
                    } catch (Exception e) {
                        nombreSaga = p.getTitulo();
                    }
                } else {
                    nombreSaga = p.getTitulo();
                }

                if (!sagas.containsKey(nombreSaga)) {
                    sagas.put(nombreSaga, new SagaInfo(nombreSaga));
                }

                SagaInfo info = sagas.get(nombreSaga);
                info.cantidadPeliculas++;
                info.ingresosTotales += p.getRevenue();
                info.idsPeliculas.add(p.getId());
            }

            // Convertir a lista para ordenar
            MyList<SagaInfo> valores = sagas.values();
            List<SagaInfo> lista = new ArrayList<>();

            for (int i = 0; i < valores.getSize(); i++) {
                lista.add(valores.get(i));
            }

            lista.sort((a, b) -> Integer.compare(b.ingresosTotales, a.ingresosTotales));

            // Mostrar
            System.out.println("\n🎬 Top 5 sagas por ingresos:");
            for (int i = 0; i < Math.min(5, lista.size()); i++) {
                SagaInfo saga = lista.get(i);
                System.out.println("- " + saga.nombre);
                System.out.println("   🔢 Cantidad de películas: " + saga.cantidadPeliculas);
                System.out.println("   🎞️ IDs de películas: " + saga.idsPeliculas);
                System.out.println("   💸 Ingresos totales: " + saga.ingresosTotales);
            }
        }




    @Override
    public void consulta4() {
        long ini = System.currentTimeMillis();
        MyLinkedListImpl<Tuple<Integer, Double>> directorPromedios = new MyLinkedListImpl<>();

        for (Director d : directores.values()) {
            int nombre = d.getIdMiembro();
            MyLinkedListImpl<String> peliculasId = d.getPeliculasId();

            double suma = 0.0;
            int contador = 0;

            for (String id : peliculasId) {
                Integer iid = Integer.parseInt(id);
                for (Evaluacion ev : evaluaciones.values()) {
                    if (ev.getMovieId() == iid) {
                        suma += ev.getRating();
                        contador++;
                    }
                }
            }
            if (contador > 0) {
                double promedio = suma / contador;
                directorPromedios.add(new Tuple<>(nombre, promedio));
            }
            for (int i = 0; i < directorPromedios.getSize(); i++) {
                for (int j = 0; j < directorPromedios.getSize() - 1 - i; j++) {
                    if (directorPromedios.get(j).compareTo(directorPromedios.get(j + 1)) > 0) {
                        Tuple<Integer, Double> temp = directorPromedios.get(j);
                        directorPromedios.set(j, directorPromedios.get(j + 1));
                        directorPromedios.set(j + 1, temp);
                    }
                }
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
