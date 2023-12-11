package model;

import model.exceptions.EmptyEntryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BrandTest {

    private Brand testBrand1Item;         // empty brand
    private Brand testBrand2Item;         // brand with 1 items
    private Brand testBrand3Item;         // brand with 2 items

    private Clothing testItem1;
    private Clothing testItem2;
    private Clothing testItem3;

    @BeforeEach
    public void runBefore() throws EmptyEntryException {
        testItem1 = new Clothing("Yellow Shirt", 10, "Happy Brand");
        testItem1.addWearCount();
        testItem1.addWearCount();

        testItem2 = new Clothing("Blue Shirt", 30, "Happy Brand");
        testItem2.addWearCount();
        testItem2.addWearCount();
        testItem2.addWearCount();

        testItem3 = new Clothing("Red Shirt", 25, "Happy Brand");

        testBrand1Item = new Brand("Happy Store");

        testBrand2Item = new Brand("Happy Store");
        testBrand2Item.addClothingItem(testItem1);

        testBrand3Item = new Brand("Happy Store");
        testBrand3Item.addClothingItem(testItem1);
        testBrand3Item.addClothingItem(testItem2);
    }

    // test case where you add 1 item to an empty brand
    @Test
    public void testAddClothingItem() {
        testBrand1Item.addClothingItem(testItem1);
        Clothing testName1 = (Clothing) testBrand1Item.getListOfClothes().get(0);

        assertEquals(1, testBrand1Item.getListOfClothes().size());
        assertEquals(1, testBrand1Item.getNumOfItems());
        assertEquals("Yellow Shirt", testName1.getItemName());
    }

    // test case where you add multiple items to an empty brand
    @Test
    public void testAddClothingItemMultiItem() {
        testBrand1Item.addClothingItem(testItem1);
        testBrand1Item.addClothingItem(testItem2);
        Clothing testName1 = (Clothing) testBrand1Item.getListOfClothes().get(0);
        Clothing testName2 = (Clothing) testBrand1Item.getListOfClothes().get(1);

        assertEquals(2, testBrand1Item.getListOfClothes().size());
        assertEquals(2, testBrand1Item.getNumOfItems());
        assertEquals("Yellow Shirt", testName1.getItemName());
        assertEquals("Blue Shirt", testName2.getItemName());
    }

    // test case where averages are updated from 0
    @Test
    public void testUpdateAverages() {
        testBrand1Item.addClothingItem(testItem1);
        testBrand1Item.updateAverages();

        assertEquals(5.0, testBrand1Item.getAverageCostPerWear());
        assertEquals(2.0, testBrand1Item.getAverageWearCount());
    }

    // test case where averages are updated from >0
    @Test
    public void testUpdateAveragesFromNotZero() {
        testBrand1Item.addClothingItem(testItem1);
        testBrand1Item.addClothingItem(testItem2);
        testBrand1Item.updateAverages();

        assertEquals(7.5, testBrand1Item.getAverageCostPerWear());
        assertEquals(2.5, testBrand1Item.getAverageWearCount());
    }
}