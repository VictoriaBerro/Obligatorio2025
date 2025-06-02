package Arbol;

import java.util.List;

public interface MyTree<K, T> {
    T find(K key);
    void insert(K key, T data, K parentKey);
    void delete(K key);

    int size();
    int countLeaf();
    int countCompleteElements();

    List<K> inOrder(); //realiza la recorrida del árbol usando el algoritmo inOrder y devuelve una lista con los elementos visitados.
    List<K> preOrder(); // realiza la recorrida del árbol usando el algoritmo preOrder y devuelve una lista con los elementos visitados.
    List<K> postOrder(); // realiza la recorrida del árbol usando el algoritmo postOrder y devuelve una lista con los elementos visitados.

    void loadPostFijaExpression (String sPostFija); //que lo que realice es tomar la expresión postfija de la cadena de caracteres y genere el árbol aritmético.
}
