/**
 * A class is a base class, which gives all necessary information of Consumable items that a customer needs to know.
 *
 * @author MinhPhatTran
 */

package ca.cmpt213.a4.client.model;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class Consumable implements Comparable<Consumable> {
    //Fields
    private long id;
    private String name;
    private String notes;
    private Double price;
    private LocalDate expiry;
    private String type;
    private Double dayExpiry;
    private Double VolumeOrWeight;


    //Default Constructor
    public Consumable() {
        this.name = "";
        this.notes = "";
        this.price = null;
        this.VolumeOrWeight = null;
        this.expiry = null;
    }

    // Constructor
    Consumable(String theName, String theNotes, Double thePrice, Double theVolumeOrWeight, LocalDate theExpiry) {
        this.name = theName;
        this.notes = theNotes;
        this.price = thePrice;
        this.VolumeOrWeight = theVolumeOrWeight;
        this.expiry = theExpiry;
    }

    // Methods
    public void setId(long theId) {this.id = theId;}

    public void setType(String theType) {this.type = theType;}

    public void setName(String theName) {
        this.name = theName;
    }

    public void setExpiry(LocalDate theExpiry) {
        this.expiry = theExpiry;
    }

    public void setNotes(String theNotes) {
        this.notes = theNotes;
    }

    public void setPrice(Double thePrice) {
        this.price = thePrice;
    }

    public void setVolumeOrWeight(Double theVolumeOrWeight) { this.VolumeOrWeight = theVolumeOrWeight;}

    public long getId() { return this.id;}

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public String getNotes() {
        return this.notes;
    }

    public Double getPrice() {
        return this.price;
    }

    public Double getVolumeOrWeight() {
        return this.VolumeOrWeight;
    }

    public LocalDate getExpiry() {
        return this.expiry;
    }

    public long getDayExpiry() {
        long Days;
        LocalDate today = LocalDate.now();
        Days = ChronoUnit.DAYS.between(this.expiry, today);
        return Days;
    }

    // Override compareTo
    public int compareTo(Consumable c) {
        if (this.getDayExpiry() > c.getDayExpiry()) {
            return -1;
        } else if (this.getDayExpiry() < c.getDayExpiry()) {
            return 1;
        } else {
            return 0;
        }
    }
}//Consumable.java
