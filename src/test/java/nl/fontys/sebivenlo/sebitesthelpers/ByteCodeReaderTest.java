package nl.fontys.sebivenlo.sebitesthelpers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import org.junit.jupiter.api.Test;

/**
 *
 * @author "Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}"
 */
public class ByteCodeReaderTest {
    //@Disabled("Think TDD")

    @Test
    public void findNaughtSorterFederates() {
        ByteCodeReader br = ByteCodeReader.readCode( examples.NaughtySorter4.class );
        br.federatedClassFiles.forEach( federated -> {
            System.out.println( "NaughtySorter4 federated class = " + federated );
        } );
        assertThat( br.federatedClassFiles ).contains( ByteCodeReader.classToFileNameBase( examples.SneakyHelper.class ) );
//        fail("test method findNaughtSorterFederates ended, you know what to do.");
    }

    //@Disabled("Think TDD")
    @Test
    public void findMyListColaborators() {
        ByteCodeReader br = ByteCodeReader.readCode( examples.MyList.class );
        for ( String federatedClass : br.federatedClassFiles ) {
            System.out.println( "my list federated Class = " + federatedClass );

        }

//        fail( "method findMyListColaborators reached end. You know what to do." );
    }
}
