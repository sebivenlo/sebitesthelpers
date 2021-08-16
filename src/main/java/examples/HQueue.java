package examples;

import java.util.Iterator;

/**
 * Simple Queue. Invariants:
 * <ol>
 * <li> Tail == insertion node. It is the node that receives the next node.</li>
 * </ol>
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 */
public class HQueue<E> implements Queue<E> {

    static class Node<T> {

        T payload;
        Node<T> next;
//        String a,b,c;

        Node() {
        }

        Node( T payload ) {
            this.payload = payload;
        }

        void swap( Node<T> other ) {
            T p = this.payload;
            this.payload = other.payload;
            other.payload = p;

        }

//        @Override
//        public String toString() {
//            return "Node{" + "payload=" + payload + '}';
//        }
    }

    Node<E> unlinkNext( Node<E> n ) {
        Node<E> result = n.next;
        n.next = result.next;
        result.next = null;
        size--;
        return result;
    }

    Node<E> appendAfter( Node<E> current, Node<E> n ) {
        n.next = current.next;
        current.next = n;
        size++;
        return n;
    }

    Node<E> head = new Node<>();
    // point a insertion node.
    Node<E> tail = head;
    long size = 0L;

    @Override
    public void put( E t ) {
        tail = tail.next = new Node<>( t );
        size++;
    }

    @Override
    public E get() {
        E result = head.next.payload;
        // advance the head.
        head = head.next;
        size--;
        return result;
    }

    @Override
    public boolean isEmpty() {
        return head.next == null;
    }

    @Override
    public long size() {
        return size;
    }

    /**
     * Append a node to this queue.
     *
     * @param n node to append
     */
    void appendNode( Node<E> n ) {
        tail.next = n;
        tail = tail.next;
        size++;
    }

    HQueue<E> append( HQueue<E> t ) {
        if ( this.isEmpty() ) {
            this.head.next = t.head.next;
        }
        if ( !t.isEmpty() ) {
            // there is something to append
            this.tail.next = t.head.next;
            this.tail = t.tail;
        }
        this.size += t.size;
        return this;
    }

//    @Override
//    public String toString() {
//        String result = "HQueue{" + "size=" + size + ": ";
//        Node<E> current = head.next;
//        String glue = "";
//        while ( current != null ) {
//            result += glue + Objects.toString( current.payload );
//            glue = "-> ";
//            current = current.next;
//        }
//        result += " }";
//        return result;
//
//    }
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            Node<E> current = HQueue.this.head;

            @Override
            public boolean hasNext() {
                return current.next != null;
            }

            @Override
            public E next() {
                E result = current.next.payload;
                current.next = current.next.next;
                return result;
            }
        };
    }

}
