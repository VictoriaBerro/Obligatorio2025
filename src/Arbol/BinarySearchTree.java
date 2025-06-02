package Arbol;

import java.util.ArrayList;
import java.util.List;

public class BinarySearchTree<K extends Comparable<K>, T> implements MyBinarySearchTree<K, T> {
    private NodeBST<K, T> root;

    @Override
    public T find(K key) {
        return findRecursive(root, key);
    }


    private T findRecursive(NodeBST<K, T> node, K key) {    // compara las claves (ABB)
        if (node == null) return null;
        int cmp = key.compareTo(node.getKey());
        if (cmp == 0) return node.getData();
        if (cmp < 0) return findRecursive(node.getLeftChild(), key);
        return findRecursive(node.getRightChild(), key);
    }

    @Override
    public void insert(K key, T data) {
        root = insertRecursive(root, key, data);
    }


    private NodeBST<K, T> insertRecursive(NodeBST<K, T> node, K key, T data) { // Inserta respetando las reglas de un ABB (menores a la izquierda, mayores a la derecha)
        if (node == null) return new NodeBST<>(key, data);
        int cmp = key.compareTo(node.getKey());
        if (cmp < 0) node.setLeftChild(insertRecursive(node.getLeftChild(), key, data));
        else if (cmp > 0) node.setRightChild(insertRecursive(node.getRightChild(), key, data));
        else node.setData(data); // actualiza el dato si la clave ya existe
        return node;
    }

    @Override
    public void delete(K key) {
        root = deleteRecursive(root, key);
    }


    private NodeBST<K, T> deleteRecursive(NodeBST<K, T> node, K key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.getKey());
        if (cmp < 0) node.setLeftChild(deleteRecursive(node.getLeftChild(), key));
        else if (cmp > 0) node.setRightChild(deleteRecursive(node.getRightChild(), key));
        else {
            if (node.getLeftChild() == null) return node.getRightChild();
            if (node.getRightChild() == null) return node.getLeftChild();
            NodeBST<K, T> min = findMin(node.getRightChild());
            node.setKey(min.getKey());
            node.setData(min.getData());
            node.setRightChild(deleteRecursive(node.getRightChild(), min.getKey()));
        }
        return node;
    }


    private NodeBST<K, T> findMin(NodeBST<K, T> node) {    // Encuentra el nodo con la clave mas chica (más a la izquierda)
        while (node.getLeftChild() != null) node = node.getLeftChild();
        return node;
    }

    @Override
    public List<K> inOrder() {
        List<K> result = new ArrayList<>();
        inOrderRecursive(root, result);
        return result;
    }


    private void inOrderRecursive(NodeBST<K, T> node, List<K> result) {    // Recorrido InOrder (izquierda, raíz, derecha)
        if (node != null) {
            inOrderRecursive(node.getLeftChild(), result);
            result.add(node.getKey());
            inOrderRecursive(node.getRightChild(), result);
        }
    }

    @Override
    public List<K> preOrder() {
        List<K> result = new ArrayList<>();
        preOrderRecursive(root, result);
        return result;
    }


    private void preOrderRecursive(NodeBST<K, T> node, List<K> result) {    // Recorrido PreOrder (raíz, izquierda, derecha)
        if (node != null) {
            result.add(node.getKey());
            preOrderRecursive(node.getLeftChild(), result);
            preOrderRecursive(node.getRightChild(), result);
        }
    }

    @Override
    public List<K> postOrder() {
        List<K> result = new ArrayList<>();
        postOrderRecursive(root, result);
        return result;
    }


    private void postOrderRecursive(NodeBST<K, T> node, List<K> result) {    // Recorrido PostOrder (izquierda, derecha, raíz)
        if (node != null) {
            postOrderRecursive(node.getLeftChild(), result);
            postOrderRecursive(node.getRightChild(), result);
            result.add(node.getKey());
        }
    }
}
