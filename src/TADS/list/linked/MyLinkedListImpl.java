package TADS.list.linked;


import TADS.exceptions.EmptyQueue;
import TADS.exceptions.EmptyStack;
import TADS.exceptions.ListOutOfIndex;
import TADS.list.MyList;
import TADS.queue.MyQueue;
import TADS.stack.MyStack;

import java.util.Iterator;


public class MyLinkedListImpl<T> implements MyList<T>, MyStack<T>, MyQueue<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;


    public MyLinkedListImpl() {
        this.head = null;
        this.tail = null;
        size = 0;
    }

    @Override
    public void add(T value, int index) throws ListOutOfIndex {
        if (index == 0){
            addFirst(value);
        } else if (index == (size-1)){
            addLast(value);
        }else{
            Node<T> previous = nodeAtIndex(index-1);
            Node<T> temp = new Node<>( value);
            temp.setNext(previous.getNext());
            previous.setNext(temp);
            size++;
        }

    }

    @Override
    public void add(T value) {
        addLast(value);
    }

    @Override
    public void addFirst(T value) {
        Node<T> newNode = new Node<T>(value);

        if (head == null) {
            head = newNode;
            size++;
            tail = newNode;
        } else {
            newNode.setNext(head);
            head = newNode;
            size++;
        }
    }

    @Override
    public T remove(int index) throws ListOutOfIndex {

        T toReturn = null;
        if (index == size-1){                                //En este caso se elimina el ultimo indirectamente
            toReturn =  removeLast();
        } else if (index == 0){
            toReturn = head.getValue();
            Node<T> newHead = head.getNext();
            head.setNext(null);
            head = newHead;
            size--;
        } else {
            Node<T> temp = nodeAtIndex(index - 1);     //Busca el anterior del que quiero elminar
            Node<T> nodeToReturn = temp.getNext();                //Encuentro el que quiero eliminar
            toReturn = nodeToReturn.getValue();
            temp.setNext(nodeToReturn.getNext());         //Le doy al nodo anterior el siguiente del que voy  eliminar
            nodeToReturn.setNext(null);
            size--;
        }
        return toReturn;
    }

    @Override
    public T removeLast() {
        T toReturn = null;

        if (size > 0) {
            toReturn = tail.getValue();
            if (size == 1) {                                 //Si hay 1 solo elemento lo elimina a ese
                head = null;
                tail = null;
                size--;
            } else {
                Node<T> temp = null;
                try {
                    temp = nodeAtIndex(size-2);
                    temp.setNext(null);
                    tail = temp;
                    size--;                     //Va al penultimo nodo y elimina, sabiendo que no hay forma que de error
                } catch (ListOutOfIndex listOutOfIndex) {
                    listOutOfIndex.printStackTrace();
                }
            }
        }
        return toReturn;
    }

    @Override
    public T removeValue(T value) {
        Node<T> previous = null;
        Node<T> temp = head;
        for(int i = 0; i < size; i++){
            if (value.equals(temp.getValue())) {
                if(i == 0) {
                    try {
                        return remove(0);
                    } catch (ListOutOfIndex listOutOfIndex) {
                        listOutOfIndex.printStackTrace();
                    }
                } else {
                    if(size > 2) {
                        previous.setNext(temp.getNext());
                    } else{
                        previous.setNext(null);
                        tail = previous;
                    }
                    size--;
                }
                return temp.getValue();
            }
            previous = temp;
            temp = temp.getNext();
        }
        return null;
    }

    @Override
    public T get(int index) throws ListOutOfIndex {
        return nodeAtIndex(index).getValue();
    }

    @Override
    public T getValue(T valueToSearch) {
        T toReturn = null;
        Node<T> temp = head;
        for (int i = 0; i < size; i++){
            if (valueToSearch.equals(temp.getValue())){
                toReturn = temp.getValue();
                break;
            }
            temp = temp.getNext();
        }
        return toReturn;
    }

    public void set(int index, T value) {
        if (index < 0 || index >= this.getSize()) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
        }

        Node<T> actual = this.head; // asumimos que tu lista tiene el atributo `head`

        for (int i = 0; i < index; i++) {
            actual = actual.getNext();
        }

        actual.setValue(value);
    }



    @Override
    public boolean contains (T valor){          //Devuelve si el valor esta en la lista
        boolean found = false;

        Node<T> temp = head;

        while (temp != null) {                           //Va recorriendo y comparando el valor con el de cada nodo. Si lo encuentra corta
            if (temp.getValue().equals(valor)){
                found = true;
                break;
            } else {
                temp = temp.getNext();
            }
        }
        return found;
    }


    @Override
    public Iterator<T> iterator() {
        return new MyLinkedListIterator<T>(head);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public T[] toArray() {
        T[] array = (T[]) new Object[this.size];
        Node<T> temp = head;
        for (int i=0; i<this.size; i++){
            array[i] = temp.getValue();
            temp = temp.getNext();
        }
        return array;

    }

    @Override
    public String toString() {
        return visualize(" ");
    }


    @Override
    public T pop() throws EmptyStack {
        T value = removeLast();
        if (value == null){
            throw new EmptyStack();
        }
        return value;
    }

    @Override
    public T top() throws EmptyStack {

        if (size == 0){
            throw new EmptyStack();
        } else {
            return tail.getValue();
        }
    }

    @Override
    public void push(T value) {
        addLast(value);
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void makeEmpty() {

        Node<T> temp = this.head;
        Node<T> nextNode = temp.getNext();

        while(nextNode != null){
            temp.setNext(null);
            temp = nextNode;
            nextNode = temp.getNext();
        }
        head = null;
        tail = null;
        size = 0;
    }


    @Override
    public void enqueue(T value) {
        addLast(value);
    }

    @Override
    public void enqueueWithPriority(T value) {
        addInOrder(value);
    }

    @Override
    public T dequeue() throws EmptyQueue {
        Node<T> toReturn = this.head;

        if(size == 0){
            throw new EmptyQueue();
        } else{
            try {
                remove(0);
            } catch (ListOutOfIndex listOutOfIndex) {
                listOutOfIndex.printStackTrace();
            }
        }
        return toReturn.getValue();
    }

    private void addInOrder(T value){
        if (size == 0){
            addFirst(value);
        } else {
            if ( value instanceof Comparable) {
                if (((Comparable) head.getValue()).compareTo(value) < 0) {
                    addFirst(value);
                } else {
                    Node<T> temp = head.getNext();
                    Node<T> previous = head;
                    boolean added = false;

                    while (temp != null) {
                        if (((Comparable) temp.getValue()).compareTo(value) < 0) {
                            Node<T> newNode = new Node<>(value);
                            previous.setNext(newNode);
                            newNode.setNext(temp);
                            added = true;
                            break;
                        } else {
                            previous = temp;
                            temp = temp.getNext();
                        }
                    }

                    if (added) {
                        size++;
                    } else {
                        addLast(value);
                    }
                }
            }
            else{
                addLast(value);
            }
        }
    }

    private  Node<T> nodeAtIndex(int index) throws ListOutOfIndex {
        Node<T> temp = null;
        if (index >= size || index < 0) {          //Ceckea que exista la pos
            throw  new ListOutOfIndex(size);
        } else if (index == (size-1)){                //Checkea si es el ultimo
            temp = tail;
        } else {                                        //Recorre la cantidad de veces necesarias y devuelve el ultimo
            temp = head;
            for (int i = 0; i < index ; i++){
                temp = temp.getNext();
            }
        }
        return temp;
    }

    private void addLast(T valor){                         //Agrega al final
        if (head == null){                   //Si la lista es vacia agrega al principio
            addFirst(valor);
        } else {
            Node<T> newNode = new Node<T>(valor);   //Si la lista no es vacia, solamente agrega seteando el siguiente del
            tail.setNext(newNode);     //ultimo y renueva el ultimo
            tail = newNode;
            size++;
        }
    }

    private String visualize(String sep){

        Node<T> temp = head;
        String str = "";

        while(temp != null){
            str = str + (temp.getValue() + sep);
            temp = temp.getNext();
        }
        str = str + "\n";
        return str;
    }

}
