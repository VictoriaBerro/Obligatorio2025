package Stack;

public class Nodo<T>{
    private T value;
    private Nodo<T> siguiente;

    public Nodo(T value, Nodo<T> siguiente) {
        this.value = value;
        this.siguiente = siguiente;
    }

    public Nodo(T value) {
        this.value = value;
    }

    public Nodo(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }
}


