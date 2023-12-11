package persistence;

import model.Closet;
import model.Clothing;
import model.exceptions.EmptyEntryException;
import org.json.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

// sourced from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
// Represents a reader that reads closet from JSON data stored in file
public class JsonReader {
    private String source;
    private Scanner scanner;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads closet from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Closet read() throws IOException, EmptyEntryException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseCloset(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses closet from JSON object and returns it
    private Closet parseCloset(JSONObject jsonObject) throws EmptyEntryException {
        String name = jsonObject.getString("name");
        Closet cl = new Closet(name);
        addClothingItems(cl, jsonObject);
        return cl;
    }

    // MODIFIES: cl
    // EFFECTS: parses clothing items from JSON object and adds them to closet
    private void addClothingItems(Closet cl, JSONObject jsonObject) throws EmptyEntryException {
        JSONArray jsonArray = jsonObject.getJSONArray("listOfClothes");
        for (Object json : jsonArray) {
            JSONObject nextClothingItem = (JSONObject) json;
            addClothingItem(cl, nextClothingItem);
        }
    }

    // MODIFIES: cl
    // EFFECTS: parses thingy from JSON object and adds it to closet
    private void addClothingItem(Closet cl, JSONObject jsonObject) throws EmptyEntryException {
        String itemName = jsonObject.getString("itemName");
        double cost = jsonObject.getDouble("cost");
        String brand = jsonObject.getString("brand");
        Clothing clothingItem = new Clothing(itemName, cost, brand);
        clothingItem.setCostPerWear(jsonObject.getDouble("costPerWear"));
        clothingItem.setWearCount(jsonObject.getInt("wearCount"));
        cl.addClothingItem(clothingItem);
    }
}