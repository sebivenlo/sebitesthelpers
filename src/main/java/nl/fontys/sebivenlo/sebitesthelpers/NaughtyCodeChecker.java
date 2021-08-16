package nl.fontys.sebivenlo.sebitesthelpers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

/**
 * Checks that a class does not use forbidden code patterns, like arrays access,
 * uses java.util.List in some variant.
 *
 * This checker is very simple and relies on javap. It infers the class file
 * from the class, runs javap over it and writes the result to a file, then
 * analyses the text with a regular expression.
 *
 * Usage example:<br>
 * {@code NaughtyCodeChecker.assertNotNaughty("tss, naughty naughty", naughtyInstance, NaughtyCodeChecker.NO_ARRAYS);}
 *
 *
 * @author hom
 */
public class NaughtyCodeChecker {

    private Pattern forbidden;
    private Class<?> clz;
    private final List<String> naughtyLines = new ArrayList<>();
    private final Set<String> checked = new HashSet<>();

    /**
     * Array access check. Java byte code has special instructions to do that.
     */
    public static final String NO_ARRAYS = "^\\s+\\d+:\\sa(astore|aload|rraylength).*$";
    /**
     * Check for not allowed List classes from package {@code java.util}
     */
    public static final String NO_UTIL_LIST = "^(.*)?//(.*?)java/util/[a-zA-Z0-9]*?List[a-zA-Z0-9]*?.*$";

    /**
     * Create a checker with a forbidden code pattern for a class.
     *
     * @param forbidden pattern
     * @param clz       class
     */
    public NaughtyCodeChecker( String forbidden, Class<?> clz ) {
        this( Pattern.compile( forbidden ), clz );
    }

    /**
     * Create checker for an instance of a class.
     *
     * @param forbidden pattern
     * @param o         object t check
     */
    public NaughtyCodeChecker( String forbidden, Object o ) {
        this( Pattern.compile( forbidden ), o.getClass() );
    }

    /**
     * Create a checker using a regex pattern.
     *
     * @param forbidden pattern
     * @param clz       to check.
     */
    public NaughtyCodeChecker( Pattern forbidden, Class<?> clz ) {
        this.forbidden = forbidden;
        this.clz = clz;
        check( this.clz );
    }

    public final NaughtyCodeChecker check() {
        return check( this.clz );
    }

    Set<String> feds = new HashSet<>();

    /**
     * Check that the class does not use forbidden code patterns.
     *
     * @param clazz to check.
     * @return this checker
     */
    public final NaughtyCodeChecker check( Class<?> clazz ) {
        String clazzName = ByteCodeReader.classToFileNameBase( clazz );
        Predicate<String> naughtyFilter = forbidden.asPredicate();
        feds.addAll( collectEvidence( clazzName, naughtyFilter ) );
        if ( null == forbidden ) {
            throw new AssertionError( "forbidden pattern not set" );
        }
        if ( null == clazz ) {
            throw new AssertionError( "class to check not set" );
        }
        for ( String federatedClz : feds ) {
            if ( !federatedClz.equals( clazzName ) ) {
                collectEvidence( federatedClz, naughtyFilter );
            }
        }
        for ( Class<?> nestMember : clazz.getNestMembers() ) {
            if ( nestMember != clazz ) {
                collectEvidence( nestMember.getName(), naughtyFilter );
            }
        }

        Class<?> superclass = clazz.getSuperclass();
        if ( superclass != null && superclass != java.lang.Object.class ) { // terminates at Object.class
            check( superclass );
        }
        return this;
    }

    private Set<String> collectEvidence( String clzName, Predicate<String> naughtyFilter ) {
        ByteCodeReader byteCodeReader = ByteCodeReader.readCode( clzName );
        List<String> evidence = byteCodeReader
                .allClassCodeLines()
                .filter( naughtyFilter )
                .collect( toList() );
        if ( !evidence.isEmpty() ) {
            naughtyLines.add( "class = " + clzName + "{" );
            naughtyLines.addAll( evidence );
            naughtyLines.add( "}" );
        }
        checked.add( clzName );
        return byteCodeReader.federatedClassFiles;
    }

    /**
     * Report if this checker found naughtiness.
     *
     * @return naughty
     */
    public boolean isNaughty() {
        return !naughtyLines.isEmpty();
    }

    /**
     * Report the evidence of the naughtiness.
     *
     * @return string, which is empty if no evidence is found.
     */
    public String evidence() {
        return naughtyLines.stream().collect( joining( "\n" ) );
    }

    /**
     * Assert that the class of an object does apply forbidden code patterns.
     *
     * @param message to be printed on naughtiness
     * @param o       instance of naughty class
     * @param pattern java byte code matching pattern.
     */
    public static void assertNotNaughty( String message, Object o, String pattern ) {
        assertNotNaughty( message, o.getClass(), pattern );
    }

    /**
     * Assert that the class of an object does apply forbidden code patterns.
     *
     * @param message to be printed on naughtiness
     * @param clz
     * @param pattern java byte code matching pattern.
     */
    public static void assertNotNaughty( String message, Class<?> clz, String pattern ) {
        NaughtyCodeChecker nc = new NaughtyCodeChecker( pattern, clz ).check();
        if ( nc.isNaughty() ) {
            throw new AssertionError( message + "\n with evidence [" + nc.evidence() + "]" );
        }
    }

    public List<String> getNaughtyLines() {
        return naughtyLines;
    }

    public Set<String> getChecked() {
        return checked;
    }

}
