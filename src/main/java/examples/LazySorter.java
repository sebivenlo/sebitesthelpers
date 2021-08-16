/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import java.util.Comparator;
//import sortingservice.Queue;
//import sortingservice.Sorter;

/**
 *
 * @author "Pieter van den Hombergh {@code p.vandenhombergh@fontys.nl}"
 */
public class LazySorter<T> implements Sorter<T> {

    final Comparator<T> comp;

    public LazySorter( Comparator<T> comp ) {
        this.comp = comp;
    }

    @Override
    public Queue<T> sort( Queue<T> q ) {
        ( (NaughtyQueue2<T>) q ).orderElements(comp);
        return q;
    }

}
