package restaurant;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class Admin extends User implements Serializable{
private static final long serialVersionUID = 1L;
    public Admin() {
    }

    public Admin(String username, String password, String name, String role, String phone) {
        super(username, password, name, role, phone);
    }

    public void manageTables(int action, int tableID, String category, int capacity, boolean reserved) {
        ArrayList<Table> tables = Table.loadTablesFromFile();
        switch (action) {
            case 1: // Add Table
                tables.add(new Table(tableID, category, capacity, reserved));
                break;
            case 2: // Edit Table
                for (Table table : tables) {
                    if (table.getTableId() == tableID) {
                        table.setCategory(category);
                        table.setCapacity(capacity);
                        table.setReserved(reserved);
                        break;
                    }
                }
                break;
            case 3: // Remove Table
                tables.removeIf(table -> table.getTableId() == tableID);
                break;
            case 4: // Add New Category
                Table.addCategory(category);
                break;
            case 5: // View Categories
                Table.viewCategories();
                return;
            default:
                System.out.println("Invalid action.");
                return;
        }
        Table.saveTablesToFile(tables);
    }

    public void manageMenu(int action, StringBuffer name, double price, String category) {
        Menu menu = new Menu();
        
        ArrayList<Meal> meals = new ArrayList<>();
        switch (category.toLowerCase()) {
            case "breakfast":
                meals = menu.getBreakfast();
                break;
            case "lunch":
                meals = menu.getLunch();
                break;
            case "dinner":
                meals = menu.getDinner();
                break;
            case "drinks":
                meals = menu.getDrinks();
                break;
            case "appetizers":
                meals = menu.getAppetizers();
                break;
            default:
                System.out.println("Invalid category.");
                return;
        }

        switch (action) {
            case 1: // Add Menu Item
                meals.add(new Meal(name, category, price));
                break;
            case 2: // Edit Menu Item
                for (Meal meal : meals) {
                    if (meal.getName().toString().equals(name.toString())) {
                        meal.setPrice(price);
                        meal.setCategory(category);
                        break;
                    }
                }
                break;
            case 3: // Remove Menu Item
                meals.removeIf(meal -> meal.getName().toString().equals(name.toString()));
                break;
            default:
                System.out.println("Invalid action.");
                return;
        }

        menu.writeToRandomAccessFile(category.toLowerCase() + ".dat", meals);
    }

    public void manageUsers(int action, String username, String role, String password, String name, String phone) {
        ArrayList<User> users;
        try {
            users = User.loadUsers();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading users: " + e.getMessage());
            return;
        }

        switch (action) {
            case 1: // Add User
                switch (role.toLowerCase()) {
                    case "admin":
                        users.add(new Admin(username, password, name, role, phone));
                        break;
                    case "guest":
                        users.add(new Guest(username, password, name, role, phone));
                        break;
                    case "receptionist":
                        users.add(new Receptionist(username, password, name, role, phone));
                        break;
                    default:
                        System.out.println("Invalid role.");
                        return;
                }
                break;
            case 2: // Edit User
                for (User user : users) {
                    if (user.getUsername().equals(username)) {
                        user.setPassword(password);
                        user.setName(name);
                        user.setPhone(phone);
                        user.setRole(role);
                        break;
                    }
                }
                break;
            case 3: // Remove User
                users.removeIf(user -> user.getUsername().equals(username));
                break;
            default:
                System.out.println("Invalid action.");
                return;
        }

        try {
            User.saveUsers(users);
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }

    public void viewReports() {
        System.out.println(" REPORTS ");
        System.out.println("Most Ordered Meal");
        Receptionist.mostOrderedMeal();
        System.out.println("Most Rerserved Table");
        Receptionist.mostReservedTable();
    }

public void viewUsers() {
    try {
        ArrayList<User> users = User.loadUsers();
        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            System.out.println("Users:");
            for (User user : users) {
                System.out.println("Username: " + user.getUsername());
                System.out.println("Name: " + user.getName());
                System.out.println("Role: " + user.getRole());
                System.out.println("Phone: " + user.getPhone());
                System.out.println("------------------------");
            }
        }
    } catch (IOException | ClassNotFoundException e) {
        System.out.println("Error loading users: " + e.getMessage());
    }
}

 public void viewTables() {
    ArrayList<Table> tables = Table.loadTablesFromFile();
    if (tables.isEmpty()) {
        System.out.println("No tables found.");
    } else {
        System.out.println("Tables:");
        for (Table table : tables) {
            System.out.println("Table ID: " + table.getTableId());
            System.out.println("Category: " + table.getCategory());
            System.out.println("Capacity: " + table.getCapacity());
            System.out.println("Reserved: " + (table.isReserved() ? "Yes" : "No"));
            System.out.println("------------------------");
        }
    }
}

    public void viewMenu() {
        Menu menu = new Menu();
        menu.displayMenu();
    }
        
public void viewReservations() {
    Receptionist receptionist = new Receptionist();
    receptionist.loadReservations();
    ArrayList<Reservation> reservations = receptionist.getReservations();
    if (reservations.isEmpty()) {
        System.out.println("No reservations found.");
    } else {
        System.out.println("Reservations:");
        for (Reservation reservation : reservations) {
            System.out.println("Reservation ID: " + reservation.getResID());
            System.out.println("Guest Name: " + reservation.getGuestName());
            System.out.println("Phone: " + reservation.getPhone());
            System.out.println("Date and Time: " + reservation.getDateTime());
            System.out.println("Table ID: " + reservation.getTableID());
            System.out.println("Total Payment: $" + reservation.getPayment());
            System.out.println("Ordered Meals:");
            for (Meal meal : reservation.getOrderedMeals()) {
                System.out.println("  Meal Name: " + meal.getName());
                System.out.println("  Category: " + meal.getCategory());
                System.out.println("  Price: $" + meal.getPrice());
            }
            System.out.println("------------------------");
        }
    }
}
    @Override
    public void displayOptions() {
        System.out.println("1. Manage Tables");
        System.out.println("2. Manage Menus");
        System.out.println("3. Manage Users");
        System.out.println("4. View Reports");
        System.out.println("5. View Data");
        System.out.println("6. Exit");
    }
}