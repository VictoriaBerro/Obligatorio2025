package TADS.list;

import TADS.exceptions.ListOutOfIndex;

import java.util.Iterator;

public class MyArrayListImpl<T> implements MyList<T> {

    private T[] array;
    private int pointerToLastValue;

    public MyArrayListImpl(int size) {
        this.array = (T[]) new Object[size];
        this.pointerToLastValue = 0;
    }


    @Override
    public void add(T value, int index) throws ListOutOfIndex {
        if (index > pointerToLastValue){
            throw new ListOutOfIndex(pointerToLastValue);
        } else{
            if (pointerToLastValue >= this.array.length) {
                incrementArrayLength();
            }
            if (index == 0){
                addFirst(value);
            } else if (index == pointerToLastValue) {
                this.array[pointerToLastValue] = value;
                pointerToLastValue++;
            } else {
                shiftRight(index);
                this.array[index] = value;
                pointerToLastValue++;
            }
        }
    }

    @Override
    public void add(T valor) {
        if (pointerToLastValue >= this.array.length) {
            incrementArrayLength();
        }
        this.array[pointerToLastValue] = valor;
        pointerToLastValue++;
    }

    @Override
    public void addFirst(T valor) {
        if (pointerToLastValue >= this.array.length) {
            incrementArrayLength();
        }
        shiftRight(0);
        this.array[0] = valor;
        pointerToLastValue++;
    }

    @Override
    public boolean contains(T value) {
        boolean toReturn = false;
        for (int i=0; i<pointerToLastValue; i++){
            if (value.equals(this.array[i])){
                toReturn = true;
                break;
            }
        }
        return toReturn;
    }

    @Override
    public T remove(int index) throws ListOutOfIndex {
        if (index >= pointerToLastValue){
            throw new ListOutOfIndex(pointerToLastValue);
        }
        T toReturn = this.array[index];
        shiftLeft(index);
        this.array[pointerToLastValue-1] = null;
        pointerToLastValue--;
        return toReturn;
    }

    @Override
    public T removeLast() {
        T toReturn = null;
        try {
            if (this.getSize()>0) {
                toReturn = remove(pointerToLastValue - 1);
            }
        } catch (ListOutOfIndex listOutOfIndex) {
            listOutOfIndex.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public T removeValue(T value) {
        T toReturn = null;

        for (int i=0; i<pointerToLastValue; i++){
            if(value.equals(this.array[i])){
                toReturn = this.array[i];
                try {
                    remove(i);
                } catch (ListOutOfIndex listOutOfIndex) {
                    listOutOfIndex.printStackTrace();
                }
                break;
            }
        }

        return toReturn;
    }

    @Override
    public T get(int index) throws ListOutOfIndex {
        if (index >= pointerToLastValue){
            throw new ListOutOfIndex(pointerToLastValue);
        }
        return this.array[index];
    }

    @Override
    public T getValue(T valueToSearch) {
        T toReturn = null;
        for (int i=0; i<pointerToLastValue; i++){
            if (valueToSearch.equals(this.array[i])){
                toReturn = this.array[i];
                break;
            }
        }
        return toReturn;
    }

    @Override
    public int getSize() {
        return pointerToLastValue;
    }

    @Override
    public T[] toArray() {
        T[] array = (T[]) new Object[pointerToLastValue];
        for (int i=0; i<pointerToLastValue; i++){
            array[i] = this.array[i];
        }
        return array;
    }

    @Override
    public Iterator<T> iterator() {
        return new MyArrayListIterator<>(this.array, this.pointerToLastValue);
    }



    private void shiftRight(int initialPosition){
        for (int i = pointerToLastValue; i>initialPosition; i--){
            this.array[i] = this.array[i-1];
        }
    }

    private void shiftLeft(int endPosition) {
        for (int i = endPosition; i<pointerToLastValue-1; i++){
            this.array[i] = this.array[i+1];
        }
    }

    private void incrementArrayLength() {
        T[] newArray = (T[]) new Object[(this.array.length) * 2];
        System.arraycopy(this.array, 0, newArray, 0, this.array.length);
        this.array = newArray;
    }

}