/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sebivenlo.sebitesthelpers;

import examples.NaughtyUsingArray;
import static nl.fontys.sebivenlo.sebitesthelpers.NaughtyCodeChecker.NO_ARRAYS;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

/**
 *
 * @author hom
 */
public class NaughytArrayTest {

//    @Ignore( "//TODO Think TDD" )
    @Test
    public void testNaughty() {
        Object naughty = new NaughtyUsingArray();
        NaughtyCodeChecker nc = new NaughtyCodeChecker( NO_ARRAYS, naughty );
//        System.out.println( "nc.evidence() = " + nc.evidence() );
        assertThat( nc.isNaughty() ).as( "Naughty" ).isTrue();
//        Assert.fail( "test method testMethod reached its end, you will know what to do." );
    }

}
