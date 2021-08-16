/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sebivenlo.sebitesthelpers;

import examples.NaughtyUsingList;
import static nl.fontys.sebivenlo.sebitesthelpers.NaughtyCodeChecker.*;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 *
 * @author hom
 */
public class NaughtyListTest {

//    @Ignore( "//TODO Think TDD" )
    @Test
    public void testNaughty() {
        NaughtyUsingList naughty = new NaughtyUsingList();
        NaughtyCodeChecker nc = new NaughtyCodeChecker( NO_UTIL_LIST, naughty );
//        System.out.println( "evidence() = " + nc.evidence() );
        assertThat( nc.isNaughty() ).as( "Naughty class " ).isTrue();
//        Assert.fail( "test method testMethod reached its end, you will know what to do." );
    }

}
