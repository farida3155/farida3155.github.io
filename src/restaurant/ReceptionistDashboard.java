package restaurant;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.scene.paint.Color;

public class ReceptionistDashboard {

    Receptionist receptionist = new Receptionist(); // Assuming you have a Receptionist class managing reservations
    List<Reservation> reservations;

    private Scene firstScene; // Store the first scene

    // Constructor to accept the firstScene
    public ReceptionistDashboard(Scene firstScene) {
        this.firstScene = firstScene;
    }

    // Creates the admin dashboard scene
    public Scene createReceptionistDashboard(Stage primaryStage) {
        BorderPane root = new BorderPane(); // Create a BorderPane layout to structure the UI
 
      
        // Create and set the main sidebar (left side of the screen)
        VBox mainSidebar = createSidebar(primaryStage, root);
        root.setLeft(mainSidebar); // Set the main sidebar to the left of the BorderPane

        // Placeholder for the center content of the dashboard
        VBox mainContent = new VBox(20); // Create a vertical box layout with 20px spacing between elements
        mainContent.setAlignment(Pos.CENTER); // Align the content to the center
        Label placeholderLabel = new Label("Select an option from the sidebar"); // Placeholder label to prompt user
        mainContent.getChildren().add(placeholderLabel); // Add the label to the main content
        root.setCenter(mainContent); // Set the main content in the center of the BorderPane

        return new Scene(root, 800, 800, Color.BLACK); // Return a new Scene with black background and size 800x800  
    }

    private VBox createSidebar(Stage primaryStage, BorderPane mainLayout) {
        // Create the sidebar layout
        VBox sidebar = new VBox(20);
        sidebar.setStyle("-fx-background-color: #333333; -fx-padding: 20px;");
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setPrefWidth(300);

        
    // Create the title for the sidebar
    Label dashboardTitle = new Label("Receptionist Dashboard");
    dashboardTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10px;");

        
        
        // Create buttons for each action
        Button createButton = createButton("Create Reservation", e -> {
            // Directly set the content in the main window when clicked
            mainLayout.setCenter(createReservationForm());
        });

        Button addMealButton = createButton("Add Meal to Reservation", e -> {
            // Directly set the content in the main window when clicked
            addMealToReservationForm(mainLayout);
        });

        Button cancelButton = createButton("Cancel Reservation", e -> {
            // Set the content for cancel reservation directly in the main layout
            cancelReservationForm(mainLayout);
        });

        Button viewReservationsButton = createButton("View Reservations", e -> {
            // Set the content for viewing reservations directly in the main layout
            viewReservations(mainLayout);
        });

        Button mostReservedTableButton = createButton("Most Reserved Table", e -> {
            // Set the content for viewing the most reserved table directly in the main layout
            mostReservedTable(mainLayout);
        });

        Button mostOrderedMealButton = createButton("Most Ordered Meal", e -> {
            // Set the content for viewing the most ordered meal directly in the main layout
            mostOrderedMeal(mainLayout);
        });

        // Back Button to return to LoginScene
        Button backButton = createButton("Back", e -> primaryStage.setScene(firstScene));
        backButton.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-pref-width: 180px;");

        // Add all buttons to the sidebar
        sidebar.getChildren().addAll(dashboardTitle,createButton, addMealButton, cancelButton, viewReservationsButton, mostReservedTableButton, mostOrderedMealButton, backButton);

        return sidebar;
    }

    private Button createButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px; -fx-pref-width: 180px;");
        button.setMaxWidth(400);
        button.setOnAction(handler);
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px; -fx-pref-width: 180px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px; -fx-pref-width: 180px;"));

        return button;
    }

    private VBox createReservationForm() {
        // Form elements for reservation creation
        VBox formLayout = new VBox(10);
        formLayout.setAlignment(Pos.CENTER);
        formLayout.setPrefWidth(600); // Set the preferred width of the form layout

        // Reservation form UI elements
        TextField resIdField = new TextField();
        resIdField.setPromptText("Reservation ID");
        resIdField.setPrefWidth(300); // Set width of the TextField

        DatePicker datePicker = new DatePicker();
        datePicker.setPrefWidth(300); // Set width of the DatePicker

        TextField tableIdField = new TextField();
        tableIdField.setPromptText("Table ID");
        tableIdField.setPrefWidth(300); // Set width of the TextField

        TextField guestNameField = new TextField();
        guestNameField.setPromptText("Guest Name");
        guestNameField.setPrefWidth(300); // Set width of the TextField

        TextField guestIdField = new TextField();
        guestIdField.setPromptText("Guest ID");
        guestIdField.setPrefWidth(300); // Set width of the TextField

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");
        phoneField.setPrefWidth(300); // Set width of the TextField

        // Add these UI elements to the form layout
        formLayout.getChildren().addAll(
                new Label("Enter Reservation Details:"),
                new Label("Reservation ID:"), resIdField,
                new Label("Date:"), datePicker,
                new Label("Table ID:"), tableIdField,
                new Label("Guest Name:"), guestNameField,
                new Label("Guest ID:"), guestIdField,
                new Label("Phone:"), phoneField
        );

        // Ordered meals using ComboBox
        VBox mealsBox = new VBox(10);
        mealsBox.getChildren().add(new Label("Ordered Meals:"));

        Menu menu = new Menu();
        menu.initializeFiles(); // Load menu items

        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll("Breakfast", "Lunch", "Dinner", "Drinks", "Appetizers");
        categoryComboBox.setPromptText("Select Category");

        ComboBox<Meal> mealComboBox = new ComboBox<>();
        mealComboBox.setPromptText("Select Meal");

        categoryComboBox.setOnAction(e -> {
            String selectedCategory = categoryComboBox.getValue();
            mealComboBox.getItems().clear();

            switch (selectedCategory) {
                case "Breakfast":
                    mealComboBox.getItems().addAll(menu.getBreakfast());
                    break;
                case "Lunch":
                    mealComboBox.getItems().addAll(menu.getLunch());
                    break;
                case "Dinner":
                    mealComboBox.getItems().addAll(menu.getDinner());
                    break;
                case "Drinks":
                    mealComboBox.getItems().addAll(menu.getDrinks());
                    break;
                case "Appetizers":
                    mealComboBox.getItems().addAll(menu.getAppetizers());
                    break;
            }
        });

        Button addMealButton = new Button("Add Meal");
        List<Meal> orderedMeals = new ArrayList<>();
        Label selectedMealsLabel = new Label("Selected Meals: " + orderedMeals);

        addMealButton.setOnAction(e -> {
            Meal selectedMeal = mealComboBox.getValue();
            if (selectedMeal != null) {
                orderedMeals.add(selectedMeal);
                selectedMealsLabel.setText("Selected Meals: " + orderedMeals.stream()
                        .map(Meal::getName)
                        .collect(Collectors.joining(", ")));
            } else {
                // Show an alert if no meal is selected
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Meal Selected");
                alert.setHeaderText(null);
                alert.setContentText("Please select a meal to add.");
                alert.showAndWait();
            }
        });

        mealsBox.getChildren().addAll(
                new Label("Select Category:"), categoryComboBox,
                new Label("Select Meal:"), mealComboBox,
                addMealButton,
                selectedMealsLabel
        );

        // Adding meal selection to the main form layout
        formLayout.getChildren().add(mealsBox);

        // Create the Add Reservation button
        Button addReservationButton = new Button("Add Reservation");
        Button cancelButton = new Button("Cancel");

        addReservationButton.setOnAction(e -> {
            try {
                // Parse and validate inputs
                int resId = Integer.parseInt(resIdField.getText().trim());
                Date date = java.sql.Date.valueOf(datePicker.getValue());
                int tableId = Integer.parseInt(tableIdField.getText().trim());
                String guestName = guestNameField.getText().trim();
                int guestId = Integer.parseInt(guestIdField.getText().trim());
                String phone = phoneField.getText().trim();

                // Call the method to create a reservation
                receptionist.createReservations(resId, date, tableId, guestName, guestId, phone, orderedMeals); // No meals for now

                // Show success alert
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Reservation created successfully!");
                successAlert.showAndWait();

            } catch (Exception ex) {
                // Show error alert
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Invalid input! Please ensure all fields are filled correctly.");
                errorAlert.showAndWait();
            }
        });

        // Action for Cancel button
        cancelButton.setOnAction(e -> {
            // Reset the form fields if cancel is clicked
            resIdField.clear();
            datePicker.setValue(null);
            tableIdField.clear();
            guestNameField.clear();
            guestIdField.clear();
            phoneField.clear();
            categoryComboBox.getSelectionModel().clearSelection();
            mealComboBox.getItems().clear();
            selectedMealsLabel.setText("Selected Meals: None");
        });

        // Buttons container with both Add and Cancel buttons
        HBox buttonBox = new HBox(10, addReservationButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Add the button container to the form layout
        formLayout.getChildren().add(buttonBox);

        return formLayout; // Return the layout with all components
    }

    private void addMealToReservationForm(BorderPane root) {
        // Create a VBox to hold the meal reservation form content
        VBox mealFormLayout = new VBox(10);
        mealFormLayout.setAlignment(Pos.CENTER);

        Menu menu = new Menu(); // Initialize the menu
        menu.initializeFiles(); // Ensure menu items are loaded

        // Reservation ID input
        TextField resIdField = new TextField();
        resIdField.setPromptText("Reservation ID");
        resIdField.setPrefWidth(300); // Set the width of the text field

        // ComboBox for selecting a meal category
        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll("Breakfast", "Lunch", "Dinner", "Drinks", "Appetizers");
        categoryComboBox.setPromptText("Select Category");
        categoryComboBox.setPrefWidth(300); // Set the width of the combo box

        ComboBox<Meal> mealComboBox = new ComboBox<>();
        mealComboBox.setPromptText("Select Meal");
        mealComboBox.setPrefWidth(300); // Set the width of the combo box

        // Update mealComboBox based on selected category
        categoryComboBox.setOnAction(e -> {
            String selectedCategory = categoryComboBox.getValue();
            mealComboBox.getItems().clear();

            switch (selectedCategory) {
                case "Breakfast":
                    mealComboBox.getItems().addAll(menu.getBreakfast());
                    break;
                case "Lunch":
                    mealComboBox.getItems().addAll(menu.getLunch());
                    break;
                case "Dinner":
                    mealComboBox.getItems().addAll(menu.getDinner());
                    break;
                case "Drinks":
                    mealComboBox.getItems().addAll(menu.getDrinks());
                    break;
                case "Appetizers":
                    mealComboBox.getItems().addAll(menu.getAppetizers());
                    break;
            }
        });

        // Layout for the form
        mealFormLayout.getChildren().addAll(
                new Label("Enter Meal Details:"),
                new Label("Reservation ID:"), resIdField,
                new Label("Select Category:"), categoryComboBox,
                new Label("Select Meal:"), mealComboBox
        );

        mealFormLayout.setAlignment(Pos.CENTER);
        mealFormLayout.setPrefWidth(400); // Set the preferred width for the layout

        // Buttons for adding meal
        Button addMealButton = new Button("Add Meal");
        addMealButton.setOnAction(e -> {
            try {
                // Get selected values
                int resId = Integer.parseInt(resIdField.getText().trim());
                Meal selectedMeal = mealComboBox.getValue();

                if (selectedMeal == null) {
                    throw new Exception("No meal selected");
                }

                // Add meal to reservation
                receptionist.addMealToReservation(resId, selectedMeal, selectedMeal.getPrice());

                // Call the existing showAlert method for success
                showAlert("Success", "Meal added successfully!");
            } catch (Exception ex) {
                // Call the existing showAlert method for error
                showAlert("Error", "Invalid input or selection! Please ensure all fields are filled correctly.");
            }
        });

        mealFormLayout.getChildren().add(addMealButton);

        // Set the center of the root to the meal form layout
        root.setCenter(mealFormLayout);
    }

    private void cancelReservationForm(BorderPane mainLayout) {
        // Create a VBox layout for the form
        VBox formLayout = new VBox(10);
        formLayout.setAlignment(Pos.CENTER);

        // Reservation ID input
        TextField resIdField = new TextField();
        resIdField.setPromptText("Reservation ID");

        // Create a submit button
        Button submitButton = new Button("Cancel Reservation");
        submitButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;");
        submitButton.setOnAction(e -> {
            int resId = Integer.parseInt(resIdField.getText().trim());

            // Remove the reservation from the list
            reservations.removeIf(r -> r.getResID() == resId);

            receptionist.cancelReservation(resId); // Call the cancellation method in Receptionist

            // Update the displayed reservations
            viewReservations(mainLayout);
        });

        // Add all elements to the layout
        formLayout.getChildren().addAll(new Label("Enter Reservation ID to Cancel:"), resIdField, submitButton);

        // Set the form as the center of the main layout
        mainLayout.setCenter(formLayout);
    }

    private void viewReservations(BorderPane mainLayout) {
        // Load reservations from the Receptionist class
        receptionist.loadReservations();
        reservations = receptionist.getReservations();

      
    String result;
    if (reservations == null || reservations.isEmpty()) {
        result = "No reservations found.";
    }

        StringBuilder sb = new StringBuilder();
        sb.append("========= Reservations =========\n");
        for (Reservation res : reservations) {
            sb.append("Reservation ID: ").append(res.getResID()).append("\n");
            sb.append("Guest Name   : ").append(res.getGuestName()).append("\n");
            sb.append("Phone        : ").append(res.getPhone()).append("\n");
            sb.append("Date & Time  : ").append(res.getDateTime()).append("\n");
            sb.append("Table ID     : ").append(res.getTableID()).append("\n");
            sb.append("Ordered Meals:\n");

            for (Meal meal : res.getOrderedMeals()) {
                sb.append("    - Meal Name : ").append(meal.getName()).append("\n");
                sb.append("      Category  : ").append(meal.getCategory()).append("\n");
                sb.append("      Price     : $").append(meal.getPrice()).append("\n");
            }

            sb.append("\n---------------------------------\n");
        }
        sb.append("=================================");

        VBox reservationsBox = new VBox(10);
        reservationsBox.setAlignment(Pos.CENTER);
        reservationsBox.getChildren().add(new Label(sb.toString()));
        mainLayout.setCenter(reservationsBox);
    }

    private void mostReservedTable(BorderPane mainLayout) {
        // Get the most reserved table info
        String result = receptionist.mostReservedTable();

        // Create a VBox to display the result
        VBox resultBox = new VBox(10);
        resultBox.setAlignment(Pos.CENTER);
        resultBox.getChildren().add(new Label("Most Reserved Table:"));
        resultBox.getChildren().add(new Label(result));

        // Set this VBox as the center of the main layout
        mainLayout.setCenter(resultBox);
    }

private void mostOrderedMeal(BorderPane mainLayout) {
    // Get all reservations from the receptionist
    receptionist.loadReservations();
    reservations = receptionist.getReservations(); // Synchronize the reservations list

  
    String result;
    if (reservations == null || reservations.isEmpty()) {
        result = "No reservations found. Cannot determine most ordered meal.";
    }

    // Create a map to store meal counts
    Map<String, Integer> mealCountMap = new HashMap<>();

    // Iterate through reservations and count meal occurrences
    for (Reservation reservation : reservations) {
        for (Meal meal : reservation.getOrderedMeals()) {
            String mealName = meal.getName().toString();
            int currentCount = mealCountMap.getOrDefault(mealName, 0);
            mealCountMap.put(mealName, currentCount + 1);
        }
    }

    // Find the meal with the highest count
    String mostOrderedMealName = null;
    int highestCount = 0;
    for (Map.Entry<String, Integer> entry : mealCountMap.entrySet()) {
        String mealName = entry.getKey();
        int count = entry.getValue();
        if (count > highestCount) {
            mostOrderedMealName = mealName;
            highestCount = count;
        }
    }

    // Prepare the result message
  
    if (mostOrderedMealName != null) {
        result = "The most ordered meal is: " + mostOrderedMealName;
    } else {
        result = "There is no clear most ordered meal (equal counts for all meals).";
    }

    // Create a VBox to display the result
    VBox resultBox = new VBox(10);
    resultBox.setAlignment(Pos.CENTER);
    resultBox.getChildren().add(new Label("Most Ordered Meal:"));
    resultBox.getChildren().add(new Label(result));

    // Set this VBox as the center of the main layout
    mainLayout.setCenter(resultBox);
}

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setStyle("-fx-background-color: white ; -fx-text-fill: white;");
        alert.getDialogPane().setPrefSize(300, 150);
        alert.showAndWait();

}
   
}