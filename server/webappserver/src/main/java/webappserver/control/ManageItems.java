/**
 * A class is used to manage items in JSON file.
 *
 * @author MinhPhatTran
 */
package ca.cmpt213.a4.webappserver.control;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import java.time.*;
import java.io.*;
import java.util.*;
import ca.cmpt213.a4.webappserver.model.*;

public class ManageItems {
    // Reading the data stored in JSON file
    public void Reading(String FileName, ArrayList<Item> array) {
        File inputFile = new File(FileName);
        try {
            JsonElement fileElement = JsonParser.parseReader(new FileReader(inputFile));
            JsonArray fileObject = fileElement.getAsJsonArray();

            for (JsonElement ListItem : fileObject) {
                JsonObject IteminList = ListItem.getAsJsonObject();
                long ID = IteminList.get("ID").getAsLong();
                String name = IteminList.get("Name").getAsString();
                String type = IteminList.get("Type").getAsString();
                String note = IteminList.get("Notes").getAsString();
                double price = IteminList.get("Price").getAsDouble();
                double weightOrVolume = IteminList.get("WeightOrVolume").getAsDouble();
                String date = IteminList.get("Expiry Date").getAsString();
                LocalDate expiry = LocalDate.parse(date);
                array.add(new Item(ID,type,name,note,price,weightOrVolume,expiry));
            }
        } catch (FileNotFoundException e) {
        }
    }

    // Writing the data to JSON file
    public void Writing(String FileName, ArrayList<Item> array) {
        try (FileWriter fileWriter = new FileWriter(FileName);
             JsonWriter jsonWriter = new JsonWriter(fileWriter)) {
            jsonWriter.beginArray();

            for (int i = 0; i < array.size(); i++) {
                jsonWriter.beginObject();
                jsonWriter.name("ID").value(array.get(i).getId());
                jsonWriter.name("Name").value(array.get(i).getName());
                jsonWriter.name("Type").value(array.get(i).getType());
                jsonWriter.name("Notes").value(array.get(i).getNotes());
                jsonWriter.name("Price").value(array.get(i).getPrice());
                jsonWriter.name("WeightOrVolume").value(array.get(i).getVolumeOrWeight());
                jsonWriter.name("Expiry Date").value(array.get(i).getExpiry().toString());
                jsonWriter.endObject();
            }
            jsonWriter.endArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}// ManageItems.java
