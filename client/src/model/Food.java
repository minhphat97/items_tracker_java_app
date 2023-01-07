/**
 * A class is a sub class, which gives all necessary information of food that a customer needs to know.
 *
 * @author MinhPhatTran
 */

package ca.cmpt213.a4.client.model;
import java.time.*;

public class Food extends Consumable {
    //Fields
    private String type = "FOOD";

    //Default Constructor
    public Food() {
        this.setName("");
        this.setNotes("");
        this.setPrice(null);
        this.setVolumeOrWeight(null);
        this.setExpiry(null);
    }

    //Constructor
    public Food(String theName, String theNotes, double thePrice, Double theVolumeOrWeight, LocalDate theExpiry) {
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
    //Override toString
    public String toString() {
        if (this.getDayExpiry() == 0) {
            return "This is a food item." + "\n" + "Name: " + this.getName() + "\n" + "Notes: " + this.getNotes() + "\n" + "Price: " + this.getPrice() + "\n" + "Weight: " + this.getVolumeOrWeight() + "\n" + "Expiry date: " + this.getExpiry() + "\n" + "This food item will expire today.";
        } else if (this.getDayExpiry() > 0) {
            return "This is a food item." + "\n" + "Name: " + this.getName() + "\n" + "Notes: " + this.getNotes() + "\n" + "Price: " + this.getPrice() + "\n" + "Weight: " + this.getVolumeOrWeight() + "\n" + "Expiry date: " + this.getExpiry() + "\n" + "This food item is expired for " + this.getDayExpiry() + " day(s).";
        } else if (this.getDayExpiry() < 0) {
            return "This is a food item." + "\n" + "Name: " + this.getName() + "\n" + "Notes: " + this.getNotes() + "\n" + "Price: " + this.getPrice() + "\n" + "Weight: " + this.getVolumeOrWeight() + "\n" + "Expiry date: " + this.getExpiry() + "\n" + "This food item will expire in " + Math.abs(this.getDayExpiry()) + " day(s).";
        }
        return "";
    }

}//Food.java
