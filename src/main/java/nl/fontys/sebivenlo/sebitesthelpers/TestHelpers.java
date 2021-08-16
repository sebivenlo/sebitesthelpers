/*
 * Copyright (c) 2019 Informatics Fontys FHTenL University of Applied Science Venlo
 */
package nl.fontys.sebivenlo.sebitesthelpers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import static java.util.stream.Collectors.toList;

/**
 * Substitute methods for asserts, to allow use with different frameworks.
 *
 * @author Richard van den Ham {@code r.vandenham@fontys.nl}
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class TestHelpers {

    /**
     * Jacocoverage inserts fields, which interfere with fields to be checked.
     * Get the declared fields of a class with these fields filtered out.
     *
     * @param someClass defining the fields
     * @return the fields with the synthetic fields removed.
     */
    public static List<Field> filteredFields( Class<?> someClass ) {
        return Arrays.stream( someClass.getDeclaredFields() )
                .filter( f -> !f.isSynthetic() )
                .collect( toList() );
    }

    /**
     * Assert that a field is final.
     *
     * @param field to be asserted for finality.
     */
    public static void assertFinal( Field field ) {
        assertFinal( "field " + field + " should be final", field );
    }

    /**
     * assert that a field is final.
     *
     * @param msg of failure
     * @param field to assert for finality
     */
    public static void assertFinal( String msg, Field field ) {
        assertTrue( msg, Modifier.isFinal( field.getModifiers() ) );
    }

    /**
     * Helper for equals tests, which are tedious to get completely covered.
     *
     * @param <T> type of class to test
     * @param ref reference value
     * @param equal one that should test equals true
     * @param unEqual list of elements that should test unequal in all cases.
     */
    public static <T> void testEquals( T ref, T equal, T... unEqual ) {
        Object object = new Object();
        T t0 = null;

        assertFalse( "equals ref " + ref.toString() + " and null should produce false", ref.equals( t0 ) );
        assertFalse( "equals to other type should produce false", ref.equals( object ) );
        assertTrue( "equals with self", ref.equals( ref ) );
        assertTrue( "ref and equal object should report equal", ref.equals( equal ) );
        assertTrue( "equal and ref object should report equal", equal.equals( ref ) );

        for ( int i = 0; i < unEqual.length; i++ ) {
            T ueq=unEqual[i];
            assertFalse( "ref " + ref.toString() + "\n and other unequal["+i
                    + "]: " + ueq.toString() + "\n should be unequal" + ref, ref.equals( ueq ) );
        }

        assertEquals( "equals objects " + ref.toString() + "and " + equal.toString() + "should have same hashcode", ref.hashCode(), equal.hashCode() );
    }

    public static <T> void testEqualsAndHashCode( T ref, T equal, T... unEqual ) {
        testEquals( ref, equal, unEqual );
        assertTrue( "hashcode of equal objects should be same", ref.hashCode() == equal.hashCode() );
    }

    static void assertEquals( String string, Object expected, Object actual ) {
        if ( !Objects.equals( expected, actual ) ) {

            throw new AssertionError( string
                    + String.format( " Expected <%s> and <%s> to be equal but the were not",
                            expected, actual ) );
        }
    }

    static void assertTrue( String message, boolean boolValue ) {
        if ( !boolValue ) {
            throw new AssertionError( message );
        }
    }

    static void fail( String message ) {
        throw new AssertionError( message );
    }

    static void assertFalse( String message, boolean boolValue ) {
        if ( boolValue ) {
            throw new AssertionError( message );
        }
    }

}
