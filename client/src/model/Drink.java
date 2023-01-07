/**
 * A class is a sub class, which gives all necessary information of drink that a customer needs to know.
 *
 * @author MinhPhatTran
 */

package ca.cmpt213.a4.client.model;
import java.time.*;
public class Drink extends Consumable {
    //Fields
    private String type = "DRINK";
    //Default Constructor
    public Drink() {
        this.setName("");
        this.setNotes("");
        this.setPrice(null);
        this.setVolumeOrWeight(null);
        this.setExpiry(null);
    }
    //Constructor
    public Drink(String theName, String theNotes, double thePrice, double theVolumeOrWeight, LocalDate theExpiry) {
        this.setName(theName);
        this.setNotes(theNotes);
        this.setPrice(thePrice);
        this.setVolumeOrWeight(theVolumeOrWeight);
        this.setExpiry(theExpiry);
    }
    //Override getType
    public String getType() {
        return this.type;
    }

    // Override toString
    public String toString() {
        if (this.getDayExpiry() == 0) {
            return "This is a drink item." + "\n" + "Name: " + this.getName() + "\n" + "Notes: " + this.getNotes() + "\n" + "Price: " + this.getPrice() + "\n" + "Volume: " + this.getVolumeOrWeight() + "\n" + "Expiry date: " + this.getExpiry() + "\n" + "This drink item will expire today.";
        } else if (this.getDayExpiry() > 0) {
            return "This is a drink item." + "\n" + "Name: " + this.getName() + "\n" + "Notes: " + this.getNotes() + "\n" + "Price: " + this.getPrice() + "\n" + "Volume: " + this.getVolumeOrWeight() + "\n" + "Expiry date: " + this.getExpiry() + "\n" + "This drink item is expired for " + this.getDayExpiry() + " day(s).";
        } else if (this.getDayExpiry() < 0) {
            return "This is a drink item." + "\n" + "Name: " + this.getName() + "\n" + "Notes: " + this.getNotes() + "\n" + "Price: " + this.getPrice() + "\n" + "Volume: " + this.getVolumeOrWeight() + "\n" + "Expiry date: " + this.getExpiry() + "\n" + "This drink item will expire in " + Math.abs(this.getDayExpiry()) + " day(s).";
        }
        return "";
    }
}//Drink.java