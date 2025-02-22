package restaurant;

import java.io.IOException;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ManageUsers {

    private VBox contentArea;
    private ArrayList<User> users;

    // Constructor to initialize users list
    public ManageUsers() {
        try {
            users = User.loadUsers(); // Load users from file
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    // Main layout
    private BorderPane createMainLayout() {
        BorderPane mainLayout = new BorderPane();

        // Sidebar with buttons
        VBox sidebar = new VBox(20);
        sidebar.setStyle("-fx-background-color: #2F4F4F; -fx-padding: 20px;");
        sidebar.setAlignment(Pos.TOP_CENTER);

        Button addButton = new Button("Add User");
        Button editButton = new Button("Edit User");
        Button removeButton = new Button("Remove User");

        styleButton(addButton);
        styleButton(editButton);
        styleButton(removeButton);

        addButton.setOnAction(e -> contentArea.getChildren().setAll(getAddUserForm()));
        editButton.setOnAction(e -> contentArea.getChildren().setAll(getEditUserForm()));
        removeButton.setOnAction(e -> contentArea.getChildren().setAll(getRemoveUserForm()));

        sidebar.getChildren().addAll(addButton, editButton, removeButton);

        // Content area for dynamic forms
        contentArea = new VBox(20);
        contentArea.setAlignment(Pos.CENTER);

        mainLayout.setLeft(sidebar);
        mainLayout.setCenter(contentArea);

        return mainLayout;
    }

    // Returns the Add User form as a VBox
    public VBox getAddUserForm() {
        VBox addForm = new VBox(20);
        addForm.setAlignment(Pos.CENTER);

        Label title = new Label("Add User");
        title.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        TextField roleField = new TextField();
        roleField.setPromptText("Role (admin, guest, receptionist)");

        TextField passwordField = new TextField();
        passwordField.setPromptText("Password");

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");

        Button addButton = new Button("Add User");
        styleButton(addButton);

        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-pref-width: 60px;");
        closeButton.setOnAction(e -> addForm.getChildren().clear());

        addButton.setOnAction(e -> {
            String username = usernameField.getText();
            String role = roleField.getText();
            String password = passwordField.getText();
            String name = nameField.getText();
            String phone = phoneField.getText();

            manageUsers(1, username, role, password, name, phone); // Add the user
            showAlert("Success", "User added successfully!");
        });

        // Create HBox to arrange buttons horizontally
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(addButton, closeButton);

        addForm.getChildren().addAll(title, usernameField, roleField, passwordField, nameField, phoneField, buttonBox);
        return addForm;
    }

    // Returns the Edit User form as a VBox
    public VBox getEditUserForm() {
        VBox editForm = new VBox(20);
        editForm.setAlignment(Pos.CENTER);

        Label title = new Label("Edit User");
        title.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        TextField roleField = new TextField();
        roleField.setPromptText("Role (admin, guest, receptionist)");

        TextField passwordField = new TextField();
        passwordField.setPromptText("Password");

        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone");

        Button editButton = new Button("Edit User");
        styleButton(editButton);

        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-pref-width: 60px;");
        closeButton.setOnAction(e -> editForm.getChildren().clear());

        editButton.setOnAction(e -> {
            String username = usernameField.getText();
            String role = roleField.getText();
            String password = passwordField.getText();
            String name = nameField.getText();
            String phone = phoneField.getText();

            manageUsers(2, username, role, password, name, phone); // Edit the user
            showAlert("Success", "User edited successfully!");
        });

        // Create HBox to arrange buttons horizontally
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(editButton, closeButton);

        editForm.getChildren().addAll(title, usernameField, roleField, passwordField, nameField, phoneField, buttonBox);
        return editForm;
    }

   // Returns the Remove User form as a VBox
public VBox getRemoveUserForm() {
    VBox removeForm = new VBox(20);
    removeForm.setAlignment(Pos.CENTER);

    Label title = new Label("Remove User");
    title.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-weight: bold;");

    TextField usernameField = new TextField();
    usernameField.setPromptText("Username");

    TextField passwordField = new TextField();
    passwordField.setPromptText("Password");

    Button removeButton = new Button("Remove User");
    styleButton(removeButton);

    Button closeButton = new Button("Close");
    closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-pref-width: 60px;");
    closeButton.setOnAction(e -> removeForm.getChildren().clear());

    removeButton.setOnAction(e -> {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validate the password before removing the user
        if (isValidPassword(username, password)) {
            manageUsers(3, username, null, null, null, null); // Remove the user
            showAlert("Success", "User removed successfully!");
        } else {
            showAlert("Error", "Invalid password.");
        }
    });

    // Create HBox to arrange buttons horizontally
    HBox buttonBox = new HBox(10);
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(removeButton, closeButton);

    removeForm.getChildren().addAll(title, usernameField, passwordField, buttonBox);
    return removeForm;
}

// Helper method to validate the password
private boolean isValidPassword(String username, String password) {
    for (User user : users) {
        if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
            return true;
        }
    }
    return false;
}


    // Styles a button
    private void styleButton(Button button) {
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-padding: 10px;");
    }

     private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setStyle("-fx-background-color: white; -fx-text-fill: white;");
        alert.getDialogPane().setPrefSize(300, 150);
        alert.showAndWait();

}

    // Handle user management actions (Add, Edit, Remove)
    private void manageUsers(int action, String username, String role, String password, String name, String phone) {
        try {
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
                            showAlert("Error", "Invalid role.");
                            return;
                    }
                    break;
                case 2: // Edit User
                    for (User user : users) {
                        if (user.getUsername().equals(username)) {
                            user.setRole(role);
                            user.setPassword(password);
                            user.setName(name);
                            user.setPhone(phone);
                            break;
                        }
                    }
                    break;
                case 3: // Remove User
                    users.removeIf(user -> user.getUsername().equals(username));
                    break;
                default:
                    showAlert("Error", "Invalid action.");
            }
            User.saveUsers(users); // Save users to file
        } catch (IOException e) {
            showAlert("Error", "Error managing users: " + e.getMessage());
        }
    }
}
