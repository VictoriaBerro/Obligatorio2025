package LinkedList;

public class Nodo {
    Object value;
    Nodo siguiente;

    public Nodo(Object value) {
        this.value = value;
        this.siguiente = null;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Nodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }
}
