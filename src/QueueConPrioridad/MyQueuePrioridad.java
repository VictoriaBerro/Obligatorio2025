package QueueConPrioridad;

import Ejercicio1.MyQueue;

public interface MyQueuePrioridad<T>  extends MyQueue<T> {

    void enqueueWithPriority(T element, int prioridad); // Inserta el elemento en orden de prioridad (de mayor a menor)

}