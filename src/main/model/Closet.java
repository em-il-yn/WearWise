package model;

import model.exceptions.EmptyEntryException;
import model.exceptions.NoSuchItemException;
import persistence.Writable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Closet implements Writable {

    // *** TEST ADD BRAND
    // *** ADD TRY CATCH FOR ALL REQUIRES (6)
    private ArrayList<Brand> listOfBrands;
    private ArrayList<Clothing> listOfClothes;
    private String closetName;

    // EFFECTS: creates closet with empty listOfBrands and empty listOfClothes
    // MODIFIES: n/a
    public Closet(String name) throws EmptyEntryException {
        if (name.isEmpty()) {
            throw new EmptyEntryException();
        }
        listOfBrands = new ArrayList<>();
        listOfClothes = new ArrayList<>();
        this.closetName = name;
    }

    // EFFECTS: adds clothing to listOfClothes
    //          adds brand to listOfBrand
    //          returns "Item added to closet"
    // MODIFIES: this.listOfClothes
    public String addClothingItem(Clothing item) {
        String newBrand = item.getBrand();

        listOfClothes.add(item);
        addBrand(newBrand, item);

        return "Item added to closet";
    }

    // EFFECTS: removes clothing from listOfClothing
    //          IF the brand of given clothing item only contains that item
    //                      remove the brand
    //          ELSE
    //                      remove the item from the brand
    // MODIFIES: this.listOfClothes
    public String removeClothingItem(String itemName) throws EmptyEntryException, NoSuchItemException {
        String brandName = findClothingItem(itemName).getBrand();
        listOfClothes.remove(findClothingItem(itemName));
        Brand brand = findClothingBrand(brandName);

        if (brand.getNumOfItems() == 1) {
            listOfBrands.remove(brand);
        }

        return "Item removed from closet";
    }

    // EFFECTS: returns item in listOfClothes with name matching string
    // MODIFIES: n/a
    public Clothing findClothingItem(String name) throws EmptyEntryException, NoSuchItemException {
        int count = 0;

        if (this.listOfClothes.isEmpty()) {
            throw new EmptyEntryException();
        }

        for (Clothing item: listOfClothes) {
            if (item.getItemName().equals(name)) {
                break;
            } else {
                count++;
            }
        }

        if (count == listOfClothes.size()) {
            throw new NoSuchItemException();
        }

        return listOfClothes.get(count);
    }

    // EFFECTS: returns brand in listofbrand with name matching string
    // MODIFIES: n/a
    public Brand findBrand(String name) throws EmptyEntryException, NoSuchItemException {
        int count = 0;

        if (this.listOfBrands.isEmpty()) {
            throw new NoSuchItemException();
        }

        for (Brand item: listOfBrands) {
            if (item.getBrandName().equals(name)) {
                break;
            } else {
                count++;
            }
        }

        if (count == listOfBrands.size()) {
            throw new NoSuchItemException();
        }

        return listOfBrands.get(count);
    }

    // EFFECTS: returns brand in listOfBRands with name matching string
    // MODIFIES: n/a
    public Brand findClothingBrand(String name) {
        Brand brandIdentified = null;

        for (Brand brand : listOfBrands) {
            if (brand.getBrandName().equals(name)) {
                brandIdentified = brand;
            }
        }

        return brandIdentified;
    }

    // EFFECTS: adds brand to list if it is not on the list already, prints whether or not brand was added
    // MODIFIES: this.listOfBrands
    public void addBrand(String brandName, Clothing item) {
        int added = 0;

        for (Brand brand : listOfBrands) {
            if (brand.getBrandName().equals(brandName)) {
                brand.addClothingItem(item);
                added = added + 1;
                break;
            }
        }

        if (added != 1) {
            Brand newBrand = new Brand(brandName);
            newBrand.addClothingItem(item);
            listOfBrands.add(newBrand);
        }
    }

    // EFFECTS: returns a list of the names of clothing items in closet
    // MODIFIES: n/a
    public ArrayList<String> returnClothingList() {
        ArrayList<String> itemNames = new ArrayList<>();

        for (Clothing item: listOfClothes) {
            itemNames.add(item.getItemName());
        }

        return itemNames;
    }

    // EFFECTS: returns a list of the names of brands in closet
    // MODIFIES: n/a
    public ArrayList<String> returnBrandList() {
        ArrayList<String> itemNames = new ArrayList<>();

        for (Brand item: listOfBrands) {
            itemNames.add(item.getBrandName());
        }

        return itemNames;
    }

    // EFFECTS: returns listOfBrands
    // MODIFIES: n/a
    public ArrayList<Brand> getListOfBrands() {
        return listOfBrands;
    }

    public ArrayList<Clothing> getListOfClothes() {
        return listOfClothes;
    }

    public String getClosetName() {
        return closetName;
    }

    // sourced from https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", closetName);
        json.put("listOfClothes", closetItemsToJson());
        json.put("listOfBrands", brandsToJson());
        return json;
    }

    private JSONArray brandsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Brand b : listOfBrands) {
            jsonArray.put(b.toJson());
        }

        return jsonArray;
    }

    // EFFECTS: returns clothing items in this closet as a JSON array
    private JSONArray closetItemsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Clothing c : listOfClothes) {
            jsonArray.put(c.toJson());
        }

        return jsonArray;
    }
}
