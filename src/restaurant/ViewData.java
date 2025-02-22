package restaurant;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ViewData {

    private VBox contentArea; // Area to display dynamic content
    private VBox currentView; // To track the currently open form

    // Method to create the main layout
    private BorderPane createMainLayout() {
        BorderPane mainLayout = new BorderPane();

        // Sidebar
        VBox sidebar = new VBox(15);
        sidebar.setStyle("-fx-background-color: #2F4F4F; -fx-padding: 20px;");
        sidebar.setAlignment(Pos.TOP_CENTER);

        // Sidebar Buttons
        Button usersButton = new Button("View Users");
        Button tablesButton = new Button("View Tables");
        Button menuButton = new Button("View Menu");
        Button reservationsButton = new Button("View Reservations");
        Button closeButton = new Button("Close");

        // Style Buttons
        for (Button button : new Button[]{usersButton, tablesButton, menuButton, reservationsButton}) {
            button.setMaxWidth(Double.MAX_VALUE);
            button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px;");
        }
        closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-padding: 10px;");
        closeButton.setMaxWidth(Double.MAX_VALUE);

        // Button Actions
        usersButton.setOnAction(e -> openFormInContentArea(UsersView()));
        tablesButton.setOnAction(e -> openFormInContentArea(TablesView()));
        menuButton.setOnAction(e -> openFormInContentArea(MenuView()));
        reservationsButton.setOnAction(e -> openFormInContentArea(ReservationsView()));
        closeButton.setOnAction(e -> closeForm()); // Close the form

        sidebar.getChildren().addAll(usersButton, tablesButton, menuButton, reservationsButton, closeButton);

        // Content Area
        contentArea = new VBox(20);
        contentArea.setStyle("-fx-padding: 20px; -fx-background-color: #f4f4f4;");
        contentArea.setAlignment(Pos.TOP_LEFT);

        // Add Sidebar and Content Area to Main Layout
        mainLayout.setLeft(sidebar);
        mainLayout.setCenter(contentArea);

        return mainLayout;
    }

    // Method to open a form in the content area
    private void openFormInContentArea(VBox view) {
        currentView = view; // Track the current view
        contentArea.getChildren().setAll(view);
    }

    // Method to close the current form
    private void closeForm() {
        if (currentView != null) {
            contentArea.getChildren().clear(); // Clear the content area
            currentView = null; // Reset the current view
        }
    }

    // Users View
    public VBox UsersView() {
        VBox usersBox = new VBox(10);
        usersBox.setAlignment(Pos.TOP_LEFT);
        usersBox.setStyle("-fx-padding: 10px;");

        try {
            ArrayList<User> users = User.loadUsers();
            if (users.isEmpty()) {
                usersBox.getChildren().add(new Label("No users found."));
            } else {
                for (User user : users) {
                    Label userLabel = new Label(
                            "Username: " + user.getUsername() +
                                    "\nName: " + user.getName() +
                                    "\nRole: " + user.getRole() +
                                    "\nPhone: " + user.getPhone()
                    );
                    userLabel.setStyle("-fx-text-fill: black; -fx-font-size: 14px;");
                    usersBox.getChildren().add(userLabel);
                }
            }
        } catch (Exception e) {
            usersBox.getChildren().add(new Label("Error loading users: " + e.getMessage()));
        }

        // Close button
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-padding: 10px;");
        closeButton.setOnAction(e -> closeForm()); // Close form logic
        usersBox.getChildren().add(closeButton);

        return usersBox;
    }

    // Tables View
    public VBox TablesView() {
        VBox tablesBox = new VBox(10);
        tablesBox.setAlignment(Pos.TOP_LEFT);
        tablesBox.setStyle("-fx-padding: 10px;");

        ArrayList<Table> tables = Table.loadTablesFromFile();
        if (tables.isEmpty()) {
            tablesBox.getChildren().add(new Label("No tables found."));
        } else {
            for (Table table : tables) {
                Label tableLabel = new Label(
                        "Table ID: " + table.getTableId() +
                                "\nCategory: " + table.getCategory() +
                                "\nCapacity: " + table.getCapacity() +
                                "\nReserved: " + (table.isReserved() ? "Yes" : "No")
                );
                tableLabel.setStyle("-fx-text-fill: black; -fx-font-size: 14px;");
                tablesBox.getChildren().add(tableLabel);
            }
        }

        // Close button
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-padding: 10px;");
        closeButton.setOnAction(e -> closeForm()); // Close form logic
        tablesBox.getChildren().add(closeButton);

        return tablesBox;
    }

    // Menu View
    public VBox MenuView() {
        VBox menuBox = new VBox(10);
        menuBox.setAlignment(Pos.TOP_LEFT);
        menuBox.setStyle("-fx-padding: 10px;");

        Menu menu = new Menu(); // Initialize the menu
        menu.initializeFiles(); // Load menu data from files

        // Add categories to the menu view
        addCategoryToMenuBox("Appetizers", menu.getAppetizers(), menuBox);
        addCategoryToMenuBox("Breakfast", menu.getBreakfast(), menuBox);
        addCategoryToMenuBox("Lunch", menu.getLunch(), menuBox);
        addCategoryToMenuBox("Dinner", menu.getDinner(), menuBox);
        addCategoryToMenuBox("Drinks", menu.getDrinks(), menuBox);

        // Close button
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-padding: 10px;");
        closeButton.setOnAction(e -> closeForm()); // Close form logic
        menuBox.getChildren().add(closeButton);

        return menuBox;
    }

    // Helper method to add menu categories
    public void addCategoryToMenuBox(String category, ArrayList<Meal> meals, VBox menuBox) {
        Label categoryLabel = new Label(category + ":");
        categoryLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        menuBox.getChildren().add(categoryLabel);

        if (meals.isEmpty()) {
            Label emptyLabel = new Label("No items found.");
            emptyLabel.setStyle("-fx-text-fill: grey; -fx-font-size: 14px;");
            menuBox.getChildren().add(emptyLabel);
        } else {
            for (Meal meal : meals) {
                Label mealLabel = new Label(
                        "Meal Name: " + meal.getName() +
                                "\nCategory: " + meal.getCategory() +
                                "\nPrice: $" + meal.getPrice()
                );
                mealLabel.setStyle("-fx-text-fill: black; -fx-font-size: 14px;");
                menuBox.getChildren().add(mealLabel);
            }
        }
    }

    // Reservations View
    public VBox ReservationsView() {
        VBox reservationsBox = new VBox(10);
        reservationsBox.setAlignment(Pos.TOP_LEFT);
        reservationsBox.setStyle("-fx-padding: 10px;");

        Receptionist receptionist = new Receptionist();
        receptionist.loadReservations();
        ArrayList<Reservation> reservations = receptionist.getReservations();

        if (reservations.isEmpty()) {
            reservationsBox.getChildren().add(new Label("No reservations found."));
        } else {
            for (Reservation reservation : reservations) {
                Label reservationLabel = new Label(
                        "Reservation ID: " + reservation.getResID() +
                                "\nGuest Name: " + reservation.getGuestName() +
                                "\nPhone: " + reservation.getPhone() +
                                "\nDate and Time: " + reservation.getDateTime() +
                                "\nTable ID: " + reservation.getTableID() +
                                "\nTotal Payment: $" + reservation.getPayment()
                );
                reservationLabel.setStyle("-fx-text-fill: black; -fx-font-size: 14px;");

                VBox mealsBox = new VBox(5);
                for (Meal meal : reservation.getOrderedMeals()) {
                    Label mealLabel = new Label(
                            "  Meal Name: " + meal.getName() +
                                    "\n  Category: " + meal.getCategory() +
                                    "\n  Price: $" + meal.getPrice()
                    );
                    mealLabel.setStyle("-fx-text-fill: black; -fx-font-size: 12px;");
                    mealsBox.getChildren().add(mealLabel);
                }
                reservationsBox.getChildren().addAll(reservationLabel, mealsBox);
            }
        }

        // Close button
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-padding: 10px;");
        closeButton.setOnAction(e -> closeForm()); // Close form logic
        reservationsBox.getChildren().add(closeButton);

        return reservationsBox;
    }

    // Display the ViewData scene
    public void showViewDataWindow(Stage primaryStage) {
        BorderPane mainLayout = createMainLayout();
        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("View Data");
        primaryStage.show();
    }
}
