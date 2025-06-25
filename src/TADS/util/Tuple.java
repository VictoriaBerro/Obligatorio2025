package TADS.util;

public class Tuple<A, B extends Comparable<B>> implements Comparable<Tuple<A, B>> {
    private A first;
    private B second;

    public Tuple(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    @Override
    public int compareTo(Tuple<A, B> o) {
        return o.second.compareTo(this.second); // max-heap: mayor primero
    }
}
