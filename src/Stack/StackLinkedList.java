package Stack;

import java.util.EmptyStackException;

public class StackLinkedList<T> implements MyStack<T> {

    private Nodo<T> primero;
    private Nodo<T> ultimo;
    private int size;


    @Override
    public void push(T element) { // agrega al final (el tope de la pila)
        Nodo<T> nuevo = new Nodo<T> (element);
            if (primero == null) {
                // Si la lista está vacía, el nuevo nodo es primero y último
                primero = nuevo;
                ultimo = nuevo;
            } else {
                ultimo.setSiguiente(nuevo); // El último nodo apunta al nuevo nodo
                ultimo = nuevo; // Ahora el nuevo nodo es el último
                nuevo.setSiguiente(null);
            }
            size++;
        }



    @Override
    public void pop() throws EmptyStackException { // elimina el tope (el ultimo)
        if (primero == null) {
            throw new EmptyStackException();
        } else if (this.primero.getSiguiente() == null) {
            this.primero = null;
            this.ultimo = null;
        }else {
            Nodo<T> aux = this.primero;
            while (aux.getSiguiente().getSiguiente() != null) {
                aux = aux.getSiguiente();
            }
            this.ultimo = aux;
            aux.setSiguiente(null);
        }
        size--;
    }

    @Override
    public T top() throws EmptyStackException {// me devuelve el tope
        if (this.primero == null) {
            throw new EmptyStackException();
        }
        return this.ultimo.getValue();
    }

    @Override
    public boolean isEmpty() {
        if (this.primero == null) {
            return true;
        }
        else {
            return false;
        }}


    @Override
    public void makeEmpty() {
        if (this.primero == null) {//si es vacia, devolve nada.
    } else { //garbage collector va ir eliminado los siguientes (pasa solo)
            this.primero = null;
            ultimo = this.primero;
            size = 0;
        }}}

