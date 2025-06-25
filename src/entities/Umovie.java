package entities;

import Interfaz.UmovieImpl;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import TADS.Hashmap.HashMap;
import TADS.exceptions.ListOutOfIndex;
import TADS.list.linked.MyLinkedListImpl;


import org.json.JSONArray;
import org.json.JSONObject;


public class Umovie implements UmovieImpl {
    private HashMap<Integer, Pelicula> peliculas;
    private HashMap<Integer, Evaluacion> evaluaciones;
    private HashMap<String, Director> directores;
    private HashMap<String, Actor> actores;
    private Map<String,String> generos = new java.util.HashMap<String,String>();
    private HashMap<String, MyLinkedListImpl<String>> actoresConPeliculas;
    private HashMap<String, MyLinkedListImpl<String>> directoresConPeliculas;
    private HashMap<Integer, Creditos> creditos;


    public Umovie() {
        this.peliculas = new HashMap<>(1000000);   // Ver el tamańo
        this.evaluaciones = new HashMap<>(1000000);
    }
    public void setPeliculas(HashMap<Integer, Pelicula> peliculas) {
        this.peliculas = peliculas;
    }

    public void setEvaluaciones(HashMap<Integer, Evaluacion> evaluaciones) {
        this.evaluaciones = evaluaciones;
    }

    public HashMap<Integer, Pelicula> getPeliculas() {
        return peliculas;
    }

    public HashMap<Integer, Evaluacion> getEvaluaciones() {
        return evaluaciones;
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

    private static HashMap<String, String> parsearObjeto(String texto) {
        HashMap<String, String> mapa = new HashMap<>(10000);
        Matcher m = Pattern.compile("'(\\w+)'\\s*:\\s*'?(.*?)'?(,|$)").matcher(texto);
        while (m.find()) {
            mapa.put(m.group(1), m.group(2));
        }
        return mapa;
    }

    @Override
    public void cargarCreditos(String rutaCsv) {
        HashMap<Integer, Creditos> mapaCreditos = new HashMap<>(100000);
        InputStream input = getClass().getClassLoader().getResourceAsStream(rutaCsv);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            String linea;
            br.readLine(); // Saltar cabecera

            int contador = 0; // Contador de créditos cargados

            while ((linea = br.readLine()) != null) {
                try {
                    int ultimaCierre = linea.lastIndexOf("]");
                    int penultimaCierre = linea.lastIndexOf("]", ultimaCierre - 1);

                    if (ultimaCierre == -1 || penultimaCierre == -1) {
                        System.err.println("❗ Línea mal formada, se saltea: " + linea);
                        continue;
                    }

                    String castJson = linea.substring(0, penultimaCierre + 1);
                    String crewJson = linea.substring(penultimaCierre + 1, ultimaCierre + 1);
                    String resto = linea.substring(ultimaCierre + 1).trim();

                    // Limpiar el ID (puede venir como ",862" o similar)
                    String movieIdStr = resto.replaceAll("[^0-9]", "");
                    if (movieIdStr.isEmpty()) {
                        System.err.println("❗ ID vacío en línea: " + linea);
                        continue;
                    }

                    int movieId = Integer.parseInt(movieIdStr);

                    HashMap<Integer, Cast> mapaCast = new HashMap<>(40);
                    HashMap<Integer, Crew> mapaCrew = new HashMap<>(40);

                    Matcher castMatcher = Pattern.compile("\\{(.*?)\\}").matcher(castJson);
                    while (castMatcher.find()) {
                        String obj = castMatcher.group(1);
                        HashMap<String, String> datos = parsearObjeto(obj);
                        Cast c = new Cast(
                                Integer.parseInt(datos.get("cast_id")),
                                datos.get("character"),
                                datos.get("credit_id"),
                                Integer.parseInt(datos.get("gender")),
                                Integer.parseInt(datos.get("id")),
                                datos.get("name"),
                                Integer.parseInt(datos.get("order")),
                                datos.get("profile_path")
                        );
                        mapaCast.put(c.getId(), c);
                    }

                    Matcher crewMatcher = Pattern.compile("\\{(.*?)\\}").matcher(crewJson);
                    while (crewMatcher.find()) {
                        String obj = crewMatcher.group(1);
                        HashMap<String, String> datos = parsearObjeto(obj);
                        Crew c = new Crew(
                                datos.get("credit_id"),
                                datos.get("department"),
                                Integer.parseInt(datos.get("gender")),
                                Integer.parseInt(datos.get("id")),
                                datos.get("job"),
                                datos.get("name"),
                                datos.get("profile_path")
                        );
                        mapaCrew.put(c.getId(), c);
                    }

                    Creditos creditos = new Creditos(movieId, mapaCast, mapaCrew);
                    mapaCreditos.put(movieId, creditos);
                    contador++;

                } catch (Exception e) {
                    System.err.println("⚠️ Error procesando línea, se omite:");
                    System.err.println(linea);
                    e.printStackTrace();
                }
            }

            System.out.println("✅ Créditos cargados correctamente: " + contador);

        } catch (Exception e) {
            System.err.println("❌ Error leyendo el archivo:");
            e.printStackTrace();
        }

        this.creditos = mapaCreditos;
    }



    @Override
    public void consulta1() {
//La función analiza un conjunto de películas y sus evaluaciones, agrupándolas por idioma,
// y para cada idioma muestra las 5 películas más evaluadas (con mayor cantidad de calificaciones).
// Finalmente, imprime el tiempo total que tardó en hacer ese procesamiento.

        //Cuenta cuántas veces fue evaluada cada película.
        //Agrupa las películas por su idioma original.
        //Para cada idioma: Ordena las películas de ese idioma por cantidad de evaluaciones (de mayor a menor).
        //Muestra hasta 5 películas con más evaluaciones, en formato: id,título,totalEvaluaciones,idioma.
        //Imprime cuánto tiempo tomó ejecutar la consulta.


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
// Recorre todas las evaluaciones y, para cada película, suma los puntajes que recibió y cuenta
// cuántas evaluaciones tiene.
// Luego, calcula el promedio de calificaciones por película dividiendo la suma total por la
// cantidad de evaluaciones de cada una.
// Agrupa las películas según su idioma original.
// Por cada idioma:
// Ordena las películas que tienen más de 100 evaluaciones por promedio de calificación de mayor
// a menor.
// Muestra en consola el top 10 de esas películas con su ID, título y promedio.
// Finalmente, imprime el tiempo de ejecución de la consulta.

        long inicio = System.currentTimeMillis(); // ⏱ Inicio del tiempo

        // Paso 1: Calcular suma total y cantidad de evaluaciones por película (clave: String id)
        HashMap<String, Double> sumaPorPelicula = new HashMap<>(10000000); // guarda la suma de ratings por película.
        HashMap<String, Integer> cantidadPorPelicula = new HashMap<>(10000000); // guarda la cantidad de veces que fue evaluada.

        for (Evaluacion e : evaluaciones.values()) {
            int idNum = e.getMovieId(); // ID como int (de Evaluacion)
            String id = String.valueOf(idNum); // lo convierto a String para usarlo como clave

            double puntaje = e.getRating();

            // Acumulo suma y cantidad
            if (sumaPorPelicula.containsKey(id)) {
                sumaPorPelicula.put(id, sumaPorPelicula.get(id) + puntaje);
                cantidadPorPelicula.put(id, cantidadPorPelicula.get(id) + 1);
            } else {
                sumaPorPelicula.put(id, puntaje);
                cantidadPorPelicula.put(id, 1);
            }
        }

        // Paso 2: Calcular promedio por película (✅ solo si tiene más de 100 calificaciones)
        HashMap<String, Double> promedioPorPelicula = new HashMap<>(1000000);
        for (String id : sumaPorPelicula.keys()) { // devuelve una lista con todas las claves (o llaves) del mapa.
            double suma = sumaPorPelicula.get(id); // Obtiene la suma de calificaciones.
            int cantidad = cantidadPorPelicula.get(id); // Obtiene cuántas evaluaciones recibió.

            if (cantidad > 100) { // ✅ filtro agregado: solo si tiene más de 100 evaluaciones
                promedioPorPelicula.put(id, suma / cantidad); // Guarda el promedio
            }
        }

        // Paso 3: Agrupar películas por idioma
        HashMap<String, MyLinkedListImpl<Pelicula>> peliculasPorIdioma = new HashMap<>(1000000);
        for (Pelicula p : peliculas.values()) {
            String idioma = p.getIdiomaOriginal();

            if (!peliculasPorIdioma.containsKey(idioma)) {
                peliculasPorIdioma.put(idioma, new MyLinkedListImpl<>()); // Esto agrupa las películas por idioma en un MyLinkedListImpl.
            }

            peliculasPorIdioma.get(idioma).add(p);
        }

        // Paso 4: Recorrer idiomas y mostrar top 10 por promedio
        for (String idioma : peliculasPorIdioma.keys()) {
            MyLinkedListImpl<Pelicula> lista = peliculasPorIdioma.get(idioma);

            // MyLinkedListImpl no se puede ordenar directamente → se pasa a un array.
            Pelicula[] pelis = new Pelicula[lista.getSize()];
            for (int i = 0; i < lista.getSize(); i++) {
                try {
                    pelis[i] = lista.get(i);
                } catch (ListOutOfIndex e) {
                    e.printStackTrace();
                }
            }

            // Usamos bubble sort para ordenar de mayor a menor promedio
            for (int i = 0; i < pelis.length - 1; i++) {
                for (int j = 0; j < pelis.length - i - 1; j++) {
                    String id1 = pelis[j].getId();
                    String id2 = pelis[j + 1].getId();

                    double prom1 = promedioPorPelicula.containsKey(id1) ? promedioPorPelicula.get(id1) : 0;
                    double prom2 = promedioPorPelicula.containsKey(id2) ? promedioPorPelicula.get(id2) : 0;

                    if (prom1 < prom2) {
                        Pelicula temp = pelis[j];
                        pelis[j] = pelis[j + 1];
                        pelis[j + 1] = temp;
                    }
                }
            }

            // Mostrar top 10
            System.out.println("Idioma: " + idioma);
            int mostradas = 0;

            for (int i = 0; i < pelis.length && mostradas < 10; i++) {
                Pelicula p = pelis[i];
                String id = p.getId();

                if (promedioPorPelicula.containsKey(id)) { // Solo mostrar si tiene >100 evaluaciones
                    double promedio = promedioPorPelicula.get(id);
                    System.out.printf("ID: %s, Título: %s, Promedio: %.2f%n", id, p.getTitulo(), promedio);
                    mostradas++;
                }
            }

            System.out.println(); // Separador entre idiomas
        }

        long fin = System.currentTimeMillis(); // ⏱ Fin del tiempo
        System.out.println("Tiempo de ejecución de la consulta: " + (fin - inicio) + " ms");
    }



    @Override
    public void consulta3() {
//“La función agrupa las películas por su colección, calcula ingresos totales y cantidad
// de películas por colección, ordena de mayor a menor por ingresos y muestra las 5 más
// importantes junto con sus películas e ingresos.”

        // Paso 1: Agrupar películas por saga (colección o título si está vacía)
        HashMap<String, MyLinkedListImpl<Pelicula>> peliculasPorSaga = new HashMap<>(100);

        for (Pelicula p : peliculas.values()) {
            String coleccion = (p.getColeccion() != null && !p.getColeccion().isEmpty()) ? p.getColeccion() : p.getTitulo();

            if (!peliculasPorSaga.containsKey(coleccion)) {
                peliculasPorSaga.put(coleccion, new MyLinkedListImpl<>());
            }
            peliculasPorSaga.get(coleccion).add(p);
        }

        // Paso 2: Armar arreglos con los datos de cada saga
        int n = peliculasPorSaga.size();
        String[] nombres = new String[n];
        int[] revenues = new int[n];
        int[] cantidades = new int[n];
        MyLinkedListImpl<String>[] ids = new MyLinkedListImpl[n];

        int idx = 0;
        for (String coleccion : peliculasPorSaga.keys()) {
            MyLinkedListImpl<Pelicula> lista = peliculasPorSaga.get(coleccion);
            nombres[idx] = coleccion;
            revenues[idx] = 0;
            cantidades[idx] = lista.getSize();
            ids[idx] = new MyLinkedListImpl<>();

            for (int i = 0; i < lista.getSize(); i++) {
                try {
                    Pelicula p = lista.get(i);
                    revenues[idx] += p.getRevenue();
                    ids[idx].add(p.getId());
                } catch (ListOutOfIndex e) {
                    e.printStackTrace();
                }
            }
            idx++;
        }

        // Paso 3: Bubble sort por revenue descendente
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (revenues[j] < revenues[j + 1]) {
                    // Swap todo
                    int tempRev = revenues[j];
                    revenues[j] = revenues[j + 1];
                    revenues[j + 1] = tempRev;

                    int tempCant = cantidades[j];
                    cantidades[j] = cantidades[j + 1];
                    cantidades[j + 1] = tempCant;

                    String tempNom = nombres[j];
                    nombres[j] = nombres[j + 1];
                    nombres[j + 1] = tempNom;

                    MyLinkedListImpl<String> tempIds = ids[j];
                    ids[j] = ids[j + 1];
                    ids[j + 1] = tempIds;
                }
            }
        }

        // Paso 4: Mostrar el top 5
        System.out.println("Top 5 sagas con más ingresos:");
        for (int i = 0; i < Math.min(5, n); i++) {
            System.out.println("Saga: " + nombres[i]);
            System.out.println("Cantidad de películas: " + cantidades[i]);
            System.out.println("IDs de películas: " + ids[i]);
            System.out.println("Ingresos generados: $" + revenues[i]);
            System.out.println();
        }
    }


    @Override
    public void consulta4() {
        // Paso 1: HashMap para acumular suma de ratings y cantidad de evaluaciones por director
        HashMap<String, Double> sumaRatings = new HashMap<>(100);
        HashMap<String, Integer> cantidadRatings = new HashMap<>(100);

        // Paso 2: Recorremos TODAS las evaluaciones
        for (Evaluacion e : evaluaciones.values()) {
            String idPelicula = String.valueOf(e.getMovieId()); // Convertimos el ID a String

            // Verificamos si la película evaluada existe
            if (peliculas.containsKey(Integer.valueOf(idPelicula))) {
                Pelicula p = peliculas.get(Integer.valueOf(idPelicula));

                // Buscamos al director de esa película
                for (Director d : directores.values()) {
                    if (d.getPeliculasId().contains(idPelicula)) {  // La película es de este director
                        String nombre = d.getName();

                        // Acumulamos el rating
                        double suma = sumaRatings.containsKey(nombre) ? sumaRatings.get(nombre) : 0.0;
                        int cantidad = cantidadRatings.containsKey(nombre) ? cantidadRatings.get(nombre) : 0;

                        sumaRatings.put(nombre, suma + e.getRating());
                        cantidadRatings.put(nombre, cantidad + 1);

                        break; // Ya encontramos al director correcto, salimos
                    }
                }
            }
        }

        // Paso 3: Calcular promedio de cada director con +100 evaluaciones
        class InfoDirector {
            String nombre;
            int cantidadPeliculas;
            int totalEvaluaciones;
            double promedio;

            InfoDirector(String nombre, int cantidadPeliculas, int totalEvaluaciones, double promedio) {
                this.nombre = nombre;
                this.cantidadPeliculas = cantidadPeliculas;
                this.totalEvaluaciones = totalEvaluaciones;
                this.promedio = promedio;
            }
        }

        MyLinkedListImpl<InfoDirector> lista = new MyLinkedListImpl<>();

        for (String nombre : sumaRatings.keys()) {
            int totalEval = cantidadRatings.get(nombre);
            if (totalEval >= 100) {
                double promedio = sumaRatings.get(nombre) / totalEval;

                // Buscar director por nombre
                Director encontrado = null;
                for (Director d : directores.values()) {
                    if (d.getName().equals(nombre)) {
                        encontrado = d;
                        break;
                    }
                }

                if (encontrado != null) {
                    int cantPeliculas = encontrado.getPeliculasId().getSize();
                    lista.add(new InfoDirector(nombre, cantPeliculas, totalEval, promedio));
                }
            }
        }

        // Paso 4: Ordenamos por calificación promedio (descendente)
        InfoDirector[] top = lista.toArray();

        for (int i = 0; i < top.length - 1; i++) {
            for (int j = 0; j < top.length - i - 1; j++) {
                if (top[j].promedio < top[j + 1].promedio) {
                    InfoDirector temp = top[j];
                    top[j] = top[j + 1];
                    top[j + 1] = temp;
                }
            }
        }

        // Paso 5: Mostramos top 10
        System.out.println("Top 10 directores con mejor calificación promedio (mínimo 100 evaluaciones):");
        for (int i = 0; i < Math.min(10, top.length); i++) {
            InfoDirector d = top[i];
            System.out.printf("Nombre: %s, Películas: %d, Evaluaciones: %d, Promedio: %.2f%n",
                    d.nombre, d.cantidadPeliculas, d.totalEvaluaciones, d.promedio);
        }
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
