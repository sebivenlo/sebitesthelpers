package nl.fontys.sebivenlo.sebitesthelpers;

import java.lang.reflect.Field;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandehombergh@fontysvenlo.org}
 */
public class ModifierCheckerTest {

    String puk;
    String suck;
    public static final String SOCKE = "Stefan";

    public ModifierCheckerTest() throws NoSuchFieldException {

    }

//    @Ignore
    @Test
    public void testCheckPrivate() throws NoSuchFieldException {
        Field f1 = this.getClass().getDeclaredField( "puk" );
        assertThat( ModifierChecker.REQUIRE_PRIVATE.test( f1 ) ).isFalse();
        assertThat( ModifierChecker.REQUIRE_FINAL.test( f1 ) ).isFalse();
        Field f2 = this.getClass().getDeclaredField( "SOCKE" );
        assertThat( ModifierChecker.REQUIRE_PUBLIC_STATIC_FINAL.test( f2 ) ).isFalse();

//        fail( "testCheckPrivate reached it's and. You will know what to do." );
    }
}
