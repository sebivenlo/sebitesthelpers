package examples;

import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 * @param <T> type of elements in queue.
 */
public class NaughtySorter3<T> implements Sorter<T> {

    private final Comparator<T> comp;

    /**
     * Create a sorter that will impose order by the given comparator.
     *
     * @param comp comparator to use.
     */
    public NaughtySorter3( Comparator<T> comp ) {
        this.comp = comp;
    }

    @Override
    public Queue<T> sort( Queue<T> q ) {
        Sorter<T> sorter = new Sorter<>() {
            @Override
            public Queue<T> sort( Queue<T> q ) {
                boolean empty = q.isEmpty();
                T[] a = (T[]) new Object[ (int) q.size() ];
                int i = 0;
                while ( !q.isEmpty() ) {
                    a[ i++ ] = q.get();
                }
                Arrays.sort( a, comp );
                for ( T t : a ) {
                    q.put( t );
                }
                return q;
            }

        };
        return sorter.sort( q );
    }

}
