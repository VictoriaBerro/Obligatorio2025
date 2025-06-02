package LinkedList;

public class LinkedList implements Lista {
    private Nodo primero;
    private Nodo ultimo;
    private int size;

    public LinkedList() {
        this.primero = null;
        this.size = 0;
    }


//EJERCICIO 2
    public boolean contiene(Object value) {
        Nodo actual = primero;
        while (actual != null) {
            if (actual.value.equals(value)) {
                return true; // Encontro el elemento
            }
            actual = actual.siguiente;
        }
        return false; // No encontro el elemento
    }


//EJERCICIO 3
    // Paea agregar al principio de la sita
    public void addFirst(Object value) {
        Nodo nuevo = new Nodo(value);
        if (primero == null) {
            // Si la lista está vacía, el nuevo nodo es primero y último
            primero = nuevo;
            ultimo = nuevo;
        } else {
            nuevo.siguiente = primero; // El nuevo nodo apunta al antiguo primero
            primero = nuevo; // Ahora el nuevo nodo es el primero
        }
        size++;
    }

//EJERCICIO 3
    // Para agregar al final de la lista
    public void addLast(Object value) {
        Nodo nuevo = new Nodo(value);
        if (primero == null) {
            // Si la lista está vacía, el nuevo nodo es primero y último
            primero = nuevo;
            ultimo = nuevo;
        } else {
            ultimo.siguiente = nuevo; // El último nodo apunta al nuevo nodo
            ultimo = nuevo; // Ahora el nuevo nodo es el último
        }
        size++;
    }

//EJERCICIO 7
    public void agregarOrdenado(Object valor) {
        if (!(valor instanceof Comparable)) {
            throw new IllegalArgumentException("El objeto debe implementar Comparable.");
        }

        Nodo nuevo = new Nodo(valor);

        if (primero == null || ((Comparable) valor).compareTo(primero.getValue()) <= 0) {
            nuevo.setSiguiente(primero);
            primero = nuevo;
        } else {
            Nodo actual = primero;
            while (actual.getSiguiente() != null &&
                    ((Comparable) valor).compareTo(actual.getSiguiente().getValue()) > 0) {
                actual = actual.getSiguiente();
            }

            nuevo.setSiguiente(actual.getSiguiente());
            actual.setSiguiente(nuevo);
        }}


    @Override
    public void add(Object value) {
        Nodo nuevo = new Nodo(value);
        if (primero == null) {
            primero = nuevo;
        } else {
            Nodo actual = primero;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
        size++;
    }

    @Override
    public void remove(int position) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango.");
        }

        if (position == 0) {
            primero = primero.siguiente;
        } else {
            Nodo actual = primero;
            for (int i = 0; i < position - 1; i++) {
                actual = actual.siguiente;
            }
            actual.siguiente = actual.siguiente.siguiente;
        }
        size--;
    }

    @Override
    public Object get(int position) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango.");
        }

        Nodo actual = primero;
        for (int i = 0; i < position; i++) {
            actual = actual.siguiente;
        }
        return actual.value;
    }

}
