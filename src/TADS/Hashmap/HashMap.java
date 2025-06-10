package TADS.Hashmap;

import TADS.Hashmap.HashTable;
import TADS.list.linked.MyLinkedListImpl;


import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class HashMap<Key, Value> implements HashTable<Key, Value> {
    private static class Node<Key, Value> {
        Key key;
        Value value;

        Node(Key key, Value value) {
            this.key = key;
            this.value = value;
        }
    }

    private MyLinkedListImpl<Node<Key, Value>>[] table;
    private int capacity;
    private int size;

    public HashMap(int capacity) {
        this.capacity = capacity;
        this.table = new MyLinkedListImpl[capacity];
        this.size = 0;

        for (int i = 0; i < capacity; i++) {
            table[i] = new MyLinkedListImpl<>();
        }
    }

    private int hash(Key key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    @Override
    public void put(Key key, Value value) {
        int index = hash(key);
        MyLinkedListImpl<Node<Key, Value>> bucket = table[index];

        for (int i = 0; i < bucket.getSize(); i++) {
            Node<Key, Value> node = bucket.get(i);
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
        }

        bucket.add(new Node<>(key, value));
        size++;
    }

    @Override
    public Value get(Key key) {
        int index = hash(key);
        MyLinkedListImpl<Node<Key, Value>> bucket = table[index];

        for (int i = 0; i < bucket.getSize(); i++) {
            Node<Key, Value> node = bucket.get(i);
            if (node.key.equals(key)) {
                return node.value;
            }
        }

        return null;
    }

    @Override
    public boolean contains(Key key) {
        return get(key) != null;
    }

    @Override
    public void remove(Key key) {
        int index = hash(key);
        MyLinkedListImpl<Node<Key, Value>> bucket = table[index];

        for (int i = 0; i < bucket.getSize(); i++) {
            Node<Key, Value> node = bucket.get(i);
            if (node.key.equals(key)) {
                bucket.remove(i);
                size--;
                return;
            }
        }
    }

    @Override
    public MyLinkedListImpl<Key> keys() {
        MyLinkedListImpl<Key> keys = new MyLinkedListImpl<>();
        for (MyLinkedListImpl<Node<Key, Value>> bucket : table) {
            for (int i = 0; i < bucket.getSize(); i++) {
                keys.add(bucket.get(i).key);
            }
        }
        return keys;
    }

    @Override
    public MyLinkedListImpl<Value> values() {
        MyLinkedListImpl<Value> values = new MyLinkedListImpl<>();
        for (MyLinkedListImpl<Node<Key, Value>> bucket : table) {
            for (int i = 0; i < bucket.getSize(); i++) {
                values.add(bucket.get(i).value);
            }
        }
        return values;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<Value> iterator() {
        return values().iterator();
    }


    @Override
    public void forEach(Consumer<? super Value> action) {
        for (Value value : this) {
            action.accept(value);
        }
    }

    @Override
    public Spliterator<Value> spliterator() {
        return values().spliterator();
    }
}