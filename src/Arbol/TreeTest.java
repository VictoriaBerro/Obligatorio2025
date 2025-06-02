package Arbol;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TreeTest {

    @Test
    void loadPostFijaExpression() {
        Tree<String, String> tree = new Tree<>(); // crea el árbol
        tree.loadPostFijaExpression("ab+c*"); // carga la expresión postfija (a b + c *)

        List<String> inOrderResult = tree.inOrder(); // obtiene el recorrido inOrder

        // Muestra el recorrido en pantalla
        System.out.println("Recorrido inOrder del árbol generado:");
        for (String key : inOrderResult) {
            System.out.print(key + " ");
        }

        // Verifica que el recorrido inOrder sea el esperado (a + b * c)
        List<String> expected = Arrays.asList("a", "+", "b", "*", "c");
        assertEquals(expected, inOrderResult); // compara el resultado con el esperado
    }
}