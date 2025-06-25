import entities.Evaluacion;
import entities.Pelicula;
import entities.Umovie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class UmovieTest {

    private Umovie sistema;

    @BeforeEach
    void setUp() {
        sistema = new Umovie();

        // Simulamos películas en español e inglés
        TADS.Hashmap.HashMap<Integer, Pelicula> pelis = new TADS.Hashmap.HashMap<>(1000);
        pelis.put(1, new Pelicula("1", "Pelicula A", "es", null, 0, new String[]{}));
        pelis.put(2, new Pelicula("2", "Pelicula B", "es", null, 0, new String[]{}));
        pelis.put(3, new Pelicula("3", "Pelicula C", "en", null, 0, new String[]{}));
        sistema.setPeliculas(pelis);

        // Simulamos evaluaciones
        TADS.Hashmap.HashMap<Integer, Evaluacion> evals = new TADS.Hashmap.HashMap<>(1000);
        evals.put(1, new Evaluacion(101, 1, 4.5, 0));
        evals.put(2, new Evaluacion(102, 1, 4.0, 0));
        evals.put(3, new Evaluacion(103, 2, 3.5, 0));
        evals.put(4, new Evaluacion(104, 3, 5.0, 0));
        evals.put(5, new Evaluacion(105, 3, 3.0, 0));
        evals.put(6, new Evaluacion(106, 3, 2.0, 0));
        sistema.setEvaluaciones(evals);
    }

    @Test
    void testConsulta1FormatoYContenido() {
        // Capturar la salida estándar
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(salida));

        sistema.consulta1();

        // Restaurar salida original
        System.setOut(originalOut);

        String output = salida.toString().trim();

        // ✅ Verifica que contiene los idiomas esperados
        assertTrue(output.contains("es"));
        assertTrue(output.contains("en"));
        assertTrue(output.contains("fr"));
        assertTrue(output.contains("it"));
        assertTrue(output.contains("pt"));

        // ❌ Verifica que NO contiene zh
        assertFalse(output.contains("zh"));

        // ✅ Verifica formato esperado
        assertTrue(output.contains("1,Pelicula A,2,es"));
        assertTrue(output.contains("2,Pelicula B,1,es"));
        assertTrue(output.contains("3,Pelicula C,1,en"));

        // ✅ Verifica que imprime el tiempo al final
        assertTrue(output.matches("(?s).*Tiempo de ejecución de la consulta: \\d+ ms$"));
    }

    @Test
    public void testConsulta2() {
        Umovie sistema = new Umovie();

        // Crear películas en distintos idiomas
        Pelicula p1 = new Pelicula("1", "Pelicula A", "en", "", 0, new String[0]);
        Pelicula p2 = new Pelicula("2", "Pelicula B", "es", "", 0, new String[0]);
        Pelicula p3 = new Pelicula("3", "Pelicula C", "fr", "", 0, new String[0]);
        sistema.getPeliculas().put(1, p1);
        sistema.getPeliculas().put(2, p2);
        sistema.getPeliculas().put(3, p3);

        // Agregar evaluaciones:
        // P1 → 150 evaluaciones de 5.0 (promedio: 5.0)
        for (int i = 0; i < 150; i++) {
            sistema.getEvaluaciones().put(i, new Evaluacion(i, 1, 5.0, 0));
        }

        // P2 → 80 evaluaciones de 3.0 (debe ser ignorada)
        for (int i = 150; i < 230; i++) {
            sistema.getEvaluaciones().put(i, new Evaluacion(i, 2, 3.0, 0));
        }

        // P3 → 120 evaluaciones de 4.0 (promedio: 4.0)
        for (int i = 230; i < 350; i++) {
            sistema.getEvaluaciones().put(i, new Evaluacion(i, 3, 4.0, 0));
        }

        // Capturar salida estándar
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(salida));

        sistema.consulta2();

        System.setOut(originalOut);
        String output = salida.toString().trim();

        // Validaciones
        assertTrue(output.contains("ID: 1, Título: Pelicula A, Promedio: 5.00"));
        assertTrue(output.contains("ID: 3, Título: Pelicula C, Promedio: 4.00"));
        assertFalse(output.contains("Pelicula B")); // Debe quedar fuera

        // Confirmar que se imprimió tiempo de ejecución
        assertTrue(output.matches("(?s).*Tiempo de ejecución de la consulta: \\d+ ms$"));
    }

    @Test
    public void testConsulta3() {

        // Agrego películas manualmente al hashmap
        sistema.getPeliculas().put(1, new Pelicula("1", "Iron Man", "en", "Marvel", 500000, new String[]{"Acción"}));
        sistema.getPeliculas().put(2, new Pelicula("2", "Iron Man 2", "en", "Marvel", 600000, new String[]{"Acción"}));
        sistema.getPeliculas().put(3, new Pelicula("3", "Avengers", "en", "Marvel", 800000, new String[]{"Acción"}));
        sistema.getPeliculas().put(4, new Pelicula("4", "Toy Story", "en", "Pixar", 400000, new String[]{"Animación"}));
        sistema.getPeliculas().put(5, new Pelicula("5", "Película suelta", "en", "", 700000, new String[]{"Drama"}));
        sistema.getPeliculas().put(6, new Pelicula("6", "Star Wars", "en", "Star Wars", 900000, new String[]{"Sci-Fi"}));

        // Capturar salida
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(salida));

        sistema.consulta3();

        System.setOut(originalOut);
        String output = salida.toString();

        // Verificaciones
        assertTrue(output.contains("Saga: Marvel"));
        assertTrue(output.contains("Saga: Pixar"));
        assertTrue(output.contains("Saga: Película suelta")); // si cumple con la corrección
        assertTrue(output.contains("Saga: Star Wars"));
        assertTrue(output.contains("Ingresos generados: $")); // algún total

        // Podés agregar más checks si querés verificar cantidades o IDs exactos
    }


}
