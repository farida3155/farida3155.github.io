package restaurant;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import restaurant.Admin;

public class AdminGUI {

    private Admin admin;

    public AdminGUI(Admin admin) {
        this.admin = admin;
    }

    public void start(Stage primaryStage) {
        // Create UI components
        Button manageTablesButton = new Button("Manage Tables");
        Button manageMenuButton = new Button("Manage Menu");
        Button manageUsersButton = new Button("Manage Users");
        Button viewReportsButton = new Button("View Reports");
        Button viewDataButton = new Button("View Data");

        // Create a layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(
                manageTablesButton,
                manageMenuButton,
                manageUsersButton,
                viewReportsButton,
                viewDataButton
        );

        // Button handlers
        manageTablesButton.setOnAction(event -> manageTables());
        manageMenuButton.setOnAction(event -> manageMenu());
        manageUsersButton.setOnAction(event -> manageUsers());
        viewReportsButton.setOnAction(event -> viewReports());
        viewDataButton.setOnAction(event -> viewData());

        // Set up the scene and stage
        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Manage Tables action
    private void manageTables() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Manage Tables");
        dialog.setHeaderText("Enter the table action (1: Add, 2: Edit, 3: Remove, 4: Add Category, 5: View Categories):");
        dialog.showAndWait().ifPresent(action -> {
            // Process action
            int actionInt = Integer.parseInt(action);
            // Prompt for other necessary inputs like tableID, category, etc. 
            // (you can extend this with more fields if necessary)
            showMessage("Table action " + actionInt + " selected.");
            admin.manageTables(actionInt, 0, "", 0, false); // Dummy values, adjust as per requirements
        });
    }

    // Manage Menu action
    private void manageMenu() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Manage Menu");
        dialog.setHeaderText("Enter the menu action (1: Add, 2: Edit, 3: Remove):");
        dialog.showAndWait().ifPresent(action -> {
            int actionInt = Integer.parseInt(action);
            // Similarly, ask for menu name, price, category
            showMessage("Menu action " + actionInt + " selected.");
            admin.manageMenu(actionInt, new StringBuffer("Sample Menu"), 10.0, "lunch"); // Dummy values
        });
    }

    // Manage Users action
    private void manageUsers() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Manage Users");
        dialog.setHeaderText("Enter the user action (1: Add, 2: Edit, 3: Remove):");
        dialog.showAndWait().ifPresent(action -> {
            int actionInt = Integer.parseInt(action);
            // Prompt for other user info (username, password, role, etc.)
            showMessage("User action " + actionInt + " selected.");
            admin.manageUsers(actionInt, "username", "admin", "password", "Name", "1234567890"); // Dummy values
        });
    }

    // View Reports action
    private void viewReports() {
        showMessage("Viewing reports...");
        admin.viewReports();
    }

    // View Data action (Tables, Menu, Users, etc.)
    private void viewData() {
        // Prompt to view tables or menu or users (you can create further dialog boxes for that)
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("View Data");
        alert.setHeaderText("Select data to view");
        ButtonType tablesButton = new ButtonType("Tables");
        ButtonType menuButton = new ButtonType("Menu");
        ButtonType usersButton = new ButtonType("Users");
        alert.getButtonTypes().setAll(tablesButton, menuButton, usersButton);

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == tablesButton) {
                showMessage("Viewing tables...");
                admin.viewTables();
            } else if (buttonType == menuButton) {
                showMessage("Viewing menu...");
                admin.viewMenu();
            } else if (buttonType == usersButton) {
                showMessage("Viewing users...");
                admin.viewUsers();
            }
        });
    }

    // Helper method to display messages
    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
