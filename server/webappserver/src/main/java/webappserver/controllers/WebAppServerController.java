/**
 * A class is used to respond to REST messages.
 *
 * @author MinhPhatTran
 */
package ca.cmpt213.a4.webappserver.controllers;
import ca.cmpt213.a4.webappserver.control.ManageItems;
import ca.cmpt213.a4.webappserver.model.Item;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class WebAppServerController {
    private ArrayList<Item> itemArrayList = new ArrayList<Item>();
    private ArrayList<Item> itemExpired = new ArrayList<Item>();
    private ArrayList<Item> itemNonExpired = new ArrayList<Item>();
    private ArrayList<Item> itemExpired7Days = new ArrayList<Item>();
    private AtomicLong nextId = new AtomicLong(0);
    ManageItems manage = new ManageItems();
    @GetMapping("/ping")
    public String getHello() {
        return "System is up!";
    }

    // This is used to save an item to web server
    @PostMapping("/addItem")
    public Item addItem(@RequestBody Item aItem) {
        aItem.setId(nextId.getAndIncrement());
        itemArrayList.add(aItem);

        if (aItem.getDayExpiry() > 0) {
            itemExpired.add(aItem);
        }
        if (aItem.getDayExpiry() <= 0) {
            itemNonExpired.add(aItem);
        }
        if (aItem.getDayExpiry() >= -7 && aItem.getDayExpiry() <= 0) {
            itemExpired7Days.add(aItem);
        }
        Collections.sort(itemArrayList);
        Collections.sort(itemExpired);
        Collections.sort(itemNonExpired);
        Collections.sort(itemExpired7Days);
        return aItem;
    }

    // This is used to remove an item by its specific ID
    @PostMapping("/removeItem/{id}")
    public void removeItem(@PathVariable("id") long itemId) {
        Item toRemove = null;
        for (Item item : itemArrayList) {
            if (item.getId() == itemId) {
                toRemove = item;
            }
        }
        Item toRemoveExpired = null;
        for (Item item : itemExpired) {
            if (item.getId() == itemId) {
                toRemoveExpired = item;
            }
        }
        Item toRemoveNonExpired = null;
        for (Item item : itemNonExpired) {
            if (item.getId() == itemId) {
                toRemoveNonExpired = item;
            }
        }
        Item toRemoveExpired7Days = null;
        for (Item item : itemExpired7Days) {
            if (item.getId() == itemId) {
                toRemoveExpired7Days = item;
            }
        }
        if (toRemove != null) {
            itemArrayList.remove(toRemove);
        }
        if (toRemoveExpired != null) {
            itemExpired.remove(toRemoveExpired);
        }
        if (toRemoveNonExpired != null) {
            itemNonExpired.remove(toRemoveNonExpired);
        }
        if (toRemoveExpired7Days != null) {
            itemExpired7Days.remove(toRemoveExpired7Days);
        }
    }

    // This is used to list all item in web server
    @GetMapping("/listAll")
    public ArrayList<Item>getAllItems() {
        return itemArrayList;
    }

    // This is used to list expired items in web server
    @GetMapping("/listExpired")
    public ArrayList<Item>getExpiredItems() {
        return itemExpired;
    }

    // This is used to listed non-expired items in web server
    @GetMapping("/listNonExpired")
    public ArrayList<Item>getNonExpiredItems() {
        return  itemNonExpired;
    }

    // This is used to list expiring items in 7 days ion web server
    @GetMapping("/listExpiringIn7Days")
    public ArrayList<Item>getExpired7DaysItems() {
        return itemExpired7Days;
    }

    // This is used to close web server and save all items into a JSON file
    @GetMapping("/exit")
    public void Exit() {
        manage.Writing("./ItemList.JSON", itemArrayList);
        System.exit(0);
    }

}// WebAppServerController.java
