package examples;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hom
 */
public class NaughtyUsingList {

    public static void main( String[] args ) {
        List<String> l = new ArrayList<>();

        l.add( "hello" );
        System.out.println( "l = " + l );

        Serializable s = new Serializable() {
            List<String> l = new ArrayList<>();

        };
    }

    class Inner {

        List<String> l = new ArrayList<>();
    }
}
