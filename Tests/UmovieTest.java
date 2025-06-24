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
        sistema.peliculas = new HashMap<>();
        sistema.peliculas.put(1, new Pelicula("1", "Pelicula A", "es"));
        sistema.peliculas.put(2, new Pelicula("2", "Pelicula B", "es"));
        sistema.peliculas.put(3, new Pelicula("3", "Pelicula C", "en"));

        // Simulamos evaluaciones
        sistema.evaluaciones = new HashMap<>();
        sistema.evaluaciones.put(1, new Evaluacion(1, 4.5));
        sistema.evaluaciones.put(2, new Evaluacion(1, 4.0));
        sistema.evaluaciones.put(3, new Evaluacion(2, 3.5));
        sistema.evaluaciones.put(4, new Evaluacion(3, 5.0));
        sistema.evaluaciones.put(5, new Evaluacion(3, 3.0));
        sistema.evaluaciones.put(6, new Evaluacion(3, 2.0));
    }

    @Test
    void testConsulta1() {
        // Capturar salida de consola
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        System.setOut(new PrintStream(salida));

        sistema.consulta1();

        String output = salida.toString();

        // Validaciones básicas
        assertTrue(output.contains("Idioma: es"));
        assertTrue(output.contains("Idioma: en"));
        assertTrue(output.contains("Pelicula A"));
        assertTrue(output.contains("Pelicula C"));

        // Verifica orden correcto (Pelicula C tiene más evaluaciones que B o A)
        int indexC = output.indexOf("Pelicula C");
        int indexA = output.indexOf("Pelicula A");
        int indexB = output.indexOf("Pelicula B");

        assertTrue(indexC < indexA || indexC < indexB); // P. C debería estar arriba

        // Restaurar System.out
        System.setOut(System.out);
    }
}
