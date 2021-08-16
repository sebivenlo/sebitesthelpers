package examples;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This implementation shows what a well versed java programmer might try.
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 * @param <T> type of elements in the queueu.
 */
public class NaughtySorter<T> implements Sorter<T> {

    private final Comparator<T> comp;

    /**
     * Create a sorter that will impose order by the given comparator.
     *
     * @param comp to use.
     */
    public NaughtySorter( Comparator<T> comp ) {
        this.comp = comp;
    }

    @Override
    public Queue<T> sort( Queue<T> q ) {
        boolean empty = q.isEmpty();
        List<T> list = new ArrayList<T>( (int) q.size() );
        while ( !q.isEmpty() ) {
            list.add( q.get() );
        }
        list.sort( comp );
        // swap last first
//        if (!empty) {
//            list.add(list.remove(0));
//        }
        for ( T t : list ) {
            q.put( t );
        }
        return q;
    }

}
