package nl.fontys.sebivenlo.sebitesthelpers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static java.util.stream.Collectors.joining;
import org.assertj.core.api.AbstractAssert;

/**
 *
 * @author "Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}"
 */
public class ByteCodeAssertions extends AbstractAssert<ByteCodeAssertions, Method> {

    public ByteCodeAssertions( Method actual ) {
        super( actual, ByteCodeAssertions.class );
    }

    public static ByteCodeAssertions assertThat( Method actual ) {
        return new ByteCodeAssertions( actual );
    }

    /**
     * The method must use any of the codes at least once.
     *
     * The byte code strings can be actual byte codes but also parameter names,
     * type names and method names. For instance having a goto typically means
     * there is a kind of loop (for, while).
     *
     * @param codes the byte code strings.
     * @return this
     * @throws AssertionError when no all codes are used.
     */
    public ByteCodeAssertions usesCodes( String... codes ) {
        isNotNull();
        Class<?> declaringClass = actual.getDeclaringClass();
        ByteCodeReader bcr = ByteCodeReader.readCode( declaringClass );
        List<String> methodCode = bcr.getMethodCode( actual );
        int[] checkMarks = new int[ codes.length ];

        for ( int i = 0; i < codes.length; i++ ) {
            for ( String codeLine : methodCode ) {
                if ( codeLine.contains( codes[ i ] ) ) {
                    checkMarks[ i ]++;
                }
            }
        }
        if ( !Arrays.stream( checkMarks ).allMatch( i -> i > 0 ) ) {
            List<String> missing = new ArrayList<>();
            for ( int i = 0; i < checkMarks.length; i++ ) {
                if ( checkMarks[ i ] == 0 ) {
                    missing.add( codes[ i ] );
                }
            }
            failWithMessage( "Expecting specific bytes codes to be used, but these were missing: <%s> ",
                    missing.stream().collect( joining( ", " ) ) );
        }

        return this;
    }

    /**
     * The method must not use any of the codes.
     *
     * The byte code strings can be actual byte codes but also parameter names,
     * type names and method names. For instance having a goto typically means
     * there is a kind of loop (for, while). Iterator hints towards fore loop
     * for plain iterator use.
     *
     * @param codes the byte code strings.
     * @return this
     * @throws AssertionError when no all codes are used.
     */
    public ByteCodeAssertions avoidsCodes( String... codes ) {
        isNotNull();
        ByteCodeReader bcr = ByteCodeReader.readCode( actual.getDeclaringClass() );
        List<String> methodCode = bcr.getMethodCode( actual );

        int[] checkMarks = new int[ codes.length ];

        for ( int i = 0; i < codes.length; i++ ) {
            for ( String string : methodCode ) {
                if ( string.contains( codes[ i ] ) ) {
                    checkMarks[ i ]++;
                }
            }
        }

        if ( Arrays.stream( checkMarks ).anyMatch( i -> i > 0 ) ) {
            List<String> abuse = new ArrayList<>();
            for ( int i = 0; i < checkMarks.length; i++ ) {
                if ( checkMarks[ i ] > 0 ) {
                    abuse.add( codes[ i ] );
                }
            }
            failWithMessage( "Some codes are to be avoided, but were used: <%s> ",
                    abuse.stream().collect( joining( ", " ) ) );
        }

        return this;
    }
}
