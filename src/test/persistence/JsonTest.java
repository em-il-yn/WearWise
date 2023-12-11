package persistence;

import model.Brand;
import model.Clothing;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

// sourced from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
public class JsonTest {
    protected void checkClothing(String itemName, double cost, int wearCount, String brand, double costPerWear, Clothing clothingItem) {
        assertEquals(itemName, clothingItem.getItemName());
        assertEquals(cost, clothingItem.getCost());
        assertEquals(wearCount, clothingItem.getWearCount());
        assertEquals(brand, clothingItem.getBrand());
        assertEquals(costPerWear, clothingItem.getCostPerWear());
    }

    protected void checkBrand(String brandName, double averageCostPerWear, double averageWearCount, int numOfItems, Brand brand) {
        assertEquals(brandName, brand.getBrandName());
        assertEquals(averageCostPerWear, brand.getAverageCostPerWear());
        assertEquals(averageWearCount, brand.getAverageWearCount());
        assertEquals(numOfItems, brand.getNumOfItems());
    }
}
