package nl.fontys.sebivenlo.sebitesthelpers;

import examples.HQueue;
import examples.Queue;
import static nl.fontys.sebivenlo.sebitesthelpers.NaughtyCodeChecker.*;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author hom
 */
public class HQueueTest {


    @Test
    public void hqueueIsFine() {
        Queue<Integer> l = new HQueue<>();
        NaughtyCodeChecker nl = new NaughtyCodeChecker( NO_UTIL_LIST, l );
//        System.out.println( "nc.evidence = " + nl.evidence() );
        assertThat(  nl.isNaughty() ).as("HQeueue is fine").isFalse();

//        fail( "hqueueIsFine reached it's and. You will know what to do." );
    }
}
