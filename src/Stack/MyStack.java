package Stack;

public interface MyStack<T> {
    void pop () throws EmptyStackException;
    Object top() throws EmptyStackException;
    void push(T element); // Java no entiende qué es <T> ahí adentro, entiende que quiero declarar un método genérico dentro de una interfaz que ya es genérica. (confuso )
    boolean isEmpty ();
    void makeEmpty();
}