package ui;

import model.Brand;
import model.Closet;
import model.Clothing;
import model.exceptions.EmptyEntryException;
import model.exceptions.NoSuchItemException;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ClosetApp extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static final String JSON_STORE = "./data/closet.json";
    private Closet myCloset;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the closet app
    public ClosetApp() {
        openCloset();
    }

    // MODIFIES: this
    // EFFECTS: initializes the application and opens the opening frame of the applicaiton
    public void openCloset() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        JFrame frame = createFrame("WearWise");
        JComponent imagePanel = createImagePanel();

        frame.getContentPane().add(imagePanel, BorderLayout.NORTH);

        JTextField nameField = new JTextField(20);

        JPanel inputPanel = createInputPanel(frame, nameField);

        frame.getContentPane().add(inputPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // MODIFIES: creates a JFrame
    // EFFECTS: Returns an initialized JFrame
    private JFrame createFrame(String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.PINK);
        return frame;
    }

    // MODIFIES: this
    // EFFECTS: creates and displays an image IF there is no image prints an error
    private JComponent createImagePanel() {
        JPanel imagePanel = new JPanel();
        JLabel imageLabel = new JLabel();
        imagePanel.add(imageLabel);

        try {
            Image image = ImageIO.read(new File(
                    "/Users/em.il.yn/IdeaProjects/project_f3y2z/src/main/ui/clueless_closet_pic.jpeg"));
            Image resizedImage = image.getScaledInstance(400, 286, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(resizedImage);
            imageLabel.setIcon(icon);
        } catch (IOException ex) {
            System.out.println("Failed to read image: " + ex.getMessage());
        }
        return imagePanel;
    }

    // MODIFIES: Creates and returns a JPanel with certain specifications
    // EFFECTS: adds the load and start button to their JButtons and creates a layout
    private JPanel createInputPanel(JFrame frame, JTextField nameField) {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel nameLabel = new JLabel("Welcome to WearWise! Enter your name or load your closet to start: ");

        JButton startButton = createStartButton(nameField, frame);
        JButton loadButton = createLoadButton(frame);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(loadButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(startButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(nameLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(nameField, gbc);
        
        return inputPanel;
    }

    // MODIFIES: creates and returns a JButton for start
    // EFFECTS: creates the start button with a label and the functionality of opening the menu
    private JButton createStartButton(JTextField nameField, JFrame frame) {
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            String name = nameField.getText();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter your name");
            } else {
                try {
                    myCloset = new Closet(name + "'s Closet");
                    openMenu();
                } catch (EmptyEntryException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid string");
                } catch (NoSuchItemException ex) {
                    JOptionPane.showMessageDialog(frame, "Item not found");
                }
            }
        });

        return startButton;
    }

    // MODIFIES: creates and returns a JButton for loading the closet
    // EFFECTS: creates the load button with a label and the functionality of
    //          loading a previous closet then opening the menu
    private JButton createLoadButton(JFrame frame) {
        JButton loadButton = new JButton("Load Closet");
        loadButton.addActionListener(e -> {
            try {
                myCloset = jsonReader.read();
                JOptionPane.showMessageDialog(frame, "Loaded " + myCloset.getClosetName() + " from " + JSON_STORE);
                openMenu();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Unable to read from file: " + JSON_STORE);
            } catch (EmptyEntryException | NoSuchItemException ex) {
                throw new RuntimeException(ex);
            }
        });

        return loadButton;
    }

    // MODIFIES: this
    // EFFECTS: creates a new jframe with buttons for quit, save, view brands, view closet, and add clothing
    public void openMenu() throws EmptyEntryException, NoSuchItemException {
        JFrame frame = createFrame("WearWise Main Menu");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        String[] options = {"quit", "save closet", "view my brands",  "view and edit my closet", "add clothing"};
        int selection = createSelection(options);

        switch (selection) {
            case 0:
                quit();
                break;
            case 1:
                saveClosetOption(frame);
                break;
            case 2:
                loadBrands();
                break;
            case 3:
                openClothingMenu();
                break;
            case 4:
                addClothingItemOption();
                break;
            default:
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: creates a JOptionPane.showOptionDialog()
    private int createSelection(String[] options) {
        return JOptionPane.showOptionDialog(null, "✧･ﾟ: *✧･ﾟ:* " + myCloset.getClosetName() + " ✧･ﾟ: *✧･ﾟ:*",
                "WearWise Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
    }

    // MODIFIES: closet is saved to jsonwriter
    // EFFECTS: writes my closet to the json file, if exception is caught displays error message
    private void saveClosetOption(Frame frame) throws EmptyEntryException, NoSuchItemException {
        try {
            jsonWriter.open();
            jsonWriter.write(myCloset);
            jsonWriter.close();
            JOptionPane.showMessageDialog(frame, "Saved " + myCloset.getClosetName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(frame, "Unable to write to file: " + JSON_STORE);
        }
        openMenu();
    }

    // MODIFIES: adds clothing to mycloset
    // EFFECTS: creates prompts for the info needed to create and add clothing to the closet
    private void addClothingItemOption() throws EmptyEntryException, NoSuchItemException {
        String name = JOptionPane.showInputDialog(null, "Enter the item name:");
        String cost = JOptionPane.showInputDialog(null, "Enter the item cost:");
        String brand = JOptionPane.showInputDialog(null, "Enter the item brand:");
        Clothing newItem = new Clothing(name, Double.parseDouble(cost), brand);
        myCloset.addClothingItem(newItem);
        openMenu();
    }

    // MODIFIES: creates a clothing menu using jswing
    // EFFECTS: generates several options to select in a clothing menu
    public void openClothingMenu() throws EmptyEntryException, NoSuchItemException {
        String clothingList = generateClothingList();

        String[] options = {"return to main menu", "mark an item worn", "view an item's statistics", "remove an item"};
        int selection = JOptionPane.showOptionDialog(null,
                "✧･ﾟ: *✧･ﾟ:* My Clothes ✧･ﾟ: *✧･ﾟ:* \n" + clothingList, "WearWise Clothing Menu",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        switch (selection) {
            case 0:
                openMenu();
                break;
            case 1:
                markWorn();
                break;
            case 2:
                displayStatistics();
                break;
            case 3:
                removeItem();
                break;
            default:
                break;
        }
    }

    // MODIFIES: n/a
    // EFFECTS: returns a list of the clothing items as a string
    private String generateClothingList() {
        StringBuilder clothingList = new StringBuilder();

        for (String clothes : myCloset.returnClothingList()) {
            clothingList.append("\n ❥ ").append(clothes);
        }

        return clothingList.toString();
    }

    // MODIFIES: updates wear count on a clothing item
    // EFFECTS: updates the wear count of a clothign item using the mark worn button
    private void markWorn() {
        JFrame frame = new JFrame("Mark Worn");
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Which item did you wear?");
        JTextField textField = new JTextField(20);
        JButton button = new JButton("Mark Worn");
        frame.setBackground(Color.PINK);

        button.addActionListener(e -> {
            String selection = textField.getText();

            markWornEvent(frame, selection);

            frameDisposal(frame);
        });

        panel.add(label);
        panel.add(textField);
        panel.add(button);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    // MODIFIES: wearcount and brand attributes
    // EFFECTS: updates the wear count and makes sure those updates are applied to brand statistics
    private void markWornEvent(JFrame frame, String selection) {
        try {
            Clothing clothingItem = myCloset.findClothingItem(selection);
            clothingItem.addWearCount();
            Brand brand = myCloset.findBrand(clothingItem.getBrand());
            brand.updateAverages();
        } catch (EmptyEntryException ex) {
            JOptionPane.showMessageDialog(frame, "Closet currently empty. "
                    + "Add clothing item from the main menu to start tracking usage.\n");
        } catch (NoSuchItemException ex) {
            createNoSuchItemMessage(frame);
        }
    }

    // MODIFIES: n/a
    // EFFECTS: creates an error message to inform of no item
    private void createNoSuchItemMessage(JFrame frame) {
        JOptionPane.showMessageDialog(frame, "No such item. Try again.");
        try {
            openClothingMenu();
        } catch (EmptyEntryException | NoSuchItemException exc) {
            throw new RuntimeException(exc);
        }
    }

    // MODIFIES: jframe
    // EFFECTS: closes it
    private void frameDisposal(JFrame frame) {
        frame.dispose();
        try {
            openClothingMenu();
        } catch (EmptyEntryException | NoSuchItemException ex) {
            throw new RuntimeException(ex);
        }
    }

    // MODIFIES: creates a jfram
    // EFFECTS: displays item statistics or error message
    private void displayStatistics() {
        JFrame frame = new JFrame("View Item Statistics");
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter the item name:");
        JTextField textField = new JTextField(20);
        JButton button = new JButton("View Statistics");
        frame.setBackground(Color.PINK);

        button.addActionListener(e -> {
            String itemName = textField.getText();

            statisticsExceptionCatch(frame, itemName);
        });

        panel.add(label);
        panel.add(textField);
        panel.add(button);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    private void statisticsExceptionCatch(JFrame frame, String itemName) {
        try {
            printStatistics(itemName);
            frame.dispose();
        } catch (EmptyEntryException ex) {
            JOptionPane.showMessageDialog(null, "Closet currently empty. Add clothing item"
                    + " from the main menu to start tracking usage.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NoSuchItemException ex) {
            JOptionPane.showMessageDialog(null, "No such item. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // MODIFIES: creates a button
    // EFFECTS: button displays item statistics
    private void printStatistics(String itemName) throws EmptyEntryException, NoSuchItemException {
        String brand = myCloset.findClothingItem(itemName).getBrand();
        double cost = myCloset.findClothingItem(itemName).getCost();
        int wearCount = myCloset.findClothingItem(itemName).getWearCount();
        double costPerWear = myCloset.findClothingItem(itemName).getCostPerWear();

        JFrame frame = createFrame(itemName + " Statistics");
        JPanel panel = new JPanel(new GridLayout(5, 1));
        JButton button = new JButton("Return to Clothing Menu");

        JLabel brandLabel = new JLabel("Brand: " + brand);
        JLabel costLabel = new JLabel("Total Cost: $" + cost);
        JLabel wearCountLabel = new JLabel("Wear Count: " + wearCount);
        JLabel costPerWearLabel = new JLabel("Cost Per Wear: " + costPerWear);

        panel.add(brandLabel);
        panel.add(costLabel);
        panel.add(wearCountLabel);
        panel.add(costPerWearLabel);
        panel.add(button);

        button.addActionListener(e -> frameDisposal(frame));

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    // MODIFIES: listofclothes in mycloset
    // EFFECTS: removes an item or displays an error message
    private void removeItem() throws EmptyEntryException, NoSuchItemException {
        JFrame frame = new JFrame("Remove Item");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        String itemName = JOptionPane.showInputDialog(null, "Enter the item name:");
        frame.setBackground(Color.PINK);

        try {
            myCloset.removeClothingItem(itemName);
        } catch (EmptyEntryException e) {
            JOptionPane.showMessageDialog(null, "Closet currently empty. Add clothing item from"
                    + " the main menu to start tracking usage.", "Error", JOptionPane.ERROR_MESSAGE);
            openMenu();
        } catch (NoSuchItemException e) {
            JOptionPane.showMessageDialog(null, "No such item. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
            openClothingMenu();
        }

        openClothingMenu();
    }

    // MODIFIES: n/a
    // EFFECTS: shows brands in the closet, and creates a return to menu button and a view statistics button
    public void loadBrands() throws EmptyEntryException, NoSuchItemException {
        StringBuilder brandList = new StringBuilder();

        for (String brands : myCloset.returnBrandList()) {
            brandList.append("\n ❥ ").append(brands);
        }

        JFrame frame = new JFrame("My Brands");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.PINK);

        String[] options = {"return to main menu", "view a brand's statistics"};
        int selection = JOptionPane.showOptionDialog(null, "✧･ﾟ: *✧･ﾟ:* My Brands ✧･ﾟ: *✧･ﾟ:* \n" + brandList,
                "My Brands", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        switch (selection) {
            case 0:
                openMenu();
                break;
            case 1:
                displayBrandStatistics();
                break;
            default:
                break;
        }
    }

    // MODIFIES: n/a
    // EFFECTS: creates a jframe asking for the brand the user wants to see
    private void displayBrandStatistics() {
        JFrame frame = new JFrame("View Brand Statistics");
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter the brand name:");
        JTextField textField = new JTextField(20);
        JButton button = new JButton("View Statistics");
        frame.setBackground(Color.PINK);

        button.addActionListener(e -> {
            String itemName = textField.getText();

            try {
                printBrandStatistics(itemName);
                frame.dispose();
            } catch (EmptyEntryException ex) {
                createEmptyEntryExceptionMessage();
            } catch (NoSuchItemException ex) {
                JOptionPane.showMessageDialog(null, "No such item. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(label);
        panel.add(textField);
        panel.add(button);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    // MODIFIES: n/a
    // EFFECTS: creates dialogue box for errors
    private void createEmptyEntryExceptionMessage() {
        JOptionPane.showMessageDialog(null, "Closet currently empty. Add clothing item "
                + "from the main menu to start tracking usage.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    // MODIFIES: n/a
    // EFFECTS: creates a jframe to display brand statistics
    private void printBrandStatistics(String brandName) throws EmptyEntryException, NoSuchItemException {
        Brand brand = myCloset.findBrand(brandName);
        double wearCount = brand.getAverageWearCount();
        double costPerWear = brand.getAverageCostPerWear();

        JFrame frame = createFrame(brandName + " Statistics");
        JPanel panel = new JPanel(new GridLayout(4, 1));
        JButton button = new JButton("Return to Brand Menu");
        frame.setBackground(Color.PINK);

        JLabel brandLabel = new JLabel("Brand: " + brandName);
        JLabel wearCountLabel = new JLabel("Wear Count: " + wearCount);
        JLabel costPerWearLabel = new JLabel("Cost Per Wear: " + costPerWear);

        panel.add(brandLabel);
        panel.add(wearCountLabel);
        panel.add(costPerWearLabel);
        panel.add(button);

        button.addActionListener(e -> frameDisposalBrands(frame));

        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    // MODIFIES: gui
    // EFFECTS: closes it
    private void frameDisposalBrands(JFrame frame) {
        frame.dispose();
        try {
            loadBrands();
        } catch (EmptyEntryException | NoSuchItemException ex) {
            throw new RuntimeException(ex);
        }
    }

    // MODIFIES: mycloset
    // EFFECTS: prompts to choose to save and quit or just save
    private void quit() {
        String[] options = {"quit and save", "quit without saving"};
        int selection = JOptionPane.showOptionDialog(null, "✧･ﾟ: *✧･ﾟ:* " + myCloset.getClosetName() + " ✧･ﾟ: *✧･ﾟ:*",
                "WearWise Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        switch (selection) {
            case 0:
                try {
                    jsonWriter.open();
                    jsonWriter.write(myCloset);
                    jsonWriter.close();
                    System.out.println("Saved " + myCloset.getClosetName() + " to " + JSON_STORE);
                    System.exit(0);
                } catch (FileNotFoundException e) {
                    System.out.println("Unable to write to file: " + JSON_STORE);
                }
                break;
            case 1:
                System.out.println("Bye!");
                JOptionPane.showMessageDialog(null, "Bye!", "WearWise", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
                break;
            default:
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}