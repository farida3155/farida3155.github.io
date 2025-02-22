package restaurant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class Restaurant {

    public static boolean authenticateUser(String username, String password) throws IOException, ClassNotFoundException {
    File userFile = new File("Users.dat");
    if (!userFile.exists() || userFile.length() == 0) {
        throw new IOException("User file does not exist or is empty.");
    }

    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userFile))) {
        ArrayList<User> users = (ArrayList<User>) ois.readObject();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
    } catch (IOException | ClassNotFoundException e) {
        throw new IOException("Error loading users from file: " + e.getMessage());
    }
    return false;
}

    public static void main(String[] args) throws IOException, ClassNotFoundException {

    ArrayList<User> sharedUsers = new ArrayList<>();

  
    File userFile = new File("Users.dat");
    if (!userFile.exists() || userFile.length() == 0) {
        try {
            userFile.createNewFile();
            sharedUsers.add(new Admin("admin", "admin123", "Admin", "Admin", "0000000000"));
            User.saveUsers(sharedUsers);
        } catch (IOException e) {
            System.out.println("Error creating user file: " + e.getMessage());
        }
    }

    try {
        sharedUsers = User.loadUsers();
    } catch (IOException | ClassNotFoundException e) {
        System.out.println("Error loading users: " + e.getMessage());
    }

        Scanner cred = new Scanner(System.in);
       

        Menu m = new Menu();
        Admin manager = new Admin();

        int attempts = 0;
        while (true) {
            System.out.println("-------RESTAURANT MANAGEMENT SYSTEM------");
            System.out.println("1. Admin");
            System.out.println("2. Guest");
            System.out.println("3. Receptionist");
            System.out.println("4. Exit");
            Scanner input = new Scanner(System.in);
            int u = input.nextInt();
            switch (u) {
                case 1: // Admin
                    Scanner scanner = new Scanner(System.in);

                    int action;
                    
                    boolean exitAdmin = false;

                    while (attempts < 3) {
                        System.out.println("Enter Username: ");
                        String adminUsername = cred.next();
                        System.out.println("Enter Password: ");
                        String adminPassword = cred.next();

                        boolean aU = authenticateUser(adminUsername, adminPassword);
                        if (aU) {
                            System.out.println("Login successful!");
                            break;
                        } else {
                            System.out.println("Incorrect Username or Password. Please try again.");
                        }
                        attempts++;
                    }

                    if (attempts == 3) {
                        System.out.println("Too many failed attempts. Please try again later.");
                        break;
                    }

                    // Admin options
                    while (!exitAdmin) {
                        manager.displayOptions();
                        System.out.println("Choice: ");
                        int data = scanner.nextInt(); 
                        scanner.nextLine();

                        switch (data) {
                            case 1: // Manage Tables
                                
                                System.out.println("---- Manage Tables ----");

                                
                                Table.viewCategories();

                                System.out.println("1. Add Table");
                                System.out.println("2. Edit Table");
                                System.out.println("3. Remove Table");
                                System.out.println("4. Add New Category");
                                System.out.println("5. View Categories");
                                action = scanner.nextInt();

                                System.out.println("Enter Table ID: ");
                                int tableID = scanner.nextInt();
                                scanner.nextLine(); 

                                System.out.println("Enter Category: ");
                                String tcategory = scanner.next();
                                scanner.nextLine();

                                System.out.println("Enter capacity: ");
                                int capacity = scanner.nextInt();

                                System.out.println("Is the table reserved? (true/false): ");
                                boolean reserved = scanner.nextBoolean();

                                manager.manageTables(action, tableID, tcategory, capacity, reserved);
                                break;

                            case 2: // Manage Menus
    System.out.println("1. Add Menu Item");
    System.out.println("2. Edit Menu Item");
    System.out.println("3. Remove Menu Item");
    action = scanner.nextInt();
    scanner.nextLine(); 

    System.out.println("Enter Meal Name: ");
    String name = scanner.nextLine(); 

    System.out.println("Enter Meal Category: ");
    String mealcategory = scanner.nextLine(); 

    double price = 0;
    if (action == 1 || action == 2) {
        System.out.println("Enter meal price: ");
        price = scanner.nextDouble();
    }

    manager.manageMenu(action, new StringBuffer(name), price, mealcategory);
    break;

           case 3: // Manage Users
                              System.out.println("1. Add User");
System.out.println("2. Edit User");
System.out.println("3. Remove User");
action = scanner.nextInt();
scanner.nextLine(); 

System.out.println("Enter Username: ");
String username = scanner.nextLine();

System.out.println("Enter User Role: ");
String role = scanner.next();
scanner.nextLine(); 

System.out.println("Enter User Password: ");
String pass = scanner.next();
scanner.nextLine(); 

System.out.println("Enter Name: ");
String newName = scanner.nextLine();

System.out.println("Phone: ");
String phone = scanner.next();
scanner.nextLine(); 

manager.manageUsers(action, username, role, pass, newName, phone);
break;

                            case 4: // View Reports
                                manager.viewReports();
                                break;

                            case 5: // View Data
                                System.out.println("VIEW DATA");
                                System.out.println("1. View Users");
                                System.out.println("2. View Tables");
                                System.out.println("3. View Menu Items");
                                System.out.println("4. View Reservations");
                                int view = scanner.nextInt();
                                switch (view) {
                                    case 1:
                                        manager.viewUsers();
                                        break;
                                    case 2:
                                        manager.viewTables();
                                        break;
                                    case 3:
                                        manager.viewMenu();
                                        break;
                                    case 4:
                                        manager.viewReservations();
                                        break;
                                }
                                break;

                            case 6: // Exit
                                exitAdmin = true;
                                System.out.println("Exiting Admin Account");
                                break;

                            default:
                                System.out.println("Invalid number! Please enter a number between 1 and 6.");
                                break;
                        }
                    }
                    break;

                case 2: // Guest
                    try {
                        boolean runApp = true;

                        while (runApp) {
                            System.out.println("1. Login");
                            System.out.println("2. Sign Up");
                            System.out.println("3. Exit");
                            int x = cred.nextInt();
                            switch (x) {
                                case 1: // Login
                                    attempts = 0;
                                    while (attempts < 3) {
                                        System.out.println("Enter Username: ");
                                        String guser = cred.next();
                                        System.out.println("Enter Password: ");
                                        String gpass = cred.next();

                                        boolean aU = authenticateUser(guser, gpass);
                                        if (aU) {
                                            System.out.println("Login successful!");
                                            try {
                                                sharedUsers = User.loadUsers();
                                                if (sharedUsers == null || sharedUsers.isEmpty()) {
                                                    System.out.println("No users found in the system.");
                                                    break;
                                                }

                                                for (User currentUser : sharedUsers) {
                                                    if (currentUser instanceof Guest && currentUser.getUsername().equals(guser) && currentUser.getPassword().equals(gpass)) {
                                                        Guest g = (Guest) currentUser;

                                                        boolean inMenu = true;
                                                        while (inMenu) {
                                                            System.out.println("-----Options-----");
                                                            g.displayOptions();
                                                            System.out.println("What Would You Like To Do?");

                                                            int opt = cred.nextInt();
                                                            switch (opt) {
                                                                case 1:
                                                                    m.displayMenu();
                                                                    break;
                                                                case 2:
                                                                    g.viewReservations(g.getPhone());
                                                                    break;
                                                                case 3:
                                                                    System.out.println("Please Enter a Rating Between 1 & 5");
                                                                    int r = cred.nextInt();
                                                                    g.setRating(r);
                                                                    break;
                                                                case 4:
                                                                    inMenu = false;
                                                                    break;
                                                                default:
                                                                    System.out.println("Invalid option. Please choose again.");
                                                            }
                                                        }
                                                    }
                                                }
                                            } catch (IOException | ClassNotFoundException e) {
                                                System.out.println("Error loading users: " + e.getMessage());
                                            }
                                            break;
                                        } else {
                                            System.out.println("Incorrect Username or Password. Please try again.");
                                        }

                                        attempts++;
                                    }

                                    if (attempts == 3) {
                                        System.out.println("Too many failed attempts. Please try again later.");
                                    }
                                    break;

                                case 2: // Sign Up
                                System.out.println("Enter Username: ");
String newGuser = cred.next();
cred.nextLine();



System.out.println("Enter User Password: ");
String newGpass = cred.next();
cred.nextLine(); 

System.out.println("Enter Name: ");
String name = cred.nextLine();

System.out.println("Phone: ");
String phone = cred.next();
cred.nextLine();
                                    Guest newGuest = new Guest(newGuser, newGpass, name, "Guest", phone);
                                    sharedUsers.add(newGuest);
                                    try {
                                        newGuest.saveUsers(sharedUsers);
                                        System.out.println("Sign-up Successful!");
                                    } catch (Exception e) {
                                        System.out.println("An error occurred while saving the user: " + e.getMessage());
                                        e.printStackTrace();
                                    }

                                    boolean inMenu = true;
                                    while (inMenu) {
                                        System.out.println("-----Options-----");
                                        newGuest.displayOptions();
                                        System.out.println("What Would You Like To Do?");
                                        if (cred.hasNextInt()) {
                                            int opt = cred.nextInt();
                                            switch (opt) {
                                                case 1:
                                                    Menu mn = new Menu();
                                                    mn.displayMenu();
                                                    break;
                                                case 2:
                                                    newGuest.viewReservations(newGuest.getPhone());
                                                    break;
                                                case 3:
                                                    System.out.println("Please Enter a Rating Between 1 & 5");
                                                    int r = cred.nextInt();
                                                    newGuest.setRating(r);
                                                    break;
                                                case 4:
                                                    inMenu = false;
                                                    break;
                                                default:
                                                    System.out.println("Invalid option. Please choose again.");
                                            }
                                        } else {
                                            System.out.println("Please enter a valid option.");
                                        }
                                    }
                                    break;

                                case 3: // Exit
                                    runApp = false;
                                    System.out.println("Exiting Guest Account!");
                                    System.out.println("See You Next Time!");
                                    break;

                                default:
                                    System.out.println("Invalid choice. Please enter 1 for Login, 2 for Sign Up, or 3 to Exit.");
                                    break;
                            }
                        }
                    } catch (NoSuchElementException e) {
                        System.out.println("No input found: " + e.getMessage());
                    }
                    break;

                case 3: // Receptionist
                    Receptionist receptionist = new Receptionist();

                    attempts = 0;
                    while (attempts < 3) {
                        System.out.println("Enter Username: ");
                        String ruser = cred.next();
                        System.out.println("Enter Password: ");
                        String rpass = cred.next();

                        boolean aU = authenticateUser(ruser, rpass);
                        if (aU) {
                            System.out.println("Login successful!");
                            break;
                        } else {
                            System.out.println("Incorrect Username or Password. Please try again.");
                        }
                        attempts++;
                    }

                    if (attempts == 3) {
                        System.out.println("Too many failed attempts. Please try again later.");
                        break;
                    }

                    boolean inMenu = true;
                    while (inMenu) {
                        receptionist.displayOptions();
                        System.out.println("Choice: ");

                        int choice = input.nextInt();
                        input.nextLine();

                      switch (choice) {
    case 1:
        System.out.print("Reservation ID: ");
        int rID = input.nextInt();
        System.out.print("Table ID: ");
        int tID = input.nextInt();
        input.nextLine(); // consume the newline
        System.out.print("Guest Name: ");
        String gName = input.nextLine();
        System.out.print("Guest ID: ");
        int gID = input.nextInt();
        System.out.print("Phone Number: ");
        String phone = input.next();
        Date date = new Date();
        ArrayList<Meal> orderedMeals = new ArrayList<>();

        while (true) {
            
            m.displayMenu();
            System.out.print("Meal Name: ");
            input.nextLine(); // consume the newline
            String mealName = input.nextLine();

            System.out.print("Meal Category: ");
            String mealCategory = input.nextLine();

            System.out.print("Meal Price: ");
            double mealPrice = input.nextDouble();

            orderedMeals.add(new Meal(new StringBuffer(mealName), mealCategory, mealPrice));

            System.out.print("Do you want to add more meals? (yes/no): ");
            input.nextLine(); // consume the newline
            String moreMeals = input.nextLine();
            if (moreMeals.equalsIgnoreCase("no")) {
                break;
            }
        }

        receptionist.createReservations(rID, date, tID, gName, gID, phone, orderedMeals);
        System.out.println("Reservation created successfully");
        break;

   


                            case 2:
                                System.out.print("Enter Reservation Number: ");
                                int reservationNo = input.nextInt();
                                input.nextLine();
                                System.out.println("Available Meals:");
                                Menu menu = new Menu();
                                int index = 1;
                                System.out.println("Breakfast: ");
                                for (Meal meal : menu.getBreakfast()) {
                                    System.out.println(index++ + "." + meal.getName() + " - $" + meal.getPrice());
                                }
                                System.out.println("Lunch: ");
                                for (Meal meal : menu.getLunch()) {
                                    System.out.println(index++ + "." + meal.getName() + " - $" + meal.getPrice());
                                }
                                System.out.println("Dinner: ");
                                for (Meal meal : menu.getDinner()) {
                                    System.out.println(index++ + "." + meal.getName() + " - $" + meal.getPrice());
                                }
                                System.out.println("Drinks: ");
                                for (Meal meal : menu.getDrinks()) {
                                    System.out.println(index++ + "." + meal.getName() + " - $" + meal.getPrice());
                                }
                                System.out.println("Appetizers: ");
                                for (Meal meal : menu.getAppetizers()) {
                                    System.out.println(index++ + "." + meal.getName() + " - $" + meal.getPrice());
                                }

                                System.out.print("Enter the number corresponding to the meal: ");
                                int mealChoice = input.nextInt();
                                Meal selected = null;
                                index = 1;
                                for (Meal meal : menu.getBreakfast()) {
                                    if (index == mealChoice) {
                                        selected = meal;
                                        break;
                                    }
                                    index++;
                                }
                                if (selected == null) {
                                    for (Meal meal : menu.getLunch()) {
                                        if (index == mealChoice) {
                                            selected = meal;
                                            break;
                                        }
                                        index++;
                                    }
                                }
                                if (selected == null) {
                                    for (Meal meal : menu.getDinner()) {
                                        if (index == mealChoice) {
                                            selected = meal;
                                            break;
                                        }
                                        index++;
                                    }
                                }
                                if (selected == null) {
                                    for (Meal meal : menu.getDrinks()) {
                                        if (index == mealChoice) {
                                            selected = meal;
                                            break;
                                        }
                                        index++;
                                    }
                                }
                                if (selected == null) {
                                    for (Meal meal : menu.getAppetizers()) {
                                        if (index == mealChoice) {
                                            selected = meal;
                                            break;
                                        }
                                        index++;
                                    }
                                }
                                if (selected != null) {
                                    receptionist.addMealToReservation(reservationNo, selected, selected.getPrice());
                                    receptionist.saveReservations();
                                    System.out.println("Added Meal: " + selected.getName() + " - $" + selected.getPrice());
                                } else {
                                    System.out.println("Meal is not available");
                                }
                                break;
                            case 3:
                                System.out.println("Enter Reservation Number to Cancel: ");
                                reservationNo = input.nextInt();
                                receptionist.cancelReservation(reservationNo);
                                break;
                            case 4:
                                receptionist.loadReservations();
                                receptionist.viewReservations();
                                break;
                            case 5:
                                Receptionist.mostReservedTable();
                                break;
                            case 6:
                                Receptionist.mostOrderedMeal();
                                break;
                            case 7:
                                inMenu = false;
                                break;
                            default:
                                System.out.println("Please enter a valid choice.");
                                break;
                        }
                    }
                    break;

                case 4:
                    System.out.println("GOODBYE!!");
                    return;

                default:
                    System.out.println("Invalid option. Please enter a valid user type.");
                    break;
            }
        }
    }
}