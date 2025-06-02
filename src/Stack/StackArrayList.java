package Stack;

import java.util.ArrayList;
import java.util.EmptyStackException;

public class StackArrayList<T> implements MyStack<T> {
    private ArrayList<T> stack;

    public StackArrayList() {
        this.stack = new ArrayList<>();
    }

    public ArrayList<T> getStack() {
        return stack;
    }

    public void setStack(ArrayList<T> stack) {
        this.stack = stack;
    }

    @Override
    public void push(T element) {
        stack.add(element); // lo agrega al final (al tope de la pila)
    }

    @Override
    public void pop() throws EmptyStackException {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        stack.remove(stack.size() - 1); // elimkna el tope
    }

    @Override
    public T top() throws EmptyStackException {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return stack.get(stack.size() - 1); // retorna el tope
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty(); //funciona porque stack es una instancia de una clase de Java que ya sabe cómo verificar si está vacía.
    }

    @Override
    public void makeEmpty() {
        stack.clear(); // borra todos los elementos
    }
}
