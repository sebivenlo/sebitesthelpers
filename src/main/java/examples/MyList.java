package examples;

import java.util.Iterator;

/**
 * Test business code. 
 * 
 * @author Pieter van den Hombergh {@code p.vandehombergh@fontysvenlo.org}
 */
public class MyList {
    
    // triggers java/util regex
    Iterator<String> iterator() {
        return null;
    }

    void m() {
        Iterator<String> iterator = this.iterator();
        if ( iterator != null ) {
        }
    }
    
}
