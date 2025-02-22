package restaurant;

import java.io.*;
import java.util.ArrayList;

public class Table {

    private int tableID;
    protected String category;
    private int capacity;
    private boolean reserved;

    public Table() {
    }

    public Table(int tableID, String category, int capacity, boolean reserved) {
        this.tableID = tableID;
        this.category = category;
        this.capacity = capacity;
        this.reserved = reserved;
    }

    public int getTableId() {
        return tableID;
    }

    public void setTableId(int tableID) {
        this.tableID = tableID;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        if (capacity < 1) {
            System.out.println("Capacity must be at least 1.");
            return;
        }
        this.capacity = capacity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (category == null) {
            System.out.println("Category cannot be empty.");
            return;
        }
        this.category = category;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public static void saveCategoriesToFile(ArrayList<String> categories) {
        try (RandomAccessFile file = new RandomAccessFile("categories.dat", "rw")) {
            file.setLength(0);
            file.writeInt(categories.size());
            for (String cat : categories) {
                file.writeUTF(cat);
            }
            System.out.println("Categories saved to file successfully!");
        } catch (IOException e) {
            System.err.println("Error saving categories to file: " + e.getMessage());
        }
    }

    public static ArrayList<String> loadCategoriesFromFile() {
        ArrayList<String> categories = new ArrayList<>();
        File file = new File("categories.dat");

        try {
            if (!file.exists()) {
                System.out.println("Categories file not found. Creating a new one...");
                try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
                    categories.add("Outdoor");
                    categories.add("Indoor");
                    categories.add("VIP");

                    raf.writeInt(categories.size());
                    for (String category : categories) {
                        raf.writeUTF(category);
                    }
                    System.out.println("Default categories created and saved.");
                }
            } else {
                try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
                    int size = raf.readInt();
                    for (int i = 0; i < size; i++) {
                        categories.add(raf.readUTF());
                    }
                    System.out.println("Categories loaded from file successfully!");
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading categories from file: " + e.getMessage());
        }

        return categories;
    }

    public static void saveTablesToFile(ArrayList<Table> tableList) {
        if (tableList == null || tableList.isEmpty()) {
            System.out.println("Table list is empty or null, nothing to save.");
            return;
        }

        try (RandomAccessFile file = new RandomAccessFile("tables.dat", "rw")) {
            file.setLength(0);
            file.writeInt(tableList.size());

            for (Table table : tableList) {
                if (table == null) {
                    System.out.println("Skipping null table.");
                    continue;
                }

                file.writeInt(table.getTableId());
                file.writeInt(table.getCapacity());
                file.writeUTF(table.getCategory());
                file.writeBoolean(table.isReserved());
            }
            System.out.println("Tables saved to file successfully!");
        } catch (IOException e) {
            System.err.println("Error saving tables to file: " + e.getMessage());
        }
    }

    public static ArrayList<Table> loadTablesFromFile() {
        ArrayList<Table> tableList = new ArrayList<>();
        try (RandomAccessFile file = new RandomAccessFile("tables.dat", "r")) {
            int size = file.readInt();
            for (int i = 0; i < size; i++) {
                int tableID = file.readInt();
                int capacity = file.readInt();
                String category = file.readUTF();
                boolean reserved = file.readBoolean();

                Table table = new Table(tableID, category, capacity, reserved);
                tableList.add(table);
            }
            System.out.println("Tables loaded from file successfully!");
        } catch (IOException e) {
            System.err.println("Error loading tables from file: " + e.getMessage());
        }
        return tableList;
    }

    public static void viewCategories() {
        ArrayList<String> categories = loadCategoriesFromFile();
        if (categories.isEmpty()) {
            System.out.println("No categories available.");
        } else {
            System.out.println("Available Categories:");
            for (String category : categories) {
                System.out.println("- " + category);
            }
        }
    }

    public static void addCategory(String newCategory) {
        ArrayList<String> categories = loadCategoriesFromFile();
        if (!categories.contains(newCategory)) {
            categories.add(newCategory);
            saveCategoriesToFile(categories);
            System.out.println("Category added: " + newCategory);
        } else {
            System.out.println("Category already exists: " + newCategory);
        }
    }
}