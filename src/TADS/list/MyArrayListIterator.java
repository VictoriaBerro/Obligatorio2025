package TADS.list;

import java.util.Iterator;


public class MyArrayListIterator<T> implements Iterator<T> {

    private T[] array;
    private int pointerToLastValue;
    private int currentPosition;


    public MyArrayListIterator(T[] array, int pointerToTheLastValue) {
        this.array = array;
        this.pointerToLastValue = pointerToTheLastValue;
        this.currentPosition = 0;
    }


    @Override
    public boolean hasNext() {
        return (currentPosition < pointerToLastValue);
    }

    @Override
    public T next() {
        T valueToReturn = this.array[currentPosition];
        currentPosition++;
        return valueToReturn;
    }

}