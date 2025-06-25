import entities.Director;
import entities.Umovie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import entities.Evaluacion;
import entities.Pelicula;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class UmovieTest {

    private Umovie umovie;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    public void setUp() {
        umovie = new Umovie();

        // Redirigir salida estándar para poder verificar el output
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Crear películas (con distintos idiomas permitidos)
        umovie.getPeliculas().put(1, new Pelicula("1", "Peli Inglesa A", "en", "genero", 2020, new String[]{}));
        umovie.getPeliculas().put(2, new Pelicula("2", "Peli Inglesa B", "en", "genero", 2020, new String[]{}));
        umovie.getPeliculas().put(3, new Pelicula("3", "Peli Inglesa C", "en", "genero", 2020, new String[]{}));
        umovie.getPeliculas().put(4, new Pelicula("4", "Peli Francesa", "fr", "genero", 2020, new String[]{}));
        umovie.getPeliculas().put(5, new Pelicula("5", "Peli Española", "es", "genero", 2020, new String[]{}));

        // Crear evaluaciones para cada película
        umovie.getEvaluaciones().put(1, new Evaluacion(1, 1, 4.0, 1000L));
        umovie.getEvaluaciones().put(2, new Evaluacion(1, 2, 5.0, 1001L));
        umovie.getEvaluaciones().put(3, new Evaluacion(2, 2, 3.0, 1002L));
        umovie.getEvaluaciones().put(4, new Evaluacion(3, 3, 2.5, 1003L));
        umovie.getEvaluaciones().put(5, new Evaluacion(4, 4, 5.0, 1004L));
        umovie.getEvaluaciones().put(6, new Evaluacion(5, 5, 4.5, 1005L));

    }

    @Test
    public void testConsulta1() {
        umovie.consulta1();

        String salida = outContent.toString();

        // Verifica que las películas más evaluadas por idioma aparezcan
        assertTrue(salida.contains("1,Peli Inglesa A,1,en"));
        assertTrue(salida.contains("2,Peli Inglesa B,2,en"));
        assertTrue(salida.contains("3,Peli Inglesa C,1,en"));
        assertTrue(salida.contains("4,Peli Francesa,1,fr"));
        assertTrue(salida.contains("5,Peli Española,1,es"));

        // Verifica que se haya mostrado el tiempo de ejecución
        assertTrue(salida.contains("Tiempo de ejecución de la consulta:"));
    }

    @Test
    public void testConsulta4(){


    }
}
