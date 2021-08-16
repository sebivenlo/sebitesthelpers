package nl.fontys.sebivenlo.sebitesthelpers;

import examples.MyList;
import examples.NaughtyNaughtyList;
import java.util.Iterator;
import static nl.fontys.sebivenlo.sebitesthelpers.NaughtyCodeChecker.*;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author hom
 */
public class NaughtNaughtyListTest {

//    @Ignore( "//TODO Think TDD" )
    @Test
    public void testNaughty() {
        NaughtyNaughtyList naughty = new NaughtyNaughtyList();
        NaughtyCodeChecker nc = new NaughtyCodeChecker( NO_UTIL_LIST, naughty );
//        System.out.println( "nc.evidence() = " + nc.evidence() );
        assertThat( nc.isNaughty() ).as( "Naughty expected" ).isTrue();
//        Assert.fail( "test method testMethod reached its end, you will know what to do." );
    }

    @Test
    public void testOwnListNotNaughty() {
        MyList l = new MyList();
        NaughtyCodeChecker nl = new NaughtyCodeChecker( NO_UTIL_LIST, l );
//        System.out.println( "nc.evidence() = " + nl.evidence() );
        assertThat( nl.isNaughty() ).as( "Naughty" ).isFalse();

//        fail( "testOwnListNotNaughty reached it's and. You will know what to do." );
    }
}
