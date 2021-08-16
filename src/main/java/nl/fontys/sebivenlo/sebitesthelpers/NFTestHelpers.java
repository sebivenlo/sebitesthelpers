package nl.fontys.sebivenlo.sebitesthelpers;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import static java.lang.reflect.Modifier.FINAL;
import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.PROTECTED;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Arrays.asList;
import static nl.fontys.sebivenlo.sebitesthelpers.TestHelpers.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Test utilities.
 *
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public class NFTestHelpers {

    public static int ALL_VISIBILITY_MASK = PRIVATE | PROTECTED | PUBLIC;
    public static int PRIVATE_FINAL = PRIVATE | FINAL;

    /**
     * Jacocoverage inserts fields, which interfere with fields to be checked.
     *
     * @param someClass defining the fields
     * @return the fields with the synthetic fields removed.
     */
    public static Field[] filteredFields( Class<?> someClass ) {
        return filteredFieldList( someClass ).toArray( new Field[ 0 ] );
    }

    /**
     * Get the fields filtered as List.
     *
     * @param someClass to filter
     * @return the methods with synthetics removed.
     */
    public static List<Field> filteredFieldList( Class<?> someClass ) {
        Field[] declaredFields = someClass.getDeclaredFields();
        List<Field> resultList = new ArrayList<>();
        for ( Field declaredField : declaredFields ) {
            if ( !declaredField.isSynthetic() ) {
                resultList.add( declaredField );
            }
        }
        return resultList;
    }

    /**
     * Jacocoverage inserts methods, which interfere with methods to be checked.
     *
     * @param someClass defining the methods
     * @return the methods with the synthetic methods removed.
     */
    public static Method[] filteredMethods( Class<?> someClass ) {
        Method[] declaredMethods = someClass.getDeclaredMethods();
        List<Method> resultList = new ArrayList<>();
        for ( Method m : declaredMethods ) {
            if ( !m.isSynthetic() ) {
                resultList.add( m );
            }
        }
        Method[] result = resultList.toArray( new Method[ 0 ] );
        return result;
    }

    /**
     * Check the field definition of a class.This method tests if the required
     * modifiers are set.
     *
     * Example: to check private, but not require final, specify modifierMask ==
     * Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED and as
     * modsRequired
     *
     * @param targetClass to check
     * @param modifiers visibility, static, final
     * @param fieldType of the field
     * @param fieldName of the field
     * @throws SecurityException when field is not accessible
     */
    public static void checkField( Class<?> targetClass,
            int modifiers, Class<?> fieldType, String fieldName )
            throws SecurityException {
        checkField( targetClass, modifiers, modifiers, fieldType, fieldName );
    }

    /**
     * Check the field definition of a class.
     *
     * This method tests if the required modifiers are set. Example: to check
     * private, but not require final, specify modifierMask == Modifier.PUBLIC |
     * Modifier.PRIVATE | Modifier.PROTECTED and as modsRequired
     *
     * @param targetClass to check
     * @param modifierMask visibility, static, final
     * @param modsRequired required modifiers
     * @param fieldType of the field
     * @param fieldName of the field
     * @throws SecurityException when field is not accessible
     */
    public static void checkField( Class<?> targetClass,
            int modifierMask, int modsRequired, Class<?> fieldType, String fieldName )
            throws SecurityException {
        Field f = null;
        try {
            f = targetClass.getDeclaredField( fieldName );
            assertEquals( "field " + fieldName + " should be of type "
                    + fieldType,
                    fieldType,
                    f.getType() );
            int fieldModifiers = f.getModifiers();
            if ( ( modifierMask & fieldModifiers ) != modsRequired ) {
                fail( "field '" + f.getName()
                        + "' should be declared '"
                        + Modifier.toString( modifierMask )
                        + "', you declared it '"
                        + Modifier.toString( fieldModifiers ) + '\'' );
            }
        } catch ( NoSuchFieldException ex ) {
            fail( "your class '" + targetClass
                    + "' does not contain the required field '"
                    + Modifier.toString( modifierMask )
                    + " "
                    + fieldType.getSimpleName()
                    + " " + fieldName + "'" );
        }
    }

    /**
     * Check the field definition of a class, including naming conventions.
     *
     * @param targetClass to check
     * @param modifiers visibility, static, final
     * @param type of the field
     * @param fieldName of the field
     * @throws SecurityException when field is not accessible.
     */
    public static void checkFieldAndNaming( Class<?> targetClass,
            int modifiers, Class<?> type, String fieldName )
            throws SecurityException {
        if ( ( PUBLIC | STATIC | FINAL ) == modifiers ) {
            assertAllUpper( fieldName );
        } else {
            char firstChar = fieldName.charAt( 0 );
            assertEquals( "first char not lower case",
                    "" + (char) Character.toLowerCase( firstChar ),
                    "" + (char) firstChar );
        }
        checkField( targetClass, modifiers, type, fieldName );
    }

    /**
     * Check the field definition of a class, including naming conventions.
     * Deprecated because of name spelling.
     *
     * @param targetClass to check
     * @param modifiers visibility, static, final
     * @param type of the field
     * @param fieldName of the field
     * @throws SecurityException when field is not accessible.
     */
    @Deprecated
    public static void checkFieldAnNaming( Class<?> targetClass,
            int modifiers, Class<?> type, String fieldName )
            throws SecurityException {
        checkFieldAndNaming( targetClass, modifiers, type, fieldName );
    }

    /**
     * Assert that a name is fit for a public static final or constant. Throws
     * assertError when failing.
     *
     * @param fieldName to check
     */
    public static void assertAllUpper( String fieldName ) {
        if ( !fieldName.toUpperCase().equals( fieldName ) ) {
            throw new AssertionError( "No all upper in '" + fieldName + "'" );
        }
    }

    public interface ThrowingCallable {

        public void call() throws Throwable;
    }

    /**
     *
     * @param expectedClass expected exception type.
     * @param work to do in the try block
     */
    public static void assertExceptionThrown(
            Class<? extends Throwable> expectedClass, ThrowingCallable work ) {
        Throwable caught = null;

        try {
            work.call();
        } catch ( Throwable t ) {
            caught = t;
        } finally {
            if ( null == caught ) {
                throw new AssertionError( "expected exception "
                        + expectedClass.getName()
                        + " not caught, instead received no exception" );
            } else if ( caught instanceof InvocationTargetException ) {
                caught = caught.getCause();
            }
            if ( !expectedClass.isInstance( caught ) ) {
                throw new AssertionError( "expected exception "
                        + expectedClass.getName()
                        + " not caught, instead received " + caught.getClass().
                                getName() );
            }
        }
    }

    /**
     * Assert that all expected interfaces are implemented.
     *
     * @param targetClass test target
     * @param expected interfaces
     */
    public static void assertImplementsInterfaces( Class<?> targetClass,
            Class<?>... expected ) {
        List<Class<?>> eList = Arrays.asList( expected );
        List<Class<?>> cInterfaces = Arrays.
                asList( targetClass.getInterfaces() );
        boolean allImplemented = true; // start positive
        List<Class<?>> missing = new ArrayList<>();
        for ( Class<?> eInterface : eList ) {
            if ( !cInterfaces.contains( eInterface ) ) {
                missing.add( eInterface );
                allImplemented = false;
            }
        }
        if ( !allImplemented ) {

            throw new AssertionError( "class should (also) implement interfaces " + missing.
                    toString() );
        }
    }

    /**
     * Assert that all expected argument are contained in target.
     *
     * @param target the string to test
     * @param expected sub strings of out
     */
    public static void assertStringContainsAll( String target,
            String... expected ) {
        boolean success = true;
        List<String> missing = new ArrayList<>();
        for ( String arg : expected ) {
            if ( !target.contains( arg ) ) {
                missing.add( arg );
                success = false;
            }
        }
        if ( !success ) {
            throw new AssertionError( "the string does not contain " + missing );
        }
//        assertTrue( , success );
    }

    /**
     * Assert that all regexes given match with the target.
     *
     * @param target the string to test
     * @param regexes that should match
     */
    public static void assertStringMatchesAll( String target,
            String... regexes ) {
        boolean success = true;
        List<String> notMatching = new ArrayList<>();
        for ( String arg : regexes ) {
            Pattern p = Pattern.compile( ".*?" + arg + ".*", Pattern.DOTALL | Pattern.CASE_INSENSITIVE );
            if ( !p.matcher( target ).matches() ) {
                notMatching.add( arg );
                success = false;
            }
        }
        assertTrue( "the string '" + target + "' does not match " + notMatching, success );
    }

    /**
     * Ensure a type is an interface or implements the interface.
     *
     * @param message when there is a complaint
     * @param candidate type, typically field type to check
     * @param expectedInterface what needs to be there.
     */
    //@SuppressWarnings("")
    public static void assertIsOrImplements( String message, Type candidate,
            Type expectedInterface ) {
        assertTrue( message, checkIsOrImplements( candidate, expectedInterface ) );
    }

    /**
     * Check that type is an interface or implements it.
     *
     * @param candidate to check
     * @param expectedInterface that must match
     * @return true on success
     */
    public static boolean checkIsOrImplements( Type candidate,
            Type expectedInterface
    ) {
        Class<?> clz = (Class<?>) candidate;
        return candidate.equals( expectedInterface )
                || asList( clz.getInterfaces() ).
                        contains( (Class<?>) expectedInterface );
    }

    /**
     * Get a field's value by type for an instance by making it accessible.
     *
     * @param <FT> the field generic type
     * @param clz the class of the instance
     * @param instance the object having the field (or not)
     * @param fieldType the class of the field
     * @return the value
     */
    public static <FT> FT getFieldValueByType( Class<?> clz, Object instance,
            Class<FT> fieldType ) {
        FT result = null;
        try {
            Field[] fields = clz.getDeclaredFields();
            assertTrue( "Field of type " + fieldType + " is missing", 0
                    < fields.length );
            for ( Field clzField : fields ) {
                if ( NFTestHelpers.checkIsOrImplements( clzField.getType(),
                        fieldType ) ) {
                    clzField.setAccessible( true );
                    return fieldType.cast( clzField.get( instance ) );
                }
            }
        } catch ( IllegalArgumentException | IllegalAccessException ex ) {
            throw new AssertionError( "cannot find field of type" + fieldType,
                    ex );
        }
        return result;
    }

    /**
     * See if all needles but no more are in the haystack of type list.
     *
     * @param <T> type of
     * @param msg when not
     * @param hayStack search space
     * @param needleClass to be found
     * @param needles things to find, of type T.
     */
    public static <T> void assertContainsExactlyUsingEquals( String msg,
            List<T> hayStack, Class<T> needleClass, T... needles ) {
        List<Object> missing = new ArrayList<>();
        boolean allFound = true;
        for ( T e : needles ) {
            if ( !hayStack.contains( e ) ) {
                allFound = false;
                missing.add( e );
            }
        }
        List<Object> needleList = Arrays.asList( needles );
        List<Object> notWanted = new ArrayList<>();
        boolean noUnWanted = true;
        for ( T t : hayStack ) {
            if ( !needleList.contains( t.toString() ) ) {
                noUnWanted = false;
                notWanted.add( t );
            }
        }
        assertTrue( msg + missing + " extra :" + notWanted, allFound && noUnWanted );
    }

    /**
     * ToString that deals with any exceptions that are thrown during its
     * invocation.
     *
     * When x.toString triggers an exception, the returned string contains a
     * message with this information plus class and system hashCode of the
     * object.
     *
     * @param x to turn into string or a meaningful message.
     * @return "null" when x==null, x.toString() when not.
     */
    public static String safeToString( Object x ) {
        if ( x == null ) {
            return "null";
        }
        try {
            return x.toString();
        } catch ( Throwable e ) {
            return "invoking toString on instance "
                    + x.getClass().getCanonicalName() + "@"
                    + Integer.toHexString( System.identityHashCode( x ) )
                    + " causes an exception " + e.toString();

        }
    }

    /**
     * Assert that the bounds declaration of a method parameter is contains the
     * expected value.Used to test that a bounds declaration such as
     * {@code <? super Something>} and {@code <? extends SometHingsElse>} is
     * correct.
     *
     *
     * @param method to test
     * @param paramPos the position of the parameter in the parameter list of
     * the method, 0 based
     * @param expectedDeclaration string such as {@code <? super SomeThing>}
     * @throws AssertionError on verification failure or invalid position
     */
    public static void assertParameterBoundsDeclaration( Method method, int paramPos, String expectedDeclaration ) {
        assertParameterBoundsDeclaration( "incorrect generic (bounds) declaration", method, paramPos, expectedDeclaration );
    }

    /**
     * Assert that the bounds declaration of a method parameter is contains the
     * expected value.Used to test that a bounds declaration such as
     * {@code <? super Something>} and {@code <? extends SometHingsElse>} is
     * correct.
     *
     *
     * @param mesg message output on failure
     * @param method to test
     * @param paramPos the position of the parameter in the parameter list of
     * the method, 0 based
     * @param expectedDeclaration string such as {@code <? super SomeThing>}
     * @throws AssertionError on verification failure or invalid position
     */
    public static void assertParameterBoundsDeclaration( String mesg, Method method, int paramPos, String expectedDeclaration ) {
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        if ( paramPos >= genericParameterTypes.length ) {
            fail( mesg + ": method " + method
                    + " does not have a parameter at position " + paramPos );
        }
        String param1GenericDecl = genericParameterTypes[ paramPos ].toString();

        assertTrue( mesg, param1GenericDecl.contains( expectedDeclaration ) );
//        fail( "stop" );
    }

    public static void assertParameterIsSupplier( String mesg, Method m, int paramPos, Class<?> bound ) {
        assertParameterBoundsDeclaration( mesg, m, paramPos, "extends " + bound.getName() );
    }

    public static void assertParameterIsReceiver( String mesg, Method m, int paramPos, Class<?> bound ) {
        assertParameterBoundsDeclaration( mesg, m, paramPos, "super " + bound.getName() );
    }

    /**
     * Helper for equals tests, which are tedious to get completely covered.
     *
     * @param <T> type of class to test
     * @param ref reference value
     * @param equal one that should test equals true
     * @param unEqual list of elements that should test unequal in all cases.
     */
    public static <T> void assertEqualsAndHashCode( T ref, T equal, T... unEqual ) {
        Object object = new Object();
        T t0 = null;

        assertFalse( "equals ref " + ref.toString() + " and null should produce false", ref.equals( t0 ) );
        assertFalse( "equals to other type should produce false", ref.equals( object ) );
        assertTrue( "equals with self", ref.equals( ref ) );
        assertTrue( "ref and equal object should report equal", ref.equals( equal ) );
        assertTrue( "equal and ref object should report equal", equal.equals( ref ) );

        for ( int i = 0; i < unEqual.length; i++ ) {
            T ueq = unEqual[ i ];
            assertFalse( "ref " + ref.toString() + "\n and other unequal[" + i
                    + "]: " + ueq.toString() + "\n should be unequal" + ref, ref.equals( ueq ) );
        }

        assertEquals( "equals objects " + ref.toString() + "and " + equal.toString() + "should have same hashcode", ref.hashCode(), equal.hashCode() );
    }

}
