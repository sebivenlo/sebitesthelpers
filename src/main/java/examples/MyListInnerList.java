package examples;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Test business code.
 *
 * @author Pieter van den Hombergh {@code p.vandehombergh@fontysvenlo.org}
 */
public class MyListInnerList<T> {

    Node<T> head = new Node<>();

    class Node<T> {

        List<T> l = new ArrayList<>();
    }

    // triggers java/util regex
    Iterator<T> iterator() {
        return new Iterator<T>() {
            Iterator<T> wrapped = MyListInnerList.this.head.l.iterator();

            @Override
            public boolean hasNext() {
                return wrapped.hasNext();
            }

            @Override
            public T next() {
                return wrapped.next();
            }
        };
    }

    void m() {
        Iterator<T> iterator = this.iterator();
        if ( iterator != null ) {
        }
    }

}
