package examples;

import java.util.Iterator;
import java.util.LinkedList;
//import sortingservice.Queue;

/**
 * Naughty queue. Fakes a linked list with a node class, but uses it to link to 
 * an actual linkedlist.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
class NaughtyQueue2<E> implements Queue<E> {


    static class Node<T> {

        LinkedList<T> payload= new LinkedList<>();
    }

    Node<E> head= new Node<>();

    @Override
    public void put( E t ) {
        head.payload.add( t );
    }

    @Override
    public E get() {
        return head.payload.remove();
    }

    @Override
    public boolean isEmpty() {
        return head.payload.isEmpty();
    }

    @Override
    public long size() {
        return head.payload.size();
    }

    @Override
    public Iterator<E> iterator() {
        return head.payload.iterator();
    }
    
    void orderElements(java.util.Comparator<E> c) {
        head.payload.sort( c );
    }
}
