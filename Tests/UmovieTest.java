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
    void testConsulta1() {
        // Capturar salida de consola
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(salida));

        sistema.consulta1();

        // Restaurar System.out
        System.setOut(originalOut);

        String output = salida.toString();

        // Verificaciones mínimas
        assertTrue(output.contains("Idioma: es"));
        assertTrue(output.contains("Idioma: en"));
        assertTrue(output.contains("Pelicula A"));
        assertTrue(output.contains("Pelicula B"));
        assertTrue(output.contains("Pelicula C"));

        // Validación de orden: Pelicula C debería aparecer antes que A
        int indexC = output.indexOf("Pelicula C");
        int indexA = output.indexOf("Pelicula A");
        assertTrue(indexC < indexA);
    }
}
