package restaurant;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Receptionist extends User implements Serializable {
    private ArrayList<Reservation> reservations;
    private ArrayList<Table> tables;
    private ArrayList<Meal> menuItems;
    private static HashMap<Integer, Integer> tableReservation = new HashMap<>();
    private static int mostReservedTable = 0;
    private static HashMap<String, Integer> mealOrder = new HashMap<>();
    private static String mostOrderedMeal = null;
    private static final long serialVersionUID = 1L;

    public Receptionist() {
        reservations = new ArrayList<>();
        tables = new ArrayList<>();
        menuItems = new ArrayList<>();
    }

    public Receptionist(String username, String password, String name, String role, String phone) {
        super(username, password, name, "Receptionist", phone);
    }

    public void createReservations(int rID, Date dT, int tID, String gName, int gID, String phone, List<Meal> orderedMeals) {
        reservations.add(new Reservation(rID, dT, tID, gName, gID, phone, orderedMeals));
        addReservation(tID);
        saveReservations();
    }

    public void addMealToReservation(int reservationNo, Meal meal, double mealPrice) {
        loadReservations(); 
        System.out.println("Attempting to add a meal to reservation number: " + reservationNo);
 

        if (reservationNo >= 0 && reservationNo < reservations.size()) {
            Reservation reservation = reservations.get(reservationNo);
            List<Meal> orderedMeals = reservation.getOrderedMeals();

            orderedMeals.add(meal);
            reservation.setOrderedMeals(orderedMeals);
            reservation.calculatePayment();
            saveReservations();
            addMealOrder(meal);

            System.out.println("Meal added successfully to the reservation.");
        } else {
            System.out.println("Invalid Reservation number. Please re-enter the reservation number.");
        }
    }

 
    public void cancelReservation(int reservationNo) {
    loadReservations(); // Ensure the latest reservations are loaded

    Reservation reservationToRemove = null;

    // Find the reservation by its ID
    for (Reservation reservation : reservations) {
        if (reservation.getResID() == reservationNo) {
            reservationToRemove = reservation;
            break;
        }
    }

    if (reservationToRemove != null) {
        reservations.remove(reservationToRemove); // Remove the entire reservation
        System.out.println("Reservation with ID " + reservationNo + " has been cancelled.");
        saveReservations(); // Save the updated list
    } else {
        System.out.println("Invalid reservation number! Reservation not found.");
}
    }


    public void viewReservations() {
        loadReservations(); // Ensure the latest reservations are loaded

        if (reservations.isEmpty()) {
            System.out.println("No reservations found in the file.");
            return;
        }

        System.out.println("Reservations:");
        for (Reservation reservation : reservations) {
            System.out.println("Reservation ID: " + reservation.getResID());
            System.out.println("Guest Name: " + reservation.getGuestName());
            System.out.println("Phone: " + reservation.getPhone());
            System.out.println("Date and Time: " + reservation.getDateTime());
            System.out.println("Table ID: " + reservation.getTableID());

            System.out.println("Ordered Meals:");
            for (Meal meal : reservation.getOrderedMeals()) {
                System.out.println("  Meal Name: " + meal.getName());
                System.out.println("  Category: " + meal.getCategory());
                System.out.println("  Price: $" + meal.getPrice());
            }
            System.out.println("------------------------");
        }
    }

    public void saveReservations() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Reservations.dat"))) {
            oos.writeObject(reservations);
            System.out.println("Reservations and meal orders have been saved to the file.");
        } catch (IOException e) {
            System.err.println("Error saving reservations to file: " + e.getMessage());
        }
    }



    public void loadReservations() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Reservations.dat"))) {
            reservations = (ArrayList<Reservation>) ois.readObject();
            System.out.println("Reservations have been loaded from the file 'Reservations.dat'.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading reservations from file: " + e.getMessage());
            reservations = new ArrayList<>(); // Initialize to avoid null pointer issues
        }
    }

    public void loadReservations(String phone) {
        List<Reservation> guestReservations = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Reservations.dat"))) {
            List<Reservation> allReservations = (List<Reservation>) ois.readObject();
            for (Reservation reservation : allReservations) {
                if (reservation.getPhone().equals(phone)) {
                    guestReservations.add(reservation);
                }
            }

            if (guestReservations.isEmpty()) {
                System.out.println("No reservations found for Phone: " + phone);
            } else {
                System.out.println("Reservations for Phone" + phone + ":");
                for (Reservation res : guestReservations) {
                    System.out.println(res);
                    for (Meal meal : res.getOrderedMeals()) {
                        System.out.println("  Meal Name: " + meal.getName());
                        System.out.println("  Category: " + meal.getCategory());
                        System.out.println("  Price: $" + meal.getPrice());
                    }
                    System.out.println("------------------------");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading reservations from file: " + e.getMessage());
        }
    }

    @Override
    public void displayOptions() {
        System.out.println("1. Create Reservation");
        System.out.println("2. Add meal to Reservation");
        System.out.println("3. Cancel Reservation");
        System.out.println("4. View Reservations");
        System.out.println("5. View Most Reserved Table");
        System.out.println("6. View Most Ordered Meal");
        System.out.println("7. Exit");
    }

    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
    }

    public ArrayList<Table> getTables() {
        return tables;
    }

    public void setTables(ArrayList<Table> tables) {
        this.tables = tables;
    }

    public ArrayList<Meal> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(ArrayList<Meal> menuItems) {
        this.menuItems = menuItems;
    }

    private static void addReservation(int tableID) {
        tableReservation.put(tableID, tableReservation.getOrDefault(tableID, 0) + 1);
        if (mostReservedTable == 0 || tableReservation.get(tableID) > tableReservation.get(mostReservedTable)) {
            mostReservedTable = tableID;
        }
    }

    private static void addMealOrder(Meal meal) {
        String mealName = meal.getName().toString();
        mealOrder.put(mealName, mealOrder.getOrDefault(mealName, 0) + 1);
        
        if (mostOrderedMeal == null || mealOrder.get(mealName) > mealOrder.get(mostOrderedMeal)) {
            mostOrderedMeal = mealName;
        }
    }

    public static String mostReservedTable() {
        if (mostReservedTable != 0) {
            return "Most Reserved Table: Table " + mostReservedTable;
        } else {
            
           return "No reservations yet.";
        }
    }
    

   public static String mostOrderedMeal() {
    if (mostOrderedMeal != null) {
        return "Most Ordered Meal: " + mostOrderedMeal;
    } else {
        return "No meals ordered yet.";
    }
}

}
