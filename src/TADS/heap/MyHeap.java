package TADS.heap;

public interface MyHeap<T extends Comparable<T>> {

    void insert(T value);

    T getMax();

    T deleteMax();

    int size();


}
