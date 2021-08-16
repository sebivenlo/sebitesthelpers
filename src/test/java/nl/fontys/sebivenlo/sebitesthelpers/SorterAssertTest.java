package nl.fontys.sebivenlo.sebitesthelpers;

import java.util.Comparator;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class SorterAssertTest {

    //@Disabled("Think TDD")
    @Test
    public void testSorted() {

        List<String> l = List.of( "Aap", "Mies", "Noot" );
        Iterable<String> i = (Iterable<String>) l;
        Comparator<String> comp = ( a, b ) -> a.compareTo( b );
        SorterAssert.assertThat( i ).isNotNull().isOrderedAccordingTo( comp );
//        fail( "method testSorted reached end. You know what to do." );
    }

    @Test
    public void testNotSorted() {

        List<String> l = List.of( "Aap", "Noot", "Mies" );
        Throwable t = null;
        Comparator<String> comp = ( a, b ) -> a.compareTo( b );
        try {
            SorterAssert.assertThat( l ).isOrderedAccordingTo( comp );
            throw new Throwable( "failed to throw exception" );
        } catch ( Throwable e ) {
            t = e;
        }

        if ( !AssertionError.class.isInstance( t ) ) {
            throw new AssertionError( "assertion error not thrown, instead caught " + t.toString() );
        }
//        fail( "method testSorted reached end. You know what to do." );
    }

    @Test
    public void testIterationCount() {

        List<String> l = List.of( "Aap", "Mies", "Noot" );
        Throwable t = null;
        Comparator<String> comp = ( a, b ) -> a.compareTo( b );
        try {
            SorterAssert.assertThat( l ).isOrderedAccordingTo( comp ).hasIterationCount( 4 );
            throw new Throwable( "failed to throw exception count 4" );
        } catch ( Throwable e ) {
            t = e;
        }

        if ( !AssertionError.class.isInstance( t ) ) {
            throw new AssertionError( "assertion error not thrown, instead caught " + t.toString() );
        }
//        fail( "method testSorted reached end. You know what to do." );
    }

}
