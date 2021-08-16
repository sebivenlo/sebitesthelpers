package nl.fontys.sebivenlo.sebitesthelpers;

import java.util.Comparator;
import java.util.Iterator;

/**
 *
 * @author "Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}"
 */
public class SorterAssert {

    final Iterable actual;
    private String msg;

    public SorterAssert( Iterable actual ) {
        this.actual = actual;

    }

    public SorterAssert isNotNull() {
        if ( null == actual ) {
            failWithMessage( "iterable should not be null" );
        }
        return this;
    }

    public static SorterAssert assertThat( Iterable actual ) {
        return new SorterAssert( actual );
    }

    public SorterAssert as( String msg ) {
        this.msg = msg;
        return this;
    }

    public SorterAssert isOrderedAccordingTo( Comparator comp ) {
        isNotNull();
        if ( !isSorted( comp ) ) {
            failWithMessage( msg+" Expecting iterable to be sorted but was not, "
                    + "comparison failed with prev=<%s> and next=<%s>", prev, current );
        }
        return this;
    }

    public SorterAssert hasIterationCount( long expected ) {
        isNotNull();
        this.expectedIterationCount = expected;
        if ( iterationCompleted && ( iterationCount != expectedIterationCount ) ) {
            failWithMessage( msg + " Expecting iterable to allow %d iteration(s) with iterable " + actual.getClass()
                    + " but stopped after %d iteration(s).",
                    expectedIterationCount, iterationCount );
        }
        return this;
    }

    private Object prev;
    private Object current;
    boolean iterationCompleted = false;
    private long iterationCount = 0;
    private long expectedIterationCount = 0;

    private boolean isSorted( Comparator comp ) {
        Iterator itr = actual.iterator();
        if ( !itr.hasNext() ) {
            return true;
        }

        prev = itr.next();
        iterationCount = 1;
        while ( itr.hasNext() ) {
            current = itr.next();
            iterationCount++;
            if ( comp.compare( prev, current ) > 0 ) {
                return false;
            }
            prev = current;
        }
        iterationCompleted = true;
        return true;
    }

    void failWithMessage( String msg, Object... args ) {
        throw new AssertionError( String.format( msg, args ) );
    }
}
