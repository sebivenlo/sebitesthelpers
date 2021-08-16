/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.fontys.sebivenlo.sebitesthelpers;

/**
 *
 * @author hom
 */
public class BrokenToString {

    @Override
    public String toString() {
        throw new RuntimeException("BrokenToString{" + '}');
    }
    
    
}
