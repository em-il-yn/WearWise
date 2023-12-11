package model;

import model.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClothingTest {

    private Clothing testItem1;

    @BeforeEach
    public void runBefore() throws EmptyEntryException {
        testItem1 = new Clothing("Yellow Shirt", 10, "Happy Brand");
    }

    // Test case where exception is thrown in constructor based on name
    @Test
    public void excTestThrownClothingName() {
        try {
            new Clothing("", 10, "Aritzia");
            fail("I was not expecting to reach this line of code");
        } catch (EmptyEntryException e) {
            System.out.println("great!");
        }
    }

    // Test case where exception is thrown in constructor based on brand
    @Test
    public void excTestThrownClothingBrand() {
        try {
            new Clothing("Shirt", 10, "");
            fail("I was not expecting to reach this line of code");
        } catch (EmptyEntryException e) {
            System.out.println("great!");
        }
    }

    // Test case where exception is not thrown in constructor
    @Test
    public void excTestNotThrownClothing() {
        try {
            new Clothing("Shirt", 10, "Aritzia");
        } catch (EmptyEntryException e) {
            fail("I was not expecting to EmptyEntryException");
        }
    }

    // test case where wear count is updated 1 time
    @Test
    public void testWearCount() {
        testItem1.addWearCount();

        assertEquals(1, testItem1.getWearCount());
    }

    // test case where wear count is updated multiple times
    @Test
    public void testWearCountMultiWear() {
        testItem1.addWearCount();
        testItem1.addWearCount();

        assertEquals(2, testItem1.getWearCount());
    }

    // test case where costPerWear is updated for 1 wear
    @Test
    public void testUpdateCostPerWear() {
        testItem1.addWearCount();
        testItem1.updateCostPerWear();

        assertEquals(10, testItem1.getCostPerWear());
    }

    // test case where costPerWear is updated for multiple wears
    @Test
    public void testUpdateCostPerWearMulti() {
        testItem1.addWearCount();
        testItem1.addWearCount();
        testItem1.updateCostPerWear();

        assertEquals(5, testItem1.getCostPerWear());
    }

    // test if cost is returned for getCost
    @Test
    public void testGetCost() {
        assertEquals(10, testItem1.getCost());
    }
}
