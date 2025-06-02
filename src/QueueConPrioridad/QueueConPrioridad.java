package QueueConPrioridad;

public class QueueConPrioridad<T> implements MyQueuePrioridad<T> {
    private NodoPrioridad<T> primero;

    public QueueConPrioridad() {
        primero = null;
    }
// CUANTO MAS GRANDE LA PRIORIDAD EN VALOR, MAS PRIORIDAD
    @Override
    public void enqueueWithPriority(T elemento, int prioridad) {
        NodoPrioridad<T> nuevo = new NodoPrioridad<>(elemento, prioridad);

        if (primero == null || prioridad > primero.getPrioridad()) { // si la lista esta vacia O si la prioridad es mayor que la prioridad del primero
            nuevo.setSiguiente(primero);
            primero = nuevo;
        } else {
            NodoPrioridad<T> actual = primero;

            while (actual.getSiguiente() != null && //mientras no sea el ultimo
                    actual.getSiguiente().getPrioridad() >= prioridad) { //la prioridad del que le sigue es mayor o igual a la nueva
                actual = actual.getSiguiente(); // avanzo al siguiente nodo para seguir buscando dónde insertar

            }

            nuevo.setSiguiente(actual.getSiguiente());// el nuevo nodo apunta al siguiente del actual
            actual.setSiguiente(nuevo); // el actual ahora apunta al nuevo nodo
        }
    }
    @Override
    public void enqueue(T element) {
        enqueueWithPriority(element, 0); //me lo agrega al final
    }

    @Override
    public T dequeue() throws EmptyQueueException {
        if (isEmpty()) {
            throw new EmptyQueueException();
        }

        T dato = primero.getValue();
        primero = primero.getSiguiente();
        return dato;
    }

    @Override
    public boolean isEmpty() {
        return primero == null;
    }
}
