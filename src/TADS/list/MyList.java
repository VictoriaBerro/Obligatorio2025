package TADS.list;

import TADS.exceptions.ListOutOfIndex;

/**
 * Interface for a list.
 * @param <T> Type of the elements in the list.
 */
public interface MyList<T> extends Iterable<T> {

  void add (T value, int index) throws ListOutOfIndex;
  void add (T value);
  void addFirst (T value);

  boolean contains(T value);

  T remove (int index) throws ListOutOfIndex;
  T removeLast ();
  T removeValue (T value);

  T get (int index) throws ListOutOfIndex;
  T getValue (T valueToSearch);

  int getSize();

  T[] toArray();

}
