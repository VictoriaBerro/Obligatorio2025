package TADS.heap;


import TADS.exceptions.HeapOverflow;

public class MyHeapImpl<T extends Comparable<T>> implements MyHeap<T> {

    private T[] values;
    private int pointerLastValue;



    public MyHeapImpl(int size){
        this.values = (T[]) new Comparable[size];
        this.pointerLastValue = 0;
    }
    public MyHeapImpl(T[] values) {
        this.values = values;
        this.pointerLastValue = 0;
        this.constructor();
    }

    private void constructor() {
        for (int i=0; i<values.length; i++){
            insert(values[i]);
        }
    }

    @Override
    public void insert(T value) {
        if (this.pointerLastValue > this.values.length-1){
            throw new HeapOverflow("");
        }

        this.values[pointerLastValue] = value;
        int valuePosition = pointerLastValue;
        pointerLastValue++;

        while (valuePosition != 0 && value.compareTo(getFather(valuePosition)) > 0) {

            T tempValue = getFather(valuePosition);

            this.values[getFatherPosition(valuePosition)] = value;
            this.values[valuePosition] = tempValue;

            valuePosition = getFatherPosition(valuePosition);
        }

    }

    @Override
    public T getMax() {
        return this.values[0];
    }

    @Override
    public T deleteMax() {

        if(size() == 1){
            pointerLastValue--;
            return this.values[0];
        } else if (size() == 0){
            return null;
        }


        T maxValue = this.values[0];
        int position = 0;
        int newPosition = position;
        pointerLastValue--;



        this.values[0] = this.values[pointerLastValue];
        this.values[pointerLastValue] = null;

        T tempValue = this.values[0];


        while(true) {

            if (getChildPosition(position) < pointerLastValue && tempValue.compareTo(getFirstChild(position)) < 0){
                newPosition = getChildPosition(position);
                tempValue = values[newPosition];
            }
            if (getChildPosition(position)+1 < pointerLastValue && tempValue.compareTo(getSecondChild(position)) < 0){
                newPosition = getChildPosition(position)+1;
            }

            if (position != newPosition ){
                tempValue = values[position];
                values[position] = values[newPosition];
                values[newPosition] = tempValue;
                position = newPosition;
            } else{
                break;
            }
        }


        return maxValue;
    }

    @Override
    public int size() {
        return pointerLastValue;
    }

    @Override
    public String toString() {
        String str = "";
        int nivel = 0;
        int posicion = 0;
        double veces = 0;
        int tabs = (int) (Math.log10(size())/Math.log10(2));
        int mayortab = tabs;
        while(posicion < pointerLastValue){
            tabs = (int) (Math.log10(size())/Math.log10(2));
            tabs = (int) (tabs - nivel);
            for (int i = 0; i < tabs; i++ ) {
                str = str + "\t";
                for (int j = 0; j < mayortab-tabs; j++)
                    str = str + "  ";
            }

            veces = Math.pow(2,nivel);
            veces = veces + posicion;
            while(posicion < veces && posicion < pointerLastValue ){
                str = str + this.values[posicion] + "\t";
                posicion++;
            }

            nivel++;
            str = str + "\n";
        }


        return str;
    }



    private int getFatherPosition(int childPosition){
        return ((childPosition-1)/2);
    }

    private T getFather(int childPosition){
        return this.values[(childPosition-1)/2];
    }

    private int getChildPosition(int fatherPosition){
        return (2*fatherPosition +1);
    }

    private T getFirstChild(int fatherPosition){
        return this.values[2*fatherPosition +1];
    }

    private T getSecondChild(int fatherPosition){
        return this.values[2*fatherPosition +2];
    }
}

