package nl.fontys.sebivenlo.sebitesthelpers;

import java.time.LocalDate;

/**
 * Car example with wrong field definitions for test purposes.
 *  Each field has a problem.
 * @author Pieter van den Hombergh (879417) {@code p.vandenhombergh@fontys.nl}
 */
public class Car {

    String color; // should be private and final.
    final LocalDate buildDate; // should be private
    private String licensePlate; // should be final
    final double price; // should be int
    
    public Car( String color, LocalDate buildDate, String lp, double price ) {
        this.color = color;
        this.buildDate = buildDate;
        this.licensePlate = lp;
        this.price=price;
    }
    
}
