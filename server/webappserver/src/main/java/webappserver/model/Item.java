/**
 * A class is used to create class Item which has all information for a consumable item.
 *
 * @author MinhPhatTran
 */
package ca.cmpt213.a4.webappserver.model;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Item implements Comparable<Item>{
    // Fields
    private long id;
    private String name;
    private String notes;
    private Double price;
    private Double VolumeOrWeight;
    private LocalDate expiry;
    private String type;

    //Constructor
    public Item(long id, String type, String name, String notes, Double price, Double VolumeOrWeight, LocalDate expiry) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.notes = notes;
        this.price = price;
        this.VolumeOrWeight = VolumeOrWeight;
        this.expiry = expiry;
    }

    public long getDayExpiry() {
        long Days;
        LocalDate today = LocalDate.now();
        Days = ChronoUnit.DAYS.between(this.expiry, today);
        return Days;
    }

    @Override
    public int compareTo(Item c) {
        if (this.getDayExpiry() > c.getDayExpiry()) {
            return -1;
        } else if (this.getDayExpiry() < c.getDayExpiry()) {
            return 1;
        } else {
            return 0;
        }
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getVolumeOrWeight() {
        return VolumeOrWeight;
    }

    public void setVolumeOrWeight(Double volumeOrWeight) {
        VolumeOrWeight = volumeOrWeight;
    }

    public LocalDate getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDate expiry) {
        this.expiry = expiry;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", notes='" + notes + '\'' +
                ", price=" + price +
                ", VolumeOrWeight=" + VolumeOrWeight +
                ", expiry=" + expiry +
                ", type='" + type + '\'' +
                '}';
    }
}// Item.java
