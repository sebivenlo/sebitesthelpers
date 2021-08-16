package examples;

import java.util.Arrays;
import java.util.List;

/**
 * Test data to check code patterns in methods.
 *
 * @author Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}
 */
public class CodePatterns {
    
    void foreChar() {
        for ( char c : "hello".toCharArray() ) {
            System.out.println( "c=" + c );
        }
    }
    
    void forEList() {
        
        List<String> of = Arrays.asList( "Hello", "world" );
        
        for ( String string : of ) {
            System.out.println( "string = " + string );
        }
    }
    
    void useStream() {
        Arrays.asList( "Hello", "world" ).stream().forEach( e -> {
        } );
        throw new AssertionError( "for kicks" );
        
    }
}
