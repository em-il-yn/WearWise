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
class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() throws EmptyEntryException {
        try {
            Closet cl = new Closet("My Closet");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            System.out.println("great!");
        }
    }

    @Test
    void testWriterEmptyCloset() throws EmptyEntryException {
        try {
            Closet cl = new Closet("My Closet");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyCloset.json");
            writer.open();
            writer.write(cl);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyCloset.json");
            cl = reader.read();
            assertEquals("My Closet", cl.getClosetName());
            assertTrue(cl.returnClothingList().isEmpty());
            assertTrue(cl.getListOfBrands().isEmpty());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralCloset() {
        try {
            Closet cl = new Closet("My Closet");
            Clothing clothing1 = new Clothing("My Pants", 31, "Aritzia");
            Clothing clothing2 = new Clothing("My Shirt", 15, "Bluenotes");
            clothing1.addWearCount();
            clothing1.addWearCount();
            clothing2.addWearCount();
            cl.addClothingItem(clothing1);
            cl.addClothingItem(clothing2);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralCloset.json");

            writer.open();
            writer.write(cl);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralCloset.json");
            cl = reader.read();

            assertEquals("My Closet", cl.getClosetName());
            List<Clothing> listOfClothes = cl.getListOfClothes();
            List<Brand> listOfBrands = cl.getListOfBrands();

            assertEquals(2, listOfBrands.size());
            assertEquals(2, listOfClothes.size());

            checkClothing("My Pants", 31.00, 2, "Aritzia", 15.50, listOfClothes.get(0));
            checkClothing("My Shirt", 15, 1, "Bluenotes", 15, listOfClothes.get(1));

            checkBrand("Aritzia", 15.50, 2, 1, listOfBrands.get(0));
            checkBrand("Bluenotes", 15, 1, 1, listOfBrands.get(1));

        } catch (IOException | EmptyEntryException e) {
            fail("Exception should not have been thrown");
        }
    }
}