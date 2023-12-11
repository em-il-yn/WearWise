package model;

import model.exceptions.EmptyEntryException;
import persistence.Writable;
import org.json.JSONObject;


public class Clothing implements Writable {
    private String itemName;
    private double cost;
    private int wearCount;
    private String brand;
    private double costPerWear;

    // EFFECTS: creates ClothingItem with cost, wearCount, brand, and costPerWear
    public Clothing(String itemName, double cost, String brand) throws EmptyEntryException {
        if (itemName.isEmpty() | brand.isEmpty()) {
            throw new EmptyEntryException();
        }

        this.itemName = itemName;
        this.cost = cost;
        this.brand = brand;
        this.costPerWear = 0;
        this.wearCount = 0;
    }

    // EFFECTS: adds 1 to wearCount
    // MODIFIES: this.wearCount
    public void addWearCount() {
        wearCount = wearCount + 1;
        updateCostPerWear();
    }

    // REQUIRES: n/a
    // EFFECTS: updates cost per wear
    // MODIFIES: this.costPerWear
    public void updateCostPerWear() {
        costPerWear = cost / wearCount;
    }

    public String getBrand() {
        return brand;
    }

    public int getWearCount() {
        return wearCount;
    }

    public double getCostPerWear() {
        return costPerWear;
    }

    public String getItemName() {
        return itemName;
    }

    public double getCost() {
        return cost;
    }

    public void setCostPerWear(double cpw) {
        costPerWear = cpw;
    }

    public void setWearCount(int wc) {
        wearCount = wc;
    }

    // sourced from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("itemName", itemName);
        json.put("cost", cost);
        json.put("brand", brand);
        json.put("costPerWear", getCostPerWear());
        json.put("wearCount", getWearCount());
        return json;
    }
}
