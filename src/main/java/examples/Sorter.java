package examples;

/**
 * A sorter sorts it input.
 *
 * The implementation is expected to return the <b>same</b> queue instance.
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 * @param <T> type of elements in queue.
 */
public interface Sorter<T> {

    /**
     * Return the input queue in sorted order, the order being imposed by the
     * {@code Comparator<E>} provide by the constructor.
     *
     * @param q to be sorted
     * @return the queue with the elements in non-descending order as per the comparator.
     */
    Queue<T> sort( Queue<T> q );
}
