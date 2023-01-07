/**
 * A class is used to manage items saved in web server as well as JSON file
 *
 * @author MinhPhatTran
 */
package ca.cmpt213.a4.client.control;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.*;
import java.io.*;
import java.util.*;
import ca.cmpt213.a4.client.model.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import org.json.JSONArray;
import org.json.JSONObject;

public class ManageItems {
    // Reading all items stored in web server
    public void readingBuffer (String connection, ArrayList<Consumable>array) throws IOException {
        String url = connection;
        URL obj = new URL(url);
        HttpURLConnection link = (HttpURLConnection) obj.openConnection();
        link.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        String data = response.toString();
        JSONArray jArray = new JSONArray(data);
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject jObject = jArray.getJSONObject(i);
            String name = jObject.getString("name");
            Long id = jObject.getLong("id");
            String note = jObject.getString("notes");
            Double price = jObject.getDouble("price");
            String expiryDate = jObject.getString("expiry");
            String type = jObject.getString("type");
            Double dayExpiry = jObject.getDouble("dayExpiry");
            Double VolumeOrWeight = jObject.getDouble("volumeOrWeight");
            LocalDate expiry = LocalDate.parse(expiryDate);
            Consumable temp = new Consumable();
            temp.setId(id);
            temp.setName(name);
            temp.setNotes(note);
            temp.setPrice(price);
            temp.setType(type);
            temp.setExpiry(expiry);
            temp.setVolumeOrWeight(VolumeOrWeight);
            array.add(temp);
        }
    }

    // Reading the data stored in JSON file, we do not use this function for this assignment
    public void Reading(String FileName, ArrayList<Consumable> array) {
        File inputFile = new File(FileName);
        try {
            JsonElement fileElement = JsonParser.parseReader(new FileReader(inputFile));
            JsonArray fileObject = fileElement.getAsJsonArray();

            for (JsonElement ListItem : fileObject) {
                JsonObject IteminList = ListItem.getAsJsonObject();
                String name = IteminList.get("Name").getAsString();
                String type = IteminList.get("Type").getAsString();
                String note = IteminList.get("Notes").getAsString();
                double price = IteminList.get("Price").getAsDouble();
                double weightOrVolume = IteminList.get("WeightOrVolume").getAsDouble();
                String date = IteminList.get("Expiry Date").getAsString();
                LocalDate expiry = LocalDate.parse(date);
                if (type.equals("FOOD")) {
                    array.add(new Food(name, note, price, weightOrVolume, expiry));
                } else if (type.equals("DRINK")) {
                    array.add(new Drink(name, note, price, weightOrVolume, expiry));
                }
            }
        } catch (FileNotFoundException e) {
        }
    }

    // Writing the data to JSON file, we do not use this function for this assignment
    public void Writing(String FileName, ArrayList<Consumable> array) {
        try (FileWriter fileWriter = new FileWriter(FileName);
             JsonWriter jsonWriter = new JsonWriter(fileWriter)) {
            jsonWriter.beginArray();

            for (int i = 0; i < array.size(); i++) {
                jsonWriter.beginObject();
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
