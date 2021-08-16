package examples;

/**
 * Simple FIFO queue.
 *
 * <p>
 * As the size returns a long this interface can be implemented by a queue
 * having more elements than can be indexed in an array. This queue is used as
 * an interface to input and output data to the sorters. In particular,
 * queue.put(E t) is used to provide data to the sorter and E queue.get() is
 * used to get the data (in the order required by the comparator).</p>
 *
 * <p>
 * This queue extends the {@link java.lang.Iterable Iterable} interface too,
 * because it makes checking for ordered-ness particularly easy with assertJ.
 * </p>
 *
 * 
 * 
 * <p>
 * The sorter may choose the best queue implementation for its sort strategy.
 * Note that singly-linked queues can be more space efficient than doubly linked
 * queues.</p>
 *
 * @author Pieter van den Hombergh {@code pieter.van.den.hombergh@gmail.com}
 * @param <E> elements in queue.
 */
public interface Queue<E> extends Iterable<E> {

    /**
     * Add element to queue.
     *
     * @param t element to add
     *
     */
    void put( E t );

    /**
     * Remove element of this queue and return it.
     *
     * @return the first element in this queue
     */
    E get();

    /**
     * Does this queue contain any elements.
     *
     * @return empty state, true is empty.
     */
    boolean isEmpty();

    /**
     * The number of elements contained in this queue.
     *
     * @return the number of elements.
     */
    long size();
}
