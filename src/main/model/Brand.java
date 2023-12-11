package model;

import persistence.Writable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

// sourced from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git

public class Brand implements Writable {

    private String brandName;
    private ArrayList<Clothing> listOfClothes;
    private double averageCostPerWear;
    private double averageWearCount;
    private int numOfItems;

    // EFFECTS: creates brand with empty listOfClothes, empty wearCount, and empty averageCostPerWear
    // MODIFIES: n/a/
    public Brand(String name) {
        brandName = name;
        listOfClothes = new ArrayList<>();
        averageCostPerWear = 0;
        averageWearCount = 0;
        numOfItems = 0;
    }

    // EFFECTS: adds clothing to list of clothing, increases numOfItems by 1
    // MODIFIES: this.listOfClothes
    public void addClothingItem(Clothing item) {
        listOfClothes.add(item);
        numOfItems += 1;
        updateAverages();
    }

    // EFFECTS: updates the averageWearCount and the averageCostPerWear according to new items added to list
    // MODIFIES: this.listOfClothes
    public void updateAverages() {
        double totalCostPerWear = 0;
        double totalWears = 0;

        for (Clothing item: this.listOfClothes) {
            totalCostPerWear = totalCostPerWear + item.getCostPerWear();
            totalWears = totalWears + item.getWearCount();
        }
        averageWearCount = totalWears / numOfItems;
        averageCostPerWear = totalCostPerWear / numOfItems;
    }

    public int getNumOfItems() {
        return numOfItems;
    }

    public ArrayList<Clothing> getListOfClothes() {
        return listOfClothes;
    }

    public String getBrandName() {
        return brandName;
    }

    public double getAverageCostPerWear() {
        return averageCostPerWear;
    }

    public double getAverageWearCount() {
        return averageWearCount;
    }

    // sourced from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("brandName", brandName);
        json.put("listOfClothes", brandClosetItemsToJson());
        json.put("averageCostPerWear", getAverageCostPerWear());
        json.put("numOfItems", getNumOfItems());
        return json;
    }

    // EFFECTS: returns clothing items in this brand as a JSON array
    private JSONArray brandClosetItemsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Clothing c : listOfClothes) {
            jsonArray.put(c.toJson());
        }

        return jsonArray;
    }
}
