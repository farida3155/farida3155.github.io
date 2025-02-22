package restaurant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.io.RandomAccessFile;

public class Menu {

    ArrayList<Meal> breakfast = new ArrayList<>();
    ArrayList<Meal> appetizers = new ArrayList<>();
    ArrayList<Meal> lunch = new ArrayList<>();
    ArrayList<Meal> dinner = new ArrayList<>();
    ArrayList<Meal> drinks = new ArrayList<>();
    
    public Menu() {
        initializeDefaultMenu();
    }

    private void initializeDefaultMenu() {
        breakfast.add(new Meal(new StringBuffer("Pancakes"), "Breakfast", 5.99));
        breakfast.add(new Meal(new StringBuffer("Omelette"), "Breakfast", 7.99));
        breakfast.add(new Meal(new StringBuffer("Waffles"), "Breakfast", 9.03));
        breakfast.add(new Meal(new StringBuffer("Croissant"), "Breakfast", 8.56));
        breakfast.add(new Meal(new StringBuffer("French Toast"), "Breakfast", 11.32));

        lunch.add(new Meal(new StringBuffer("Grilled Cheese Sandwich"), "Lunch", 6.50));
        lunch.add(new Meal(new StringBuffer("Caesar Salad"), "Lunch", 8.00));
        lunch.add(new Meal(new StringBuffer("Club Sandwich"), "Lunch", 9.00));
        lunch.add(new Meal(new StringBuffer("Classic Burger"), "Lunch", 11.00));
        lunch.add(new Meal(new StringBuffer("Chicken Wings"), "Lunch", 8.50));

        dinner.add(new Meal(new StringBuffer("Steak"), "Dinner", 15.99));
        dinner.add(new Meal(new StringBuffer("Spaghetti"), "Dinner", 12.50));
        dinner.add(new Meal(new StringBuffer("Grilled Chicken"), "Dinner", 14.50));
        dinner.add(new Meal(new StringBuffer("Chicken Fried Rice"), "Dinner", 13.50));
        dinner.add(new Meal(new StringBuffer("Mac and Cheese"), "Dinner", 11.50));

        drinks.add(new Meal(new StringBuffer("Coffee"), "Drink", 2.50));
        drinks.add(new Meal(new StringBuffer("Orange Juice"), "Drink", 3.00));
        drinks.add(new Meal(new StringBuffer("Mojito"), "Drink", 5.00));
        drinks.add(new Meal(new StringBuffer("Water"), "Drink", 1.50));
        drinks.add(new Meal(new StringBuffer("Redbull"), "Drink", 6.70));

        appetizers.add(new Meal(new StringBuffer("Garlic Bread"), "Appetizer", 4.00));
        appetizers.add(new Meal(new StringBuffer("Spring Rolls"), "Appetizer", 5.50));
        appetizers.add(new Meal(new StringBuffer("French Fries"), "Appetizer", 3.99));
        appetizers.add(new Meal(new StringBuffer("Onion Rings"), "Appetizer", 5.20));
        appetizers.add(new Meal(new StringBuffer("Mozzarella Sticks"), "Appetizer", 6.00));   
    }

    public void writeToRandomAccessFile(String fileName, ArrayList<Meal> menuList) {
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
            raf.setLength(0);
            for (Meal meal : menuList) {
                raf.writeUTF(meal.getName().toString());
                raf.writeUTF(meal.getCategory());
                raf.writeDouble(meal.getPrice());
            }
            System.out.println("File written successfully: " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + fileName + ". " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void readFromRandomAccessFile(String fileName, ArrayList<Meal> menuList) {
        menuList.clear();
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                StringBuffer name = new StringBuffer(raf.readUTF());
                String category = raf.readUTF();
                double price = raf.readDouble();
                menuList.add(new Meal(name, category, price));
           
           }
        } catch (IOException e) {
            System.err.println("Error reading from file: " + fileName + ". " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void initializeFiles() {
        initializeFile("breakfast.dat", breakfast);
        initializeFile("lunch.dat", lunch);
        initializeFile("dinner.dat", dinner);
        initializeFile("drinks.dat", drinks);
        initializeFile("appetizers.dat", appetizers);
    }

    private void initializeFile(String fileName, ArrayList<Meal> menuList) {
        File file = new File(fileName);
        if (!file.exists()) {
            writeToRandomAccessFile(fileName, menuList);
        } else {
            readFromRandomAccessFile(fileName, menuList);
        }
    }

    public void displayMenu() {
        System.out.println("\nAppetizers:");
        for (Meal meal : appetizers) {
            meal.display();
        }

        System.out.println("\nBreakfast:");
        for (Meal meal : breakfast) {
            meal.display();
        }

        System.out.println("\nLunch:");
        for (Meal meal : lunch) {
            meal.display();
        }

        System.out.println("\nDinner:");
        for (Meal meal : dinner) {
            meal.display();
        }

        System.out.println("\nDrinks:");
        for (Meal meal : drinks) {
            meal.display();
        }
    }

    public ArrayList<Meal> getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(ArrayList<Meal> breakfast) {
        this.breakfast = breakfast;
    }

    public ArrayList<Meal> getAppetizers() {
        return appetizers;
    }

    public void setAppetizers(ArrayList<Meal> appetizers) {
        this.appetizers = appetizers;
    }

    public ArrayList<Meal> getLunch() {
        return lunch;
    }

    public void setLunch(ArrayList<Meal> lunch) {
        this.lunch = lunch;
    }

    public ArrayList<Meal> getDinner() {
        return dinner;
    }

    public void setDinner(ArrayList<Meal> dinner) {
        this.dinner = dinner;
    }

    public ArrayList<Meal> getDrinks() {
        return drinks;
    }

    public void setDrinks(ArrayList<Meal> drinks) {
        this.drinks = drinks;
    }

   
}
