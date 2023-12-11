package persistence;

import model.Closet;
import model.Clothing;
import model.Brand;
import model.exceptions.EmptyEntryException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// sourced from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
public class JsonReaderTest extends JsonTest {
    @Test
    void testReaderNonExistentFile() throws EmptyEntryException {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Closet cl = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            System.out.println("great!");
        }
    }

    @Test
    void testReaderEmptyCloset() throws EmptyEntryException {
        JsonReader reader = new JsonReader("./data/testReaderEmptyCloset.json");
        try {
            Closet cl = reader.read();
            assertEquals("My Closet", cl.getClosetName());
            assertTrue(cl.returnClothingList().isEmpty());
            assertTrue(cl.getListOfBrands().isEmpty());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralCloset() throws EmptyEntryException {
        JsonReader reader = new JsonReader("./data/testReaderGeneralCloset.json");
        try {
            Closet cl = reader.read();
            assertEquals("My Closet", cl.getClosetName());
            List<Clothing> listOfClothingItems = cl.getListOfClothes();
            List<Brand> listOfBrands = cl.getListOfBrands();

            assertEquals(2, listOfClothingItems.size());
            assertEquals(2, listOfBrands.size());

            checkClothing("My Shirt", 15, 1, "Bluenotes", 15, listOfClothingItems.get(0));
            checkClothing("My Pants", 31.00, 2, "Aritzia", 15.50, listOfClothingItems.get(1));

            checkBrand("Bluenotes", 15, 1, 1, listOfBrands.get(0));
            checkBrand("Aritzia", 15.50, 2, 1, listOfBrands.get(1));

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
