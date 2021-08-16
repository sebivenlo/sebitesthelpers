package examples;

import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author "Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}"
 */
public class SneakyHelper {

    static <T> Queue<T> sortQueue( Comparator<T> comp, Queue<T> q ) {

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
}
