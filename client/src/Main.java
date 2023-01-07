/**
 * A class is to contain main function.
 *
 * @author MinhPhatTran
 */

package ca.cmpt213.a4.client;
import java.io.IOException;
import javax.swing.*;
import ca.cmpt213.a4.client.view.ConsumablesTracker;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new ConsumablesTracker();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}// Main.java
