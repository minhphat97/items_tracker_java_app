/**
 * A class is a class used to design graphic user interface for the application Consumables Tracker.
 *
 * @author MinhPhatTran
 */
package ca.cmpt213.a4.client.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.HttpURLConnection;

import org.apache.http.client.methods.*;

import java.net.URL;
import java.util.*;
import java.time.*;

import com.github.lgooddatepicker.components.*;
import ca.cmpt213.a4.client.model.*;
import ca.cmpt213.a4.client.control.ManageItems;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class ConsumablesTracker extends JFrame {
    static ArrayList<Consumable> Item = new ArrayList<Consumable>();
    static ArrayList<Consumable> itemExpired = new ArrayList<Consumable>();
    static ArrayList<Consumable> itemNonExpired = new ArrayList<Consumable>();
    static ArrayList<Consumable> itemExpired7Days = new ArrayList<Consumable>();

    JTextArea textArea;

    // Create a general frame and read data from JSON file
    public ConsumablesTracker() throws IOException {
        URL url = new URL("http://localhost:8080/ping");
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.disconnect();

        // Load data saved from web to ArrayList
        ManageItems manage = new ManageItems();
        manage.readingBuffer("http://localhost:8080/listAll", Item);
        manage.readingBuffer("http://localhost:8080/listExpired", itemExpired);
        manage.readingBuffer("http://localhost:8080/listNonExpired", itemNonExpired);
        manage.readingBuffer("http://localhost:8080/listExpiringIn7Days", itemExpired7Days);

        setTitle("My Consumables Tracker");
        setSize(600, 700);
        setLocation(200, 200);
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        getContentPane().setLayout(null);

        // This button is to add a new item
        JButton btnAddItem = new JButton();
        btnAddItem.setText("Add item");
        btnAddItem.setSize(100, 50);
        btnAddItem.setLocation(250, 560);

        // Create a Dialog with multiple buttons
        class AddItem extends JDialog {
            public AddItem() {

                String type[] = {"Food", "Drink"};
                setResizable(false);
                setTitle("Add Item");
                setSize(350, 600);
                setLocation(200, 200);
                setLocationRelativeTo(null);
                getContentPane().setLayout(null);

                JLabel text = new JLabel("Type: ");
                text.setBounds(50, 30, 74, 23);
                JComboBox types = new JComboBox(type);
                types.setBounds(100, 60, 175, 23);
                getContentPane().add(text);
                getContentPane().add(types);

                JLabel text1 = new JLabel("Name: ");
                text1.setBounds(50, 90, 74, 23);
                JTextField name = new JTextField();
                name.setBounds(100, 120, 175, 23);
                getContentPane().add(text1);
                getContentPane().add(name);

                JLabel text2 = new JLabel("Notes: ");
                text2.setBounds(50, 150, 74, 23);
                JTextField note = new JTextField();
                note.setBounds(100, 180, 175, 23);
                getContentPane().add(text2);
                getContentPane().add(note);

                JLabel text3 = new JLabel("Price: ");
                text3.setBounds(50, 210, 74, 23);
                JTextField price = new JTextField();
                price.setBounds(100, 240, 175, 23);
                getContentPane().add(text3);
                getContentPane().add(price);

                JLabel text5 = new JLabel();
                text5.setBounds(50, 270, 74, 23);
                JTextField weightOrVolume = new JTextField();
                weightOrVolume.setBounds(100, 300, 175, 23);
                getContentPane().add(text5);
                getContentPane().add(weightOrVolume);

                JLabel text6 = new JLabel("Expiry date: ");
                text6.setBounds(50, 330, 100, 23);
                DatePicker expiryDate = new DatePicker();
                expiryDate.setBounds(100, 360, 175, 23);
                getContentPane().add(text6);
                getContentPane().add(expiryDate);

                types.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        if (e.getSource() == types) {
                            JComboBox temp = (JComboBox) e.getSource();
                            String msg = (String) temp.getSelectedItem();
                            switch (msg) {
                                case "Food":
                                    text5.setText("Weight: ");
                                    break;
                                case "Drink":
                                    text5.setText("Volume: ");
                                    break;
                            }
                        }
                    }
                });
                // This button will save an item to server
                JButton btnCreate = new JButton("Create");
                btnCreate.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String typeItem = (String) types.getSelectedItem();
                        String nameItem = name.getText();
                        String noteItem = note.getText();
                        String priceItem = price.getText();
                        String weightVolumeItem = weightOrVolume.getText();
                        LocalDate expiryItem = expiryDate.getDate();
                        String[] messageFood = {"Name, price or weight of food item is empty !", "Price or weight of food item is negative !"};
                        String[] messageDrink = {"Name, price or volume of drink item is empty !", "Price or volume of drink item is negative !"};

                        if (typeItem.equals("Food")) {
                            if (nameItem.equals("") || priceItem.equals("") || weightVolumeItem.equals("")) {
                                JOptionPane.showMessageDialog(null, messageFood[0], "Error !", JOptionPane.ERROR_MESSAGE);
                            }
                            if (Double.valueOf(priceItem) < 0 || Double.valueOf(weightVolumeItem) < 0) { // Note here there is error
                                JOptionPane.showMessageDialog(null, messageFood[1], "Error !", JOptionPane.ERROR_MESSAGE);
                            } else {
                                ConsumableFactory consumableFactory = new ConsumableFactory();
                                Consumable consumeFood = consumableFactory.getInstance("FOOD");
                                consumeFood = new Food(nameItem, noteItem, Double.valueOf(priceItem), Double.valueOf(weightVolumeItem), expiryItem);
                                Item.add(consumeFood);
                                if (consumeFood.getDayExpiry() > 0) {
                                    itemExpired.add(consumeFood);
                                }
                                if (consumeFood.getDayExpiry() <= 0) {
                                    itemNonExpired.add(consumeFood);
                                }
                                if (consumeFood.getDayExpiry() >= -7 && consumeFood.getDayExpiry() <= 0) {
                                    itemExpired7Days.add(consumeFood);
                                }
                                Collections.sort(Item);
                                Collections.sort(itemExpired);
                                Collections.sort(itemNonExpired);
                                Collections.sort(itemExpired7Days);

                                String FoodExpiryDay = String.valueOf(consumeFood.getDayExpiry());
                                String FoodExpiryDate = expiryItem.toString();
                                String FoodType = "FOOD";
                                // Save data to web server
                                String data = "{\"name\" : \"" + nameItem + "\" , " + "\"notes\" : \"" + noteItem + "\" , " + "\"price\" : \"" + priceItem + "\" , " + "\"expiry\" : \"" + FoodExpiryDate + "\" , " + "\"type\" : \"" + FoodType + "\" ," + "\"dayExpiry\" : \"" + FoodExpiryDay + "\" , " + "\"VolumeOrWeight\" : \"" + weightVolumeItem + "\"}";
                                // System.out.println(data);
                                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                                try {
                                    HttpPost request = new HttpPost("http://localhost:8080/addItem");
                                    StringEntity params = new StringEntity(data);
                                    request.addHeader("Content-Type", "application/json");
                                    request.setEntity(params);
                                    request.setEntity(params);
                                    CloseableHttpResponse response = httpClient.execute(request);

                                } catch (Exception ex) {
                                } finally {
                                    // @Deprecated httpClient.getConnectionManager().shutdown();
                                }

                            }
                        } else if (typeItem.equals("Drink")) {
                            if (nameItem.equals("") || priceItem.equals("") || weightVolumeItem.equals("")) {
                                JOptionPane.showMessageDialog(null, messageDrink[0], "Error !", JOptionPane.ERROR_MESSAGE);
                            }
                            if (Double.valueOf(priceItem) < 0 || Double.valueOf(weightVolumeItem) < 0) { // Note here there is error
                                JOptionPane.showMessageDialog(null, messageDrink[1], "Error !", JOptionPane.ERROR_MESSAGE);
                            } else {
                                ConsumableFactory consumableFactory = new ConsumableFactory();
                                Consumable consumeDrink = consumableFactory.getInstance("DRINK");
                                consumeDrink = new Drink(nameItem, noteItem, Double.valueOf(priceItem), Double.valueOf(weightVolumeItem), expiryItem);
                                Item.add(consumeDrink);
                                if (consumeDrink.getDayExpiry() > 0) {
                                    itemExpired.add(consumeDrink);
                                }
                                if (consumeDrink.getDayExpiry() <= 0) {
                                    itemNonExpired.add(consumeDrink);
                                }
                                if (consumeDrink.getDayExpiry() >= -7 && consumeDrink.getDayExpiry() <= 0) {
                                    itemExpired7Days.add(consumeDrink);
                                }
                                Collections.sort(Item);
                                Collections.sort(itemExpired);
                                Collections.sort(itemNonExpired);
                                Collections.sort(itemExpired7Days);

                                String DrinkExpiryDay = String.valueOf(consumeDrink.getDayExpiry());
                                String DrinkExpiryDate = expiryItem.toString();
                                String DrinkType = "DRINK";
                                // Save data to web server
                                String data = "{\"name\" : \"" + nameItem + "\" , " + "\"notes\" : \"" + noteItem + "\" , " + "\"price\" : \"" + priceItem + "\" , " + "\"expiry\" : \"" + DrinkExpiryDate + "\" , " + "\"type\" : \"" + DrinkType + "\" ," + "\"dayExpiry\" : \"" + DrinkExpiryDay + "\" , " + "\"VolumeOrWeight\" : \"" + weightVolumeItem + "\"}";
                                // System.out.println(data);
                                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                                try {
                                    HttpPost request = new HttpPost("http://localhost:8080/addItem");
                                    StringEntity params = new StringEntity(data);
                                    request.addHeader("Content-Type", "application/json");
                                    request.setEntity(params);
                                    CloseableHttpResponse response = httpClient.execute(request);

                                } catch (Exception ex) {
                                } finally {
                                    // @Deprecated httpClient.getConnectionManager().shutdown();
                                }
                            }
                        }
                        textArea.selectAll();
                        textArea.replaceSelection("");
                        for (int i = 0; i < Item.size(); i++) {
                            if (Item.get(i).getType().equals("FOOD")) {
                                textArea.append("Item #" + (i + 1) + " " + Item.get(i).getType());
                                textArea.append("\n");
                                textArea.append("Name: " + Item.get(i).getName());
                                textArea.append("\n");
                                textArea.append("Notes: " + Item.get(i).getNotes());
                                textArea.append("\n");
                                textArea.append("Price: " + Item.get(i).getPrice());
                                textArea.append("\n");
                                textArea.append("Weight: " + Item.get(i).getVolumeOrWeight());
                                textArea.append("\n");
                                textArea.append("Expiry date: " + Item.get(i).getExpiry());
                                textArea.append("\n");
                                if (Item.get(i).getDayExpiry() == 0) {
                                    textArea.append("This food item will expire today.");
                                    textArea.append("\n");
                                    textArea.append("\n");
                                } else if (Item.get(i).getDayExpiry() > 0) {
                                    textArea.append("This food item is expired for " + Item.get(i).getDayExpiry() + " day(s).");
                                    textArea.append("\n");
                                    textArea.append("\n");
                                } else if (Item.get(i).getDayExpiry() < 0) {
                                    textArea.append("This food item will expire in " + Math.abs(Item.get(i).getDayExpiry()) + " day(s).");
                                    textArea.append("\n");
                                    textArea.append("\n");
                                }
                            } else if (Item.get(i).getType().equals("DRINK")) {
                                textArea.append("Item #" + (i + 1) + " " + Item.get(i).getType());
                                textArea.append("\n");
                                textArea.append("Name: " + Item.get(i).getName());
                                textArea.append("\n");
                                textArea.append("Notes: " + Item.get(i).getNotes());
                                textArea.append("\n");
                                textArea.append("Price: " + Item.get(i).getPrice());
                                textArea.append("\n");
                                textArea.append("Volume: " + Item.get(i).getVolumeOrWeight());
                                textArea.append("\n");
                                textArea.append("Expiry date: " + Item.get(i).getExpiry());
                                textArea.append("\n");
                                if (Item.get(i).getDayExpiry() == 0) {
                                    textArea.append("This drink item will expire today.");
                                    textArea.append("\n");
                                    textArea.append("\n");
                                } else if (Item.get(i).getDayExpiry() > 0) {
                                    textArea.append("This drink item is expired for " + Item.get(i).getDayExpiry() + " day(s).");
                                    textArea.append("\n");
                                    textArea.append("\n");
                                } else if (Item.get(i).getDayExpiry() < 0) {
                                    textArea.append("This drink item will expire in " + Math.abs(Item.get(i).getDayExpiry()) + " day(s).");
                                    textArea.append("\n");
                                    textArea.append("\n");
                                }
                            }
                        }
                        dispose();
                    }
                });
                btnCreate.setBounds(70, 500, 74, 23);
                getContentPane().add(btnCreate);

                // This button is to cancel current operation
                JButton btnCancel = new JButton("Cancel");
                btnCancel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
                btnCancel.setBounds(158, 500, 74, 23);
                getContentPane().add(btnCancel);
            }
        }

        btnAddItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddItem addItem = new AddItem();
                addItem.setModal(true);
                addItem.setVisible(true);

                for (int i = 0; i < Item.size(); i++) {
                    System.out.println("Item #" + (i + 1));
                    System.out.println(Item.get(i));
                    System.out.println();
                }

            }
        });
        getContentPane().add(btnAddItem);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollFrame = new JScrollPane(textArea);
        scrollFrame.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollFrame.setBounds(55, 200, 500, 300);
        scrollFrame.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        getContentPane().add(scrollFrame);

        // This button is to list all items
        JButton btnAll = new JButton();
        btnAll.setText("All");
        btnAll.setBounds(70, 80, 50, 50);
        btnAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.selectAll();
                textArea.replaceSelection("");
                if (Item.size() == 0) {
                    textArea.append("No items to show.");
                    textArea.append("\n");
                }

                for (int i = 0; i < Item.size(); i++) {
                    if (Item.get(i).getType().equals("FOOD")) {
                        textArea.append("Item #" + (i + 1) + " " + Item.get(i).getType());
                        textArea.append("\n");
                        textArea.append("Name: " + Item.get(i).getName());
                        textArea.append("\n");
                        textArea.append("Notes: " + Item.get(i).getNotes());
                        textArea.append("\n");
                        textArea.append("Price: " + Item.get(i).getPrice());
                        textArea.append("\n");
                        textArea.append("Weight: " + Item.get(i).getVolumeOrWeight());
                        textArea.append("\n");
                        textArea.append("Expiry date: " + Item.get(i).getExpiry());
                        textArea.append("\n");
                        if (Item.get(i).getDayExpiry() == 0) {
                            textArea.append("This food item will expire today.");
                            textArea.append("\n");
                            textArea.append("\n");
                        } else if (Item.get(i).getDayExpiry() > 0) {
                            textArea.append("This food item is expired for " + Item.get(i).getDayExpiry() + " day(s).");
                            textArea.append("\n");
                            textArea.append("\n");
                        } else if (Item.get(i).getDayExpiry() < 0) {
                            textArea.append("This food item will expire in " + Math.abs(Item.get(i).getDayExpiry()) + " day(s).");
                            textArea.append("\n");
                            textArea.append("\n");
                        }
                    } else if (Item.get(i).getType().equals("DRINK")) {
                        textArea.append("Item #" + (i + 1) + " " + Item.get(i).getType());
                        textArea.append("\n");
                        textArea.append("Name: " + Item.get(i).getName());
                        textArea.append("\n");
                        textArea.append("Notes: " + Item.get(i).getNotes());
                        textArea.append("\n");
                        textArea.append("Price: " + Item.get(i).getPrice());
                        textArea.append("\n");
                        textArea.append("Volume: " + Item.get(i).getVolumeOrWeight());
                        textArea.append("\n");
                        textArea.append("Expiry date: " + Item.get(i).getExpiry());
                        textArea.append("\n");
                        if (Item.get(i).getDayExpiry() == 0) {
                            textArea.append("This drink item will expire today.");
                            textArea.append("\n");
                            textArea.append("\n");
                        } else if (Item.get(i).getDayExpiry() > 0) {
                            textArea.append("This drink item is expired for " + Item.get(i).getDayExpiry() + " day(s).");
                            textArea.append("\n");
                            textArea.append("\n");
                        } else if (Item.get(i).getDayExpiry() < 0) {
                            textArea.append("This drink item will expire in " + Math.abs(Item.get(i).getDayExpiry()) + " day(s).");
                            textArea.append("\n");
                            textArea.append("\n");
                        }
                    }
                }
            }
        });
        getContentPane().add(btnAll);

        // This button is to list all expired items
        JButton btnExpired = new JButton();
        btnExpired.setText("Expired");
        btnExpired.setBounds(130, 80, 90, 50);
        btnExpired.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int generatedNumber = 0;
                textArea.selectAll();
                textArea.replaceSelection("");

                if (itemExpired.size() == 0) {
                    textArea.append("No expired items to show.");
                    textArea.append("\n");
                }

                for (int i = 0; i < itemExpired.size(); i++) {
                    generatedNumber = generatedNumber + 1;
                    if (itemExpired.get(i).getType().equals("FOOD")) {
                        textArea.append("Item #" + generatedNumber + " " + itemExpired.get(i).getType());
                        textArea.append("\n");
                        textArea.append("Name: " + itemExpired.get(i).getName());
                        textArea.append("\n");
                        textArea.append("Notes: " + itemExpired.get(i).getNotes());
                        textArea.append("\n");
                        textArea.append("Price: " + itemExpired.get(i).getPrice());
                        textArea.append("\n");
                        textArea.append("Weight: " + itemExpired.get(i).getVolumeOrWeight());
                        textArea.append("\n");
                        textArea.append("Expiry date: " + itemExpired.get(i).getExpiry());
                        textArea.append("\n");
                        textArea.append("This food item is expired for " + itemExpired.get(i).getDayExpiry() + " day(s).");
                        textArea.append("\n");
                        textArea.append("\n");
                    } else if (itemExpired.get(i).getType().equals("DRINK")) {
                        textArea.append("Item #" + generatedNumber + " " + itemExpired.get(i).getType());
                        textArea.append("\n");
                        textArea.append("Name: " + itemExpired.get(i).getName());
                        textArea.append("\n");
                        textArea.append("Notes: " + itemExpired.get(i).getNotes());
                        textArea.append("\n");
                        textArea.append("Price: " + itemExpired.get(i).getPrice());
                        textArea.append("\n");
                        textArea.append("Weight: " + itemExpired.get(i).getVolumeOrWeight());
                        textArea.append("\n");
                        textArea.append("Expiry date: " + itemExpired.get(i).getExpiry());
                        textArea.append("\n");
                        textArea.append("This drink item is expired for " + itemExpired.get(i).getDayExpiry() + " day(s).");
                        textArea.append("\n");
                        textArea.append("\n");
                    }
                }
            }
        });
        getContentPane().add(btnExpired);

        // This button is to list all non-expired items
        JButton btnNotExpired = new JButton();
        btnNotExpired.setText("Not Expired");
        btnNotExpired.setBounds(230, 80, 110, 50);
        btnNotExpired.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int generatedNumber = 0;
                textArea.selectAll();
                textArea.replaceSelection("");

                if (itemNonExpired.size() == 0) {
                    textArea.append("No non-expired items to show.");
                    textArea.append("\n");
                }
                for (int i = 0; i < itemNonExpired.size(); i++) {
                    generatedNumber = generatedNumber + 1;
                    if (itemNonExpired.get(i).getType().equals("FOOD")) {
                        textArea.append("Item #" + generatedNumber + " " + itemNonExpired.get(i).getType());
                        textArea.append("\n");
                        textArea.append("Name: " + itemNonExpired.get(i).getName());
                        textArea.append("\n");
                        textArea.append("Notes: " + itemNonExpired.get(i).getNotes());
                        textArea.append("\n");
                        textArea.append("Price: " + itemNonExpired.get(i).getPrice());
                        textArea.append("\n");
                        textArea.append("Weight: " + itemNonExpired.get(i).getVolumeOrWeight());
                        textArea.append("\n");
                        textArea.append("Expiry date: " + itemNonExpired.get(i).getExpiry());
                        textArea.append("\n");
                        if (itemNonExpired.get(i).getDayExpiry() == 0) {
                            textArea.append("This food item will expire today.");
                            textArea.append("\n");
                            textArea.append("\n");
                        } else if (itemNonExpired.get(i).getDayExpiry() < 0) {
                            textArea.append("This food item will expire in " + Math.abs(itemNonExpired.get(i).getDayExpiry()) + " day(s).");
                            textArea.append("\n");
                            textArea.append("\n");
                        }
                    } else if (itemNonExpired.get(i).getType().equals("DRINK")) {
                        textArea.append("Item #" + generatedNumber + " " + itemNonExpired.get(i).getType());
                        textArea.append("\n");
                        textArea.append("Name: " + itemNonExpired.get(i).getName());
                        textArea.append("\n");
                        textArea.append("Notes: " + itemNonExpired.get(i).getNotes());
                        textArea.append("\n");
                        textArea.append("Price: " + itemNonExpired.get(i).getPrice());
                        textArea.append("\n");
                        textArea.append("Volume: " + itemNonExpired.get(i).getVolumeOrWeight());
                        textArea.append("\n");
                        textArea.append("Expiry date: " + itemNonExpired.get(i).getExpiry());
                        textArea.append("\n");
                        if (itemNonExpired.get(i).getDayExpiry() == 0) {
                            textArea.append("This drink item will expire today.");
                            textArea.append("\n");
                            textArea.append("\n");
                        } else if (itemNonExpired.get(i).getDayExpiry() < 0) {
                            textArea.append("This drink item will expire in " + Math.abs(itemNonExpired.get(i).getDayExpiry()) + " day(s).");
                            textArea.append("\n");
                            textArea.append("\n");
                        }
                    }

                }
            }
        });
        getContentPane().add(btnNotExpired);

        // This button is to list all expiring items in 7 days
        JButton btnExpiring7Days = new JButton();
        btnExpiring7Days.setText("Expiring in 7 Days");
        btnExpiring7Days.setBounds(350, 80, 150, 50);
        btnExpiring7Days.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int generatedNumber = 0;
                textArea.selectAll();
                textArea.replaceSelection("");

                if (itemExpired7Days.size() == 0) {
                    textArea.append("No items expiring in 7 days to show.");
                    textArea.append("\n");
                }
                for (int i = 0; i < itemExpired7Days.size(); i++) {
                    generatedNumber = generatedNumber + 1;
                    if (itemExpired7Days.get(i).getType().equals("FOOD")) {
                        textArea.append("Item #" + generatedNumber + " " + itemExpired7Days.get(i).getType());
                        textArea.append("\n");
                        textArea.append("Name: " + itemExpired7Days.get(i).getName());
                        textArea.append("\n");
                        textArea.append("Notes: " + itemExpired7Days.get(i).getNotes());
                        textArea.append("\n");
                        textArea.append("Price: " + itemExpired7Days.get(i).getPrice());
                        textArea.append("\n");
                        textArea.append("Weight: " + itemExpired7Days.get(i).getVolumeOrWeight());
                        textArea.append("\n");
                        textArea.append("Expiry date: " + itemExpired7Days.get(i).getExpiry());
                        textArea.append("\n");
                        if (itemExpired7Days.get(i).getDayExpiry() == 0) {
                            textArea.append("This food item will expire today.");
                            textArea.append("\n");
                            textArea.append("\n");
                        } else if (itemExpired7Days.get(i).getDayExpiry() < 0) {
                            textArea.append("This food item will expire in " + Math.abs(itemExpired7Days.get(i).getDayExpiry()) + " day(s).");
                            textArea.append("\n");
                            textArea.append("\n");
                        }
                    } else if (itemExpired7Days.get(i).getType().equals("DRINK")) {
                        textArea.append("Item #" + generatedNumber + " " + itemExpired7Days.get(i).getType());
                        textArea.append("\n");
                        textArea.append("Name: " + itemExpired7Days.get(i).getName());
                        textArea.append("\n");
                        textArea.append("Notes: " + itemExpired7Days.get(i).getNotes());
                        textArea.append("\n");
                        textArea.append("Price: " + itemExpired7Days.get(i).getPrice());
                        textArea.append("\n");
                        textArea.append("Volume: " + itemExpired7Days.get(i).getVolumeOrWeight());
                        textArea.append("\n");
                        textArea.append("Expiry date: " + itemExpired7Days.get(i).getExpiry());
                        textArea.append("\n");
                        if (itemExpired7Days.get(i).getDayExpiry() == 0) {
                            textArea.append("This drink item will expire today.");
                            textArea.append("\n");
                            textArea.append("\n");
                        } else if (itemExpired7Days.get(i).getDayExpiry() < 0) {
                            textArea.append("This drink item will expire in " + Math.abs(itemExpired7Days.get(i).getDayExpiry()) + " day(s).");
                            textArea.append("\n");
                            textArea.append("\n");
                        }
                    }

                }
            }
        });
        getContentPane().add(btnExpiring7Days);

        class Remove extends JDialog {
            public Remove() {
                setResizable(false);
                setTitle("Remove");
                setBounds(200, 300, 450, 300);
                setLocationRelativeTo(null);
                getContentPane().setLayout(null);

                JLabel question = new JLabel("Please choose the item number you want to remove: ");
                question.setBounds(70, 40, 500, 23);
                getContentPane().add(question);
                JTextField answer = new JTextField();
                answer.setBounds(170, 90, 100, 23);
                getContentPane().add(answer);

                JButton OK = new JButton("OK");
                OK.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String textAnswer = answer.getText();
                        int numTextAnswer = Integer.parseInt(textAnswer);
                        if (numTextAnswer <= 0 || numTextAnswer > Item.size()) {
                            JOptionPane.showMessageDialog(null, "Your item number does not exist !", "Error !", JOptionPane.ERROR_MESSAGE);
                        } else {


                            try {

                                Long ID = Item.get(numTextAnswer - 1).getId();
                                String number = String.valueOf(ID);
                                String connection = "http://localhost:8080/removeItem/";
                                String web = connection.concat(number);
                                URL url = new URL(web);
                                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                                http.setRequestMethod("POST");
                                http.setDoOutput(true);
                                http.setRequestProperty("Content-Type", "application/json");
                                http.setRequestProperty("Content-Length", "0");
                                System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
                                http.disconnect();
                                Item.clear();
                                itemNonExpired.clear();
                                itemExpired.clear();
                                itemExpired7Days.clear();
                                ManageItems manage = new ManageItems();
                                manage.readingBuffer("http://localhost:8080/listAll", Item);
                                manage.readingBuffer("http://localhost:8080/listExpired", itemExpired);
                                manage.readingBuffer("http://localhost:8080/listNonExpired", itemNonExpired);
                                manage.readingBuffer("http://localhost:8080/listExpiringIn7Days", itemExpired7Days);
                            } catch (Exception ex) {
                            } finally {
                                // @Deprecated httpClient.getConnectionManager().shutdown();
                            }
                        }

                        textArea.selectAll();
                        textArea.replaceSelection("");
                        if (Item.size() == 0) {
                            textArea.append("No items to show.");
                            textArea.append("\n");
                        }

                        for (int i = 0; i < Item.size(); i++) {
                            if (Item.get(i).getType().equals("FOOD")) {
                                textArea.append("Item #" + (i + 1) + " " + Item.get(i).getType());
                                textArea.append("\n");
                                textArea.append("Name: " + Item.get(i).getName());
                                textArea.append("\n");
                                textArea.append("Notes: " + Item.get(i).getNotes());
                                textArea.append("\n");
                                textArea.append("Price: " + Item.get(i).getPrice());
                                textArea.append("\n");
                                textArea.append("Weight: " + Item.get(i).getVolumeOrWeight());
                                textArea.append("\n");
                                textArea.append("Expiry date: " + Item.get(i).getExpiry());
                                textArea.append("\n");
                                if (Item.get(i).getDayExpiry() == 0) {
                                    textArea.append("This food item will expire today.");
                                    textArea.append("\n");
                                    textArea.append("\n");
                                } else if (Item.get(i).getDayExpiry() > 0) {
                                    textArea.append("This food item is expired for " + Item.get(i).getDayExpiry() + " day(s).");
                                    textArea.append("\n");
                                    textArea.append("\n");
                                } else if (Item.get(i).getDayExpiry() < 0) {
                                    textArea.append("This food item will expire in " + Math.abs(Item.get(i).getDayExpiry()) + " day(s).");
                                    textArea.append("\n");
                                    textArea.append("\n");
                                }
                            } else if (Item.get(i).getType().equals("DRINK")) {
                                textArea.append("Item #" + (i + 1) + " " + Item.get(i).getType());
                                textArea.append("\n");
                                textArea.append("Name: " + Item.get(i).getName());
                                textArea.append("\n");
                                textArea.append("Notes: " + Item.get(i).getNotes());
                                textArea.append("\n");
                                textArea.append("Price: " + Item.get(i).getPrice());
                                textArea.append("\n");
                                textArea.append("Volume: " + Item.get(i).getVolumeOrWeight());
                                textArea.append("\n");
                                textArea.append("Expiry date: " + Item.get(i).getExpiry());
                                textArea.append("\n");
                                if (Item.get(i).getDayExpiry() == 0) {
                                    textArea.append("This drink item will expire today.");
                                    textArea.append("\n");
                                    textArea.append("\n");
                                } else if (Item.get(i).getDayExpiry() > 0) {
                                    textArea.append("This drink item is expired for " + Item.get(i).getDayExpiry() + " day(s).");
                                    textArea.append("\n");
                                    textArea.append("\n");
                                } else if (Item.get(i).getDayExpiry() < 0) {
                                    textArea.append("This drink item will expire in " + Math.abs(Item.get(i).getDayExpiry()) + " day(s).");
                                    textArea.append("\n");
                                    textArea.append("\n");
                                }
                            }
                        }
                        dispose();
                    }
                });
                OK.setBounds(185, 160, 70, 23);
                getContentPane().add(OK);
            }
        }

        // This button is to remove an item
        JButton btnRemove = new JButton();
        btnRemove.setText("Remove");
        btnRemove.setBounds(250, 140, 100, 50);
        btnRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Remove removeItem = new Remove();
                removeItem.setModal(true);
                removeItem.setVisible(true);
            }
        });
        getContentPane().add(btnRemove);
        // Create Close behavior and store data of items into JSON file
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
    }
}// ConsumablesTracker.java

