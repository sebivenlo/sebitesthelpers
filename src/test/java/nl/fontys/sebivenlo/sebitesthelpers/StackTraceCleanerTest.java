package nl.fontys.sebivenlo.sebitesthelpers;

import static org.assertj.core.api.Assertions.fail;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class StackTraceCleanerTest {

//    @Disabled( "Think TDD" )
//    @Test
    public void seriousStackTrace() throws Throwable {
        StackTraceCleaner.executedAndRinse( () -> {
            fail( "method seriousStackTrace reached end. You know what to do." );
        } );

    }

}
