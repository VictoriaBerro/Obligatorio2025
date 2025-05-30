import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {

            System.out.println("Seleccione la opción que desee:");
            System.out.println("1. Carga de datos");
            System.out.println("2. Ejecutar consultas");
            System.out.println("3. Salir");
            int opcion = scanner.nextInt();

            if (opcion == 1) {
                long inicio = System.currentTimeMillis();
                //logica de la carga de datos
                long fin = System.currentTimeMillis();
                System.out.println("Carga de datos exitosa, tiempo de ejecución de la carga:" + (fin - inicio) + " ms");
            } else if (opcion == 2) {
                boolean salir = false;
                while (!salir) {//hago el while para q salga de este menu solo si el usuario pone 7
                    System.out.println("1. Top 5 de las películas que más calificaciones por idioma.");
                    System.out.println("2. Top 10 de las películas que mejor calificación media tienen por parte de los usuarios.");
                    System.out.println("3. Top 5 de las colecciones que más ingresos generaron.");
                    System.out.println("4. Top 10 de los directores que mejor calificación tienen");
                    System.out.println("5. Actor con más calificaciones recibidas en cada mes del año.");
                    System.out.println("6. Usuarios con más calificaciones por género");
                    System.out.println("7. Salir");
                    int opcion2 = scanner.nextInt();
                    if (opcion2 == 1) {

                    } else if (opcion2 == 2) {

                    } else if (opcion2 == 3) {

                    } else if (opcion2 == 4) {

                    } else if (opcion2 == 5) {

                    } else if (opcion2 == 6) {

                    } else if (opcion2 == 7) {
                        salir = true;
                    } else {
                        System.out.println("Opción inválida.");
                        continue;
                    }
                }
            } else if (opcion == 3) {
                continuar = false;
            } else {
                System.out.println("Opción inválida.");
                continue;
            }
        }
    }
}


