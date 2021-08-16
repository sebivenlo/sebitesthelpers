package examples;

import java.util.Comparator;

/**
 *
 * @author "Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}"
 */
public class NaughtySorter4<T> implements Sorter<T> {

    private final Comparator<T> comp;

    public NaughtySorter4( Comparator<T> comp ) {
        this.comp = comp;
    }

    @Override
    public Queue<T> sort( Queue<T> q ) {
        return SneakyHelper.sortQueue( comp, q );
    }

}
