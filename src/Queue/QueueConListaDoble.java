package Queue;
import Util.EmptyQueueException;


public class QueueConListaDoble<T> implements MyQueue<T> {
    private Nodo<T> primero;
    private Nodo<T> ultimo;

    public QueueConListaDoble() {
        primero = null;
        ultimo = null;
    }

    public Nodo<T> getPrimero() {
        return primero;
    }

    public void setPrimero(Nodo<T> primero) {
        this.primero = primero;
    }

    public Nodo<T> getUltimo() {
        return ultimo;
    }

    public void setUltimo(Nodo<T> ultimo) {
        this.ultimo = ultimo;
    }

    @Override
    public void enqueue(T element) {
        Nodo<T> nuevo = new Nodo<>(element);

        if (isEmpty()) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.setSiguiente(nuevo);// al ultimo que tenía antes, va a estar antes del nuevo
            ultimo = nuevo; //porque se agrega al final
        }
    }

    @Override
    public T dequeue() throws EmptyQueueException {
        if (isEmpty()) {
            throw new EmptyQueueException();
        }

        T dato = primero.getValue();
        primero = primero.getSiguiente();

        if (primero == null) {
            ultimo = null; // si quedó vacía, no tengo ni primero ni ultimo
        }

        return dato;
    }

    @Override
    public boolean isEmpty() {
        return primero == null;
    }
}
