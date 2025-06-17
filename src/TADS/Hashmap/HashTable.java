package TADS.Hashmap;

import TADS.list.linked.MyLinkedListImpl;

public interface HashTable<Key, Value>  extends Iterable<Value>{
    void put(Key key, Value value);
    Value get(Key key);
    boolean contains(Key key);
    void remove(Key key);
    MyLinkedListImpl<Key> keys();
    MyLinkedListImpl<Value> values();
    int size();
}
