package Arbol;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BinarySearchTreeTest {

    @Test
    void inOrder() {
        BinarySearchTree<Integer, String> bst = new BinarySearchTree<>();
        bst.insert(5, "A");
        bst.insert(3, "B");
        bst.insert(7, "C");

        List<Integer> result = bst.inOrder();
        List<Integer> expected = Arrays.asList(3, 5, 7); // InOrder debería verse así
        assertEquals(expected, result);
    }

    @Test
    void preOrder() {
        BinarySearchTree<Integer, String> bst = new BinarySearchTree<>();
        bst.insert(5, "A");
        bst.insert(3, "B");
        bst.insert(7, "C");

        List<Integer> result = bst.preOrder();
        List<Integer> expected = Arrays.asList(5, 3, 7); // PreOrder debería ser raíz, izquierda, derecha
        assertEquals(expected, result);
    }

    @Test
    void postOrder() {
        BinarySearchTree<Integer, String> bst = new BinarySearchTree<>();
        bst.insert(5, "A");
        bst.insert(3, "B");
        bst.insert(7, "C");

        List<Integer> result = bst.postOrder();
        List<Integer> expected = Arrays.asList(3, 7, 5); // PostOrder debría ser izquierda, derecha, raíz
        assertEquals(expected, result);
    }
}
