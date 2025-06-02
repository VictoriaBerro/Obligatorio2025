package Queue;



public class QueueConLista<T> implements MyQueue<T> {
    private Nodo<T> primero; // inicio de la cola

    public QueueConLista() {
        this.primero = null;
    }

    public Nodo<T> getPrimero() {
        return primero;
    }

    public void setPrimero(Nodo<T> primero) {
        this.primero = primero;
    }

    //Si agrego un nuevo elemento con enqueue, ese elemento debe quedar al final de la cola.

    @Override
    public void enqueue(T element) {
        Nodo<T> nuevo = new Nodo<>(element);

        if (primero == null) {
            primero = nuevo; // Si la cola está vacía, el nuevo es el primero
        } else {
            Nodo<T> actual = primero;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo); // Lo agrego al final real
        }
    }

    @Override
    public T dequeue() throws EmptyQueueException {
        if (primero == null) {
            throw new EmptyQueueException(); //no puedo sacar nada
        }

        T valor = primero.getValue();
        primero = primero.getSiguiente(); //el primero pasa a ser el siguiente
        return valor;
    }

    @Override
    public boolean isEmpty() {
        return primero == null;
    }
}
