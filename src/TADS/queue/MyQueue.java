package TADS.queue;

import TADS.exceptions.EmptyQueue;

public interface MyQueue<T> extends Iterable<T>{

    void enqueue (T value);

    /**
     * Requires T to be Comparable to work. Otherwise, adds at last.
     */
    void enqueueWithPriority (T value);

    T dequeue () throws EmptyQueue;

    boolean isEmpty();

    int getSize();


}
