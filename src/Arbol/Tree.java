package Arbol;// CONCEPTOS
// Forma recursiva, son funciones que se llaman a sí mismas para ir navegando el árbol.
// CONCEPTOS
// Forma recursiva,que se llame a sí mismo hasta que encuentre algo que lo termine
import java.util.*;

public class Tree<K extends Comparable<K>, T> implements MyTree<K, T> {
    private Node<K, T> root; //nodo de objeto T y clave K

    @Override
    public T find(K key) {
        return findRecursive(root, key); // inicia la búsqueda desde la raíz
    }

    private T findRecursive(Node<K, T> actual, K key) {
        if (actual == null) {
            return null; // no se encontró la clave
        }

        if (actual.getKey().equals(key)) { // si la clave coincide, retorna el dato
            return actual.getData();
        }

        T encontrado = findRecursive(actual.getLeftChild(), key); // busca en el hijo izquierdo

        if (encontrado == null) { // si no lo encuentra en el izquierdo, busca en el derecho
            encontrado = findRecursive(actual.getRightChild(), key);
        }
        return encontrado; // retorna el dato si lo encuentra o null si no existe
    }

    @Override
    public void insert(K key, T data, K parentKey) {
        if (root == null) { // si el árbol está vacío, inserta en la raíz
            root = new Node<>(key, data);
            return;
        }
        insertRecursive(root, key, data, parentKey); // inserta recursivamente
    }

    private boolean insertRecursive(Node<K, T> actual, K key, T data, K parentKey) {
        if (parentKey == null) {
            throw new IllegalStateException("No se encontró el nodo padre " + parentKey); // si no hay clave padre, error
        }

        if (actual == null) { // nodo vacío, no se puede insertar aquí
            return false;
        }

        if (actual.getKey().equals(parentKey)) { // si encuentra el nodo padre
            if (actual.getLeftChild() == null) { // si el hijo izquierdo está vacío, inserta ahí
                actual.setLeftChild(new Node<>(key, data));
                return true;
            } else if (actual.getRightChild() == null) { // si el derecho está vacío, inserta ahí
                actual.setRightChild(new Node<>(key, data));
                return true;
            } else { // si ya tiene dos hijos, error
                throw new IllegalStateException("El nodo con clave " + parentKey + " ya tiene dos hijos.");
            }
        }
        // busca en ambos hijos recursivamente
        return insertRecursive(actual.getLeftChild(), key, data, parentKey)
                || insertRecursive(actual.getRightChild(), key, data, parentKey);
    }

    @Override
    public void delete(K key) {
        root = deleteRecursive(root, key); // elimina recursivamente desde la raíz
    }

    private Node<K, T> deleteRecursive(Node<K, T> actual, K key) { // devuelve el nodo modificado tras eliminar
        if (actual == null) { // no se encontró la clave a eliminar en ese camino
            return null;
        }

        if (actual.getKey().equals(key)) { // si encuentra el nodo a eliminar
            // Caso hoja
            if (actual.getLeftChild() == null && actual.getRightChild() == null) {
                return null; // elimina el nodo (caso hoja)
            }
            // Caso un hijo
            if (actual.getLeftChild() == null) return actual.getRightChild(); // reemplaza por el derecho si no hay izquierdo
            if (actual.getRightChild() == null) return actual.getLeftChild(); // reemplaza por el izquierdo si no hay derecho

            // Caso dos hijos (reemplazamos con el más a la izquierda del subárbol derecho)
            // Para reemplazar un nodo con dos hijos, se usa el menor del subárbol derecho, que es el nodo más a la izquierda de ese subárbol.
            Node<K, T> sucesor = findMin(actual.getRightChild());
            actual.setKey(sucesor.getKey()); // reemplaza la clave con la del sucesor
            actual.setData(sucesor.getData()); // reemplaza el dato con el del sucesor
            // elimina el sucesor en el subárbol derecho
            actual.setRightChild(deleteRecursive(actual.getRightChild(), sucesor.getKey()));
            return actual; // retorna el nodo modificado
        }

        // sigue buscando en ambos hijos recursivamente
        actual.setLeftChild(deleteRecursive(actual.getLeftChild(), key));
        actual.setRightChild(deleteRecursive(actual.getRightChild(), key));
        return actual; // retorna el nodo modificado
    }

    private Node<K, T> findMin(Node<K, T> nodo) { // encuentra el nodo con la clave mínima (más a la izquierda)
        while (nodo.getLeftChild() != null) { // recorre hacia la izquierda
            nodo = nodo.getLeftChild();
        }
        return nodo; // retorna el nodo más pequeño
    }

    @Override // cuenta los nodos
    public int size() {
        return sizeRecursive(root); // cuenta recursivamente desde la raíz
    }

    private int sizeRecursive(Node<K, T> node) {
        if (node == null) return 0; // si el nodo es nulo, no cuenta
        return 1 + sizeRecursive(node.getLeftChild()) + sizeRecursive(node.getRightChild()); // cuenta el nodo actual más sus hijos
    }

    @Override
    public int countLeaf() { // cuenta solo las hojas
        return countLeafRecursive(root); // cuenta recursivamente desde la raíz
    }

    private int countLeafRecursive(Node<K, T> node) {
        if (node == null) return 0; // nodo vacío, no cuenta
        if (node.getLeftChild() == null && node.getRightChild() == null) return 1; // si es hoja, cuenta 1
        // suma las hojas de ambos subárboles
        return countLeafRecursive(node.getLeftChild()) + countLeafRecursive(node.getRightChild());
    }

    @Override
    public int countCompleteElements() { // cuenta los nodos completos (con ambos hijos)
        return countCompleteRecursive(root); // cuenta recursivamente desde la raíz
    }

    private int countCompleteRecursive(Node<K, T> node) {
        if (node == null) return 0; // nodo vacío, no cuenta
        int count = 0;
        if (node.getLeftChild() != null && node.getRightChild() != null) count++; // si tiene ambos hijos, cuenta 1
        // suma los nodos completos de ambos subárboles
        count += countCompleteRecursive(node.getLeftChild());
        count += countCompleteRecursive(node.getRightChild());
        return count;
    }

    @Override
    public List<K> inOrder() { // recorrido en orden (izquierda, raíz, derecha)
        List<K> result = new ArrayList<>();
        inOrderRecursive(root, result);
        return result;
    }

    private void inOrderRecursive(Node<K, T> node, List<K> result) {
        if (node != null) {
            inOrderRecursive(node.getLeftChild(), result); // izquierda
            result.add(node.getKey()); // raíz
            inOrderRecursive(node.getRightChild(), result); // derecha
        }
    }

    @Override
    public List<K> preOrder() { // recorrido preorden (raíz, izquierda, derecha)
        List<K> result = new ArrayList<>();
        preOrderRecursive(root, result);
        return result;
    }

    private void preOrderRecursive(Node<K, T> node, List<K> result) {
        if (node != null) {
            result.add(node.getKey()); // raíz
            preOrderRecursive(node.getLeftChild(), result); // izquierda
            preOrderRecursive(node.getRightChild(), result); // derecha
        }
    }

    @Override
    public List<K> postOrder() { // recorrido postorden (izquierda, derecha, raíz)
        List<K> result = new ArrayList<>();
        postOrderRecursive(root, result);
        return result;
    }


    private void postOrderRecursive(Node<K, T> node, List<K> result) {
        if (node != null) {
            postOrderRecursive(node.getLeftChild(), result); // izquierda
            postOrderRecursive(node.getRightChild(), result); // derecha
            result.add(node.getKey()); // raíz
        }
    }
    // RECORRIDO POR NIVELES

    public List<K> recorridoPorNiveles() { // recorrido por niveles (de arriba hacia abajo, de izquierda a derecha)
        List<K> result = new ArrayList<>(); // lista para guardar el resultado
        if (root == null) return result; // si el árbol está vacío, devuelve lista vacía

        Queue<Node<K, T>> queue = new LinkedList<>(); // cola para recorrer el árbol nivel por nivel
        queue.add(root); // agrega la raíz a la cola

        while (!queue.isEmpty()) { // mientras haya nodos en la cola
            Node<K, T> current = queue.poll(); // saca el primer nodo de la cola
            result.add(current.getKey()); // agrega su clave a la lista de resultados

            if (current.getLeftChild() != null) queue.add(current.getLeftChild()); // agrega el hijo izquierdo si existe
            if (current.getRightChild() != null) queue.add(current.getRightChild()); // agrega el hijo derecho si existe
        }

        return result; // devuelve la lista con las claves en orden por niveles
    }

    // MÉTODO PARA CARGAR EXPRESIÓN POSTFIJA Y GENERAR ÁRBOL ALGEBRAICO

    public void loadPostFijaExpression(String sPostFija) {
        Stack<Node<String, String>> stack = new Stack<>(); // pila para construir el árbol (usamos String para claves y datos)

        for (char c : sPostFija.toCharArray()) { // recorremos cada caracter de la expresión postfija
            if (Character.isLetterOrDigit(c)) { // si es operando (letra o dígito)
                stack.push(new Node<>(String.valueOf(c), String.valueOf(c))); // crea un nodo hoja y lo mete en la pila
            } else if (isOperator(c)) { // si es operador (+, -, *, /)
                Node<String, String> right = stack.pop(); // saca el hijo derecho
                Node<String, String> left = stack.pop(); // saca el hijo izquierdo
                Node<String, String> operatorNode = new Node<>(String.valueOf(c), String.valueOf(c)); // crea el nodo operador
                operatorNode.setLeftChild(left); // asigna hijo izquierdo
                operatorNode.setRightChild(right); // asigna hijo derecho
                stack.push(operatorNode); // mete el nodo operador en la pila
            }
        }

        Node<String, String> expressionRoot = stack.pop(); // al final, la raíz del árbol es el último nodo en la pila

        // Convertimos el árbol de String a K y T para que sea compatible con el árbol actual
        this.root = (Node<K, T>) expressionRoot;
    }

    private boolean isOperator(char c) { // verifica si un caracter es un operador
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
}




