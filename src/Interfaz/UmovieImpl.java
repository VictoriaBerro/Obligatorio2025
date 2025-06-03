package Interfaz;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface UmovieImpl {
    void cargarPeliculas(String rutaCsv) throws IOException;
    void cargarCalificaciones(String rutaCsv);
    void cargarParticipantes(String rutaCsv);

    void consulta1(); // Top 5 por idioma
    void consulta2();
    void consulta3();
    void consulta4();
    void consulta5();
    void consulta6();
}
