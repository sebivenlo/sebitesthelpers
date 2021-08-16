package nl.fontys.sebivenlo.sebitesthelpers;

import static nl.fontys.sebivenlo.sebitesthelpers.NaughtyCodeChecker.NO_ARRAYS;
import static nl.fontys.sebivenlo.sebitesthelpers.NaughtyCodeChecker.assertNotNaughty;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author "Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}"
 */
public class NaughtyCodeCheckerTest {

    //@Disabled("Think TDD")
    @Test
    public void nSneakyHelper() {
        assertThatThrownBy( ()
                -> {
            assertNotNaughty( "", examples.SneakyHelper.class, NO_ARRAYS );
        }
        ).isExactlyInstanceOf( AssertionError.class ).hasMessageContaining( "aastore" );
//        fail( "method nSneakyHelper reached end. You know what to do." );
    }

    //@Disabled("Think TDD")
    @org.junit.jupiter.api.Test
    public void naugthyFederate() {
        assertThatThrownBy( ()
                -> {
            assertNotNaughty( "", examples.NaughtySorter4.class, NO_ARRAYS );
        } ).isExactlyInstanceOf( AssertionError.class )
                .hasMessageContainingAll( "aastore","evidence" );
//        fail( "method naugthyFederate reached end. You know what to do." );
    }

    //@Disabled("Think TDD")
    @org.junit.jupiter.api.Test
    public void findsFederate() {
        NaughtyCodeChecker checker = new NaughtyCodeChecker( NO_ARRAYS, examples.NaughtySorter4.class );
        System.out.println( "===== start readerCache  ==== " );
        for ( String clz : ByteCodeReader.getReaderCache().keySet() ) {
            System.out.println( "cached clz = " + clz );
        }
        System.out.println( "===== end readerCache  ==== " );

        NaughtyCodeChecker check = checker.check();
        for ( String clz : check.getChecked() ) {
            System.out.println( "checked class = " + clz );
        }
        assertThat( check.getChecked() ).contains( ByteCodeReader.classToFileNameBase( examples.SneakyHelper.class ) );
//        fail( "method findsFederate reached end. You know what to do." );
    }
}
