package model;

import model.exceptions.EmptyEntryException;
import model.exceptions.NoSuchItemException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ClosetTest {
    private Closet testCloset1;  // empty closet
    private Closet testCloset2;  // closet with 1 item
    private Closet testCloset3;  // closet with 2 items and 2 brands
    private Closet testCloset4;  // closet with 2 items and 1 brand
    private Closet testCloset5;  // closet with 3 items

    private Clothing testItem1;
    private Clothing testItem2;
    private Clothing testItem3;

    private ArrayList<String> testClothingList;

    @BeforeEach
    public void runBefore() throws EmptyEntryException {
        testItem1 = new Clothing("Yellow Shirt", 10, "Happy Brand");
        testItem2 = new Clothing("Blue Shirt", 15, "Sad Brand");
        testItem3 = new Clothing("Red Shirt", 25, "Happy Brand");

        testCloset1 = new Closet("Test Closet 1");

        testCloset2 = new Closet("Test Closet 2");
        testCloset2.addClothingItem(testItem1);

        testCloset3 = new Closet("Test Closet 3");
        testCloset3.addClothingItem(testItem1);
        testCloset3.addClothingItem(testItem2);

        testCloset4 = new Closet("Test Closet 4");
        testCloset4.addClothingItem(testItem1);
        testCloset4.addClothingItem(testItem3);

        testCloset5 = new Closet("Test Closet 5");
        testCloset5.addClothingItem(testItem1);
        testCloset5.addClothingItem(testItem2);
        testCloset5.addClothingItem(testItem3);

        testClothingList = new ArrayList<>();
        testClothingList.add("Yellow Shirt");
        testClothingList.add("Blue Shirt");
        testClothingList.add("Red Shirt");
    }

    // Test case where exception is thrown in constructor
    @Test
    public void excEmptyEntryThrownCloset() {
        try {
            new Closet("");
            fail("I was not expecting to reach this line of code");
        } catch (EmptyEntryException e) {
            System.out.println("great!");
        }
    }

    // Test case where exception is not thrown in constructor
    @Test
    public void excEmptyEntryNotThrownCloset() {
        try {
            new Closet("Test Exception");
        } catch (EmptyEntryException e) {
            fail("I was not expecting to EmptyEntryException");
        }
    }

    // Test case where one clothing item is added to an empty closet
    @Test
    public void testAddClothingItem() {
        assertEquals("Item added to closet", testCloset1.addClothingItem(testItem1));
        String testItemName = testCloset1.returnClothingList().get(0);
        Brand testBrandNameItem = testCloset1.getListOfBrands().get(0);

        assertEquals(1, testCloset1.returnClothingList().size());
        assertEquals(1, testCloset1.getListOfBrands().size());

        assertEquals("Yellow Shirt", testItemName);
        assertEquals("Happy Brand", testBrandNameItem.getBrandName());
    }

    // test case where two clothing items are added to an empty closet with two different brands
    @Test
    public void testMultipleItemsAdded() {
        testCloset1.addClothingItem(testItem1);
        testCloset1.addClothingItem(testItem2);

        String testItemName1 = testCloset1.returnClothingList().get(0);
        Brand testBrandName1Item = testCloset1.getListOfBrands().get(0);

        String testItemName2 = testCloset1.returnClothingList().get(1);
        Brand testBrandName2Item = testCloset1.getListOfBrands().get(1);

        assertEquals(2, testCloset1.returnClothingList().size());
        assertEquals(2, testCloset1.getListOfBrands().size());

        assertEquals("Yellow Shirt", testItemName1);
        assertEquals("Happy Brand", testBrandName1Item.getBrandName());

        assertEquals("Blue Shirt", testItemName2);
        assertEquals("Sad Brand", testBrandName2Item.getBrandName());
    }

    // test case where two clothing items are added to an empty closet with two different brands
    @Test
    public void testMultipleItemsAddedSameBrand() {
        testCloset1.addClothingItem(testItem1);
        testCloset1.addClothingItem(testItem3);

        String testItemName1 = testCloset1.returnClothingList().get(0);
        Brand testBrandName1Item = testCloset1.getListOfBrands().get(0);

        String testItemName3 = testCloset1.returnClothingList().get(1);

        assertEquals(2, testCloset1.returnClothingList().size());
        assertEquals(1, testCloset1.getListOfBrands().size());

        assertEquals("Yellow Shirt", testItemName1);
        assertEquals("Happy Brand", testBrandName1Item.getBrandName());

        assertEquals("Red Shirt", testItemName3);
    }

    // test case where remove a clothing item from a closet with 1 item and 1 brand
    @Test
    public void testRemoveClothingItem() throws EmptyEntryException, NoSuchItemException {
        assertEquals("Item removed from closet", testCloset2.removeClothingItem("Yellow Shirt"));

        assertEquals(0, testCloset2.returnClothingList().size());
        assertEquals(0, testCloset2.getListOfBrands().size());
    }

    // test case where remove a clothing item from a closet with 2 items and 2 brands
    @Test
    public void testRemoveClothingMultiItemTwoBrands() throws EmptyEntryException, NoSuchItemException {
        assertEquals("Item removed from closet", testCloset3.removeClothingItem("Yellow Shirt"));
        String testItemName = testCloset3.returnClothingList().get(0);
        Brand testBrandNameItem = testCloset3.getListOfBrands().get(0);

        assertEquals(1, testCloset3.returnClothingList().size());
        assertEquals(1, testCloset3.getListOfBrands().size());

        assertEquals("Blue Shirt", testItemName);
        assertEquals("Sad Brand", testBrandNameItem.getBrandName());
    }

    // test case where remove a clothing item from a closet with 2 items and 1 brand
    @Test
    public void testRemoveClothingMultiItemOneBrand() throws EmptyEntryException, NoSuchItemException {
        assertEquals("Item removed from closet", testCloset4.removeClothingItem("Red Shirt"));
        String testItemName = testCloset4.returnClothingList().get(0);
        Brand testBrandNameItem = testCloset4.getListOfBrands().get(0);

        assertEquals(1, testCloset4.returnClothingList().size());
        assertEquals(1, testCloset4.getListOfBrands().size());

        assertEquals("Yellow Shirt", testItemName);
        assertEquals("Happy Brand", testBrandNameItem.getBrandName());
    }

    // test case where remove 2 clothing items
    @Test
    public void testRemoveClothingMultiItem() throws EmptyEntryException, NoSuchItemException {
        assertEquals("Item removed from closet", testCloset3.removeClothingItem("Yellow Shirt"));
        assertEquals("Item removed from closet", testCloset3.removeClothingItem("Blue Shirt"));

        assertEquals(0, testCloset3.returnClothingList().size());
        assertEquals(0, testCloset3.getListOfBrands().size());
    }

    // test case where a clothing item with given string is returned, expect exception not thrown
    @Test
    public void testFindClothingItem() throws EmptyEntryException, NoSuchItemException {
        assertEquals(testItem3, testCloset5.findClothingItem("Red Shirt"));
        assertEquals(2, testCloset5.returnClothingList().indexOf("Red Shirt"));

        try {
            testCloset5.findClothingItem("Red Shirt");
        } catch (EmptyEntryException e) {
            System.out.println("I was not expecting EmptyEntryException");
        }
    }

    // test case where a clothing item does not exist in non empty list
    @Test
    public void testLostFindClothingItem() throws EmptyEntryException {
        try {
            testCloset5.findClothingItem("Shoes");
            fail("I was not expecting to reach this line of code");
        } catch (NoSuchItemException e) {
            System.out.println("great!");
        }
    }

    // test case where the list of clothing items is empty, expect exception thrown
    @Test
    public void testEmptyFindClothingItem() throws NoSuchItemException {
        try {
            testCloset1.findClothingItem("Red Shirt");
            fail("I was not expecting to reach this line of code");
        } catch (EmptyEntryException e) {
            System.out.println("great!");
        }
    }

    // test case where a brand with given string is returned, expect exception not thrown
    @Test
    public void testFindBrand() throws EmptyEntryException, NoSuchItemException {
        assertEquals("Happy Brand", testCloset5.findBrand("Happy Brand").getBrandName());
        assertEquals(0, testCloset5.returnBrandList().indexOf("Happy Brand"));

        try {
            testCloset5.findBrand("Sad Brand");
        } catch (EmptyEntryException e) {
            System.out.println("I was not expecting EmptyEntryException");
        }
    }

    // test case where the list of brands is not empty, expect nosuchentry thrown
    @Test
    public void testFindBrandLostBrand() throws EmptyEntryException {
        try {
            testCloset5.findBrand("Aritzia");
            fail("I was not expecting to reach this line of code");
        } catch (NoSuchItemException e) {
            System.out.println("great!");
        }
    }

    // test case where the list of brands is empty, expect exception thrown
    @Test
    public void testEmptyFindBrandItem() throws EmptyEntryException {
        try {
            testCloset1.findBrand("Bluenotes");
            fail("I was not expecting to reach this line of code");
        } catch (NoSuchItemException e) {
            System.out.println("great!");
        }
    }

    // test case where a brand with given string as name is returned
    @Test
    public void testFindClothingBrand() {
        Brand testBrand = testCloset5.getListOfBrands().get(0);

        assertEquals(testBrand, testCloset5.findClothingBrand("Happy Brand"));
    }

    // test case where a brand that is second in list with given string as name is returned
    @Test
    public void testFindClothingBrandSecond() {
        Brand testBrand = testCloset5.getListOfBrands().get(1);

        assertEquals(testBrand, testCloset5.findClothingBrand("Sad Brand"));
    }

    // test return clothing list
    @Test
    public void testReturnClothingList() {
        assertEquals(testClothingList, testCloset5.returnClothingList());
    }

    // test get closet name
    @Test
    public void testGetClosetName() {
        assertEquals("Test Closet 1", testCloset1.getClosetName());
    }
}
