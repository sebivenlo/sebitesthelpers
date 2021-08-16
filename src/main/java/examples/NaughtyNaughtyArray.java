package examples;

import java.util.List;

/**
 *
 * @author Pieter van den Hombergh {@code p.vandehombergh@fontysvenlo.org}
 */
public class NaughtyNaughtyArray extends NaughtyUsingArray {
    class Inner{
        List<String> naughty;

        public Inner( List<String> naughty ) {
            this.naughty = naughty;
        }
        
    }
}
