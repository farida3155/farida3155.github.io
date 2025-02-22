package restaurant;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;

public class SignUpScene {

    public Scene createSignUpScene(Stage primaryStage, Scene backScene) {
        StackPane root = new StackPane(); // Create root pane for the scene

        // Reuse the shared background setup method
        BackgroundUtils.setupBackground(root, 0, -200, 600, 600); // Set the background for the scene

        // Create the signup label
        Label signUpLabel = new Label("Sign Up"); // Create label for signup header
        signUpLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Times New Roman'; -fx-font-size: 24px; -fx-padding: 10px;"); // Style label
        signUpLabel.setTranslateY(-10); // Adjust vertical position of the label

        // Create username label and text field
        Label usernameLabel = new Label("Username:"); // Label for the username input
        usernameLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Times New Roman'; -fx-font-size: 16px;"); // Style the label
        TextField usernameField = new TextField(); // Create a text field for the username input
        usernameField.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-family: 'Times New Roman'; -fx-font-size: 16px; -fx-padding: 10px;"); // Style the username field
        usernameField.setMaxWidth(200); // Set the maximum width for the username field

        // Place the label and text field in an HBox
        HBox usernameContainer = new HBox(10); // Create a horizontal box with 10px spacing
        usernameContainer.setAlignment(Pos.CENTER); // Center align the username container
        usernameContainer.getChildren().addAll(usernameLabel, usernameField); // Add the label and text field to the container
        usernameContainer.setTranslateY(-20); // Adjust vertical position of the username container

        // Create password label and text field
        Label passwordLabel = new Label("Password:"); // Label for the password input
        passwordLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Times New Roman'; -fx-font-size: 16px;"); // Style the password label
        PasswordField passwordField = new PasswordField(); // Create a password field for the password input
        passwordField.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-family: 'Times New Roman'; -fx-font-size: 16px; -fx-padding: 10px;"); // Style the password field
        passwordField.setMaxWidth(200); // Set the maximum width for the password field

        // Place the label and text field in an HBox
        HBox passwordContainer = new HBox(10); // Create a horizontal box with 10px spacing
        passwordContainer.setAlignment(Pos.CENTER); // Center align the password container
        passwordContainer.getChildren().addAll(passwordLabel, passwordField); // Add the label and password field to the container
        passwordContainer.setTranslateY(0); // Adjust vertical position of the password container

        // Create name label and text field
        Label nameLabel = new Label("Name:");
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Times New Roman'; -fx-font-size: 16px;");
        TextField nameField = new TextField();
        nameField.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-family: 'Times New Roman'; -fx-font-size: 16px; -fx-padding: 10px;");
        nameField.setMaxWidth(200);

        // Place the label and text field in an HBox
        HBox nameContainer = new HBox(10);
        nameContainer.setAlignment(Pos.CENTER);
        nameContainer.getChildren().addAll(nameLabel, nameField);
        nameContainer.setTranslateY(20);

        // Create phone label and text field
        Label phoneLabel = new Label("Phone Number:");
        phoneLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Times New Roman'; -fx-font-size: 16px;");
        TextField phoneField = new TextField();
        phoneField.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-family: 'Times New Roman'; -fx-font-size: 16px; -fx-padding: 10px;");
        phoneField.setMaxWidth(200);

        // Place the label and text field in an HBox
        HBox phoneContainer = new HBox(10);
        phoneContainer.setAlignment(Pos.CENTER);
        phoneContainer.getChildren().addAll(phoneLabel, phoneField);
        phoneContainer.setTranslateY(40);

        // Create the signup button
        Button signUpButton = new Button("Sign Up"); // Create the signup button
        signUpButton.setStyle("-fx-background-color: black; -fx-border-color: white; -fx-border-width: 1; -fx-text-fill: white; -fx-font-family: 'Times New Roman'; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-border-radius: 15px;"); // Style the signup button
        signUpButton.setPrefWidth(120); // Set the preferred width for the signup button
        signUpButton.setPrefHeight(40); // Set the preferred height for the signup button

        // Create the back button
        Button backButton = new Button("Back"); // Create the back button
        backButton.setStyle("-fx-background-color: black; -fx-border-color: white; -fx-border-width: 1; -fx-text-fill: white; -fx-font-family: 'Times New Roman'; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-border-radius: 15px;"); // Style the back button
        backButton.setPrefWidth(120); // Set the preferred width for the back button
        backButton.setPrefHeight(40); // Set the preferred height for the back button
        backButton.setOnAction(e -> primaryStage.setScene(backScene)); // Set the action for back button to switch to the first scene

        // Create an HBox to hold the Sign Up and Back buttons side by side
        HBox buttonContainer = new HBox(20); // Create horizontal box with 20px spacing between buttons
        buttonContainer.setAlignment(Pos.CENTER); // Center align the button container
        buttonContainer.getChildren().addAll(signUpButton, backButton); // Add both buttons to the container
        buttonContainer.setTranslateY(60); // Adjust vertical position of the button container

        // Add all components to the root stack pane
        VBox signUpContainer = new VBox(10); // Create a vertical box with 10px spacing between elements
        signUpContainer.setAlignment(Pos.CENTER); // Center align the signup container
        signUpContainer.getChildren().addAll(signUpLabel, usernameContainer, passwordContainer, nameContainer, phoneContainer, buttonContainer); // Add all elements to the container
        signUpContainer.setTranslateY(60); // Adjust vertical position of the signup container

        root.getChildren().add(signUpContainer); // Add the signup container to the root stack pane

        // Action when the signup button is pressed
        signUpButton.setOnAction(e -> {
            String username = usernameField.getText(); // Get username from the text field
            String password = passwordField.getText(); // Get password from the password field
            String name = nameField.getText(); // Get name from the text field
            String phone = phoneField.getText(); // Get phone number from the text field

            if (!username.isEmpty() && !password.isEmpty() && !name.isEmpty() && !phone.isEmpty()) {
                try {
                    ArrayList<User> users = User.loadUsers();

                    // Check if user already exists
                    for (User user : users) {
                        if (user.getUsername().equals(username)) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Sign Up Failed");
                            alert.setHeaderText(null);
                            alert.setContentText("Username already exists. Please log in.");
                            alert.showAndWait();
                            return;
                        }
                    }

                    // Save new user details
                    users.add(new Guest(username, password, name, "Guest", phone));
                    User.saveUsers(users);

                    // Navigate to GuestDashboardScene (assuming you have a separate class)
                    GuestDashboardScene guestDashboard = new GuestDashboardScene(phone, backScene);
                    primaryStage.setScene(guestDashboard.createGuestDashboardScene(primaryStage)); // Use the createScene method
                } catch (IOException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            } else {
                // Display error message if fields are empty
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("All fields are required!");
                alert.showAndWait();
            }
        });

        // Return the scene without setting its fixed size
        Scene signUpScene = new Scene(root, 800, 800, Color.BLACK); // Create the signup scene with a black background
        return signUpScene; // Return the created signup scene
    }
}