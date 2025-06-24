import entities.Umovie;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;
        Umovie umovie = new Umovie();

        while (continuar) {
            System.out.println("Seleccione la opción que desee:");
            System.out.println("1. Carga de datos");
            System.out.println("2. Ejecutar consultas");
            System.out.println("3. Salir");

            if (!scanner.hasNextInt()) {
                System.out.println("❌ Por favor, ingrese un número válido.");
                scanner.next();
                continue;
            }

            int opcion = scanner.nextInt();

            if (opcion == 1) {
                long inicio = System.currentTimeMillis();

                try {
                    umovie.cargarPeliculas("movies_metadata.csv");
                    umovie.cargarCalificaciones("ratings_1mm.csv"); // corregido
                   // umovie.cargarCreditos("credits.csv"); // corregido
                    long fin = System.currentTimeMillis();
                    long duracion = fin - inicio;
                    System.out.println("✔️ Carga de datos exitosa. Tiempo: " + duracion + " milisegundos.");
                } catch (Exception e) {
                    System.out.println("❌ Error durante la carga: " + e.getMessage());
                }

            } else if (opcion == 2) {
                boolean salir = false;
                while (!salir) {
                    System.out.println("Seleccione una consulta:");
                    System.out.println("1. Top 5 de las películas que más calificaciones por idioma.");
                    System.out.println("2. Top 10 de las películas que mejor calificación media tienen por parte de los usuarios.");
                    System.out.println("3. Top 5 de las colecciones que más ingresos generaron.");
                    System.out.println("4. Top 10 de los directores que mejor calificación tienen.");
                    System.out.println("5. Actor con más calificaciones recibidas en cada mes del año.");
                    System.out.println("6. Usuarios con más calificaciones por género.");
                    System.out.println("7. Salir");

                    if (!scanner.hasNextInt()) {
                        System.out.println("❌ Por favor, ingrese un número válido.");
                        scanner.next();
                        continue;
                    }

                    int opcion2 = scanner.nextInt();
                    if (opcion2 == 1) {
                        umovie.consulta1();
                    } else if (opcion2 == 2) {
                        umovie.consulta2();
                    } else if (opcion2 == 3) {
                        umovie.consulta3();
                    } else if (opcion2 == 4) {
                        umovie.consulta4();
                    } else if (opcion2 == 5) {
                        umovie.consulta5();
                    } else if (opcion2 == 6) {
                        umovie.consulta6();
                    } else if (opcion2 == 7) {
                        salir = true;
                    } else {
                        System.out.println("❌ Opción inválida.");
                    }
                }
            } else if (opcion == 3) {
                continuar = false;
                System.out.println("👋 ¡Gracias por usar UMovie! Hasta la próxima.");
            } else {
                System.out.println("❌ Opción inválida.");
            }
        }
    }
}
