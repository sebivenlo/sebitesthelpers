package nl.fontys.sebivenlo.sebitesthelpers;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import static java.time.LocalDate.of;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import static nl.fontys.sebivenlo.sebitesthelpers.NFTestHelpers.assertParameterBoundsDeclaration;
import static nl.fontys.sebivenlo.sebitesthelpers.NFTestHelpers.assertParameterIsReceiver;
import static nl.fontys.sebivenlo.sebitesthelpers.NFTestHelpers.assertParameterIsSupplier;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author hom
 */
public class MethodParamsTest {

    class C {

        void m(List<? super Car> receiver) {
            receiver.add( new Car( "blue", of( 1977, 6, 7 ), "BK-01-23", 100D ) );
        }

        List<String> prefixAll(String prefix, List<? extends CharSequence> s) {
            return null;
        }

        /**
         * Raw parameters used.
         *
         * @param p list
         * @param c collection
         */
        void x(List p, Collection c) {

        }
    }

    @Test
    public void testSuper() throws NoSuchMethodException {
        C c = new C();
        Class<? extends C> cClass = c.getClass();
        List<Method> methods = Arrays.asList( cClass.getDeclaredMethods() );
        String collect = methods.stream().map( Method::toString ).collect( Collectors.joining( "\n" ) );
        System.out.println( "methods = " + collect );
        Method methodM = cClass.getDeclaredMethod( "m", List.class );
        Type[] genericParameterTypes = methodM.getGenericParameterTypes();
        System.out.println( "" + Arrays.toString( genericParameterTypes ) );
        String param1GenericDecl = genericParameterTypes[ 0 ].toString();
        assertThat( param1GenericDecl ).contains( "? super nl.fontys.sebivenlo.sebitesthelpers.Car" );
//        fail( "stop" );
    }

    @Test
    public void testAssertBoundsDeclaration() throws NoSuchMethodException {
        C c = new C();
        Class<? extends C> cClass = c.getClass();
        Method methodM = cClass.getDeclaredMethod( "m", List.class );
        try {
            assertParameterBoundsDeclaration( methodM, 0, "? super nl.fontys.sebivenlo.sebitesthelpers.Car" );
        } catch ( Throwable t ) {
            System.out.println( "t = " + t );
            throw t;
        }
    }

    @Test
    public void testAssertBoundsDeclaration2() throws NoSuchMethodException {
        C c = new C();
        Class<? extends C> cClass = c.getClass();
        Method methodM = cClass.getDeclaredMethod( "prefixAll", String.class, List.class );
        assertParameterBoundsDeclaration( methodM, 1, "? extends java.lang.CharSequence" );
    }

    @Test
    public void testMessage() throws NoSuchMethodException {
        C c = new C();
        Class<? extends C> cClass = c.getClass();
        Method methodM = cClass.getDeclaredMethod( "m", List.class );
        try {
            assertParameterBoundsDeclaration( "myText", methodM, 1, "? super nl.fontys.sebivenlo.sebitesthelpers.Car" );
        } catch ( Throwable t ) {
            System.out.println( "t = " + t );
            assertThat( t.getMessage() ).contains( "myText" );
        }
    }

    @Test
    public void testMessageNoSuchParam() throws NoSuchMethodException {
        C c = new C();
        Class<? extends C> cClass = c.getClass();
        Method methodM = cClass.getDeclaredMethod( "m", List.class );
        try {
            assertParameterBoundsDeclaration( methodM, 1, "? super nl.fontys.sebivenlo.sebitesthelpers.Car" );
        } catch ( Throwable t ) {
            System.out.println( "t = " + t );
            assertThat( t.getMessage() ).contains( "does not have a parameter at position" );
        }
    }

    @Test
    public void testMessageWrongBounds() throws NoSuchMethodException {
        C c = new C();
        Class<? extends C> cClass = c.getClass();
        Method methodM = cClass.getDeclaredMethod( "m", List.class );
        try {
            assertParameterBoundsDeclaration( methodM, 1, "? extends nl.fontys.sebivenlo.sebitesthelpers.Car" );
        } catch ( Throwable t ) {
            System.out.println( "t = " + t );
            assertThat( t.getMessage() ).contains( "incorrect generic (bounds) declaration" );
        }
    }

    //@Disabled("Think TDD")
    @Test
    public void supplierFail() throws NoSuchMethodException {
        C c = new C();
        Class<? extends C> cClass = c.getClass();
        Method methodX = cClass.getDeclaredMethod( "x", List.class, Collection.class );
        System.out.println( "methodX = " + methodX );
        try {
            NFTestHelpers.assertParameterIsReceiver( "This be wrong", methodX, 0, Car.class );
        } catch ( Throwable t ) {
            String message = t.getMessage();
            assertThat( message ).contains( "This be wrong" );
        }
//        fail( "method supplier reached end. You know what to do." );
    }
    //@Disabled("Think TDD")

    @Test
    public void supplierPass() throws NoSuchMethodException {
        C c = new C();
        Class<? extends C> cClass = c.getClass();
        Method methodPrefixAll = cClass.getDeclaredMethod( "prefixAll", String.class, List.class );
        assertParameterIsSupplier( "parameter 1 of method prefixAll should supply", methodPrefixAll, 1, CharSequence.class );
//        fail( "method supplierPass reached end. You know what to do." );
    }

    //@Disabled("Think TDD")
    @Test
    public void receiverFail() throws NoSuchMethodException {
        C c = new C();
        Class<? extends C> cClass = c.getClass();
        Method methodX = cClass.getDeclaredMethod( "x", List.class, Collection.class );
        System.out.println( "methodX = " + methodX );
        try {
            NFTestHelpers.assertParameterIsReceiver( "This be wrong", methodX, 1, Car.class );
        } catch ( Throwable t ) {
            String message = t.getMessage();
            assertThat( message ).contains( "This be wrong" );
        }
//        fail( "method receiverFail reached end. You know what to do." );
    }

    @Test
    public void receiverPass() throws NoSuchMethodException {
        C c = new C();
        Class<? extends C> cClass = c.getClass();
        Method method = cClass.getDeclaredMethod( "m", List.class );
        assertParameterIsReceiver( "parameter 1 of method m should supply", method, 0, Car.class );

//        fail( "method receiverPass reached end. You know what to do." );
    }
}
