/**
 * A class gives all options that a customer needs to choose for processing food items.
 *
 * @author MinhPhatTran
 */
package ca.cmpt213.a4.client.view;
import java.time.*;

public class Menu {
    private String title;
    private String[] option = {"List all items",
            "Add a new item",
            "Remove an item",
            "List expired items",
            "List items that are not expired",
            "List items expiring in 7 days",
            "Exit"};

    // Method that format menu before printing
    public void display() {
        String title = "# My Consumable Items Tracker #";
        int titleLength = title.length();
        for (int i = 0; i < titleLength; i++) {
            System.out.print("#");
        }
        System.out.print('\n');
        System.out.println(title);
        for (int i = 0; i < titleLength; i++) {
            System.out.print("#");
        }
        LocalDate today = LocalDate.now();
        System.out.print('\n');
        System.out.println("Today is: " + today);

        int optionLength = option.length;
        for (int i = 0; i < option.length; i++) {
            System.out.println((i + 1) + ": " + option[i]);
        }
    }

    // Method that check whether the user input is valid
    public void checkUserInput(int input) {
        System.out.println("Invalid selection. Enter a number between 1 and 7");
    }

}//Menu.java
