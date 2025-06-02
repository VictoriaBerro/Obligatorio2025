package QueueConPrioridad;

public class NodoPrioridad<T> {
    private T value;
    private int prioridad;
    private NodoPrioridad<T> siguiente;

    public NodoPrioridad(T value, int prioridad) {
        this.value = value;
        this.prioridad = prioridad;
        this.siguiente = null;
    }

    public T getValue() {
        return value;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public NodoPrioridad<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoPrioridad<T> siguiente) {
        this.siguiente = siguiente;
    }
}
