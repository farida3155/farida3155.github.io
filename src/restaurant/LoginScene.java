package restaurant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginScene {

    // Authenticate user by checking credentials from Users.dat and determine role
    public static String authenticateUser(String username, String password) throws IOException, ClassNotFoundException {
        File userFile = new File("Users.dat"); // Create file object for the user data file
        if (!userFile.exists() || userFile.length() == 0) { // Check if the file exists and is not empty
            throw new IOException("User file does not exist or is empty."); // Throw error if file is missing or empty
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userFile))) { // Read user data from the file
            ArrayList<User> users = (ArrayList<User>) ois.readObject(); // Deserialize user data into an ArrayList
            for (User user : users) { // Loop through each user
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) { // Check if credentials match
                    return user.getRole(); // Return the role (Admin or Receptionist)
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new IOException("Error loading users from file: " + e.getMessage()); // Handle errors in reading the file or class
        }
        return null; // Return null if no matching user is found
    }

    public Scene LoginScene(Stage primaryStage, Scene firstScene) {
        StackPane root = new StackPane(); // Create root pane for the scene

        // Reuse the shared background setup method
        BackgroundUtils.setupBackground(root, 0, -200, 600, 600); // Set the background for the scene

        // Create the login label
        Label loginLabel = new Label("Login"); // Create label for login header
        loginLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Times New Roman'; -fx-font-size: 24px; -fx-padding: 10px;"); // Style label
        loginLabel.setTranslateY(-15); // Adjust vertical position of the label

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

        // Create the submit button
        Button submitButton = new Button("Submit"); // Create the submit button
        submitButton.setStyle("-fx-background-color: black; -fx-border-color: white; -fx-border-width: 1; -fx-text-fill: white; -fx-font-family: 'Times New Roman'; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-border-radius: 15px"); // Style the submit button
        submitButton.setPrefWidth(135); // Set the preferred width for the submit button
        submitButton.setPrefHeight(40); // Set the preferred height for the submit button
        submitButton.setTranslateY(30);

        // Create the back button
        Button backButton = new Button("Back"); // Create the back button
        backButton.setStyle("-fx-background-color: black; -fx-border-color: white; -fx-border-width: 1; -fx-text-fill: white; -fx-font-family: 'Times New Roman'; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-border-radius: 15px;"); // Style the back button
        backButton.setPrefWidth(135); // Set the preferred width for the back button
        backButton.setPrefHeight(40); // Set the preferred height for the back button
        backButton.setOnAction(e -> primaryStage.setScene(firstScene)); // Set the action for back button to switch to the first scene
        backButton.setTranslateY(30);

        // Create an HBox to hold the Submit and Back buttons side by side
        HBox buttonContainer = new HBox(20); // Create horizontal box with 20px spacing between buttons
        buttonContainer.setAlignment(Pos.CENTER); // Center align the button container
        buttonContainer.getChildren().addAll(submitButton, backButton); // Add both buttons to the container
        buttonContainer.setTranslateY(20); // Adjust vertical position of the button container

        // Add all components to the root stack pane
        VBox loginContainer = new VBox(10); // Create a vertical box with 10px spacing between elements
        loginContainer.setAlignment(Pos.CENTER); // Center align the login container
        loginContainer.getChildren().addAll(loginLabel, usernameContainer, passwordContainer, buttonContainer); // Add all elements to the container
        loginContainer.setTranslateY(60); // Adjust vertical position of the login container

        root.getChildren().add(loginContainer); // Add the login container to the root stack pane

        // Action when the submit button is pressed
        submitButton.setOnAction(e -> {
            String username = usernameField.getText(); // Get username from the text field
            String password = passwordField.getText(); // Get password from the password field

             try {
        // Authenticate the user and get the role
        String role = authenticateUser(username, password); // Check if the username and password are valid

        // If authenticated, navigate to the appropriate dashboard (Admin or Receptionist)
        if (role != null) {
            if ("Admin".equalsIgnoreCase(role)) { // If user is admin
                AdminDashboardScene adminScene = new AdminDashboardScene(firstScene); // Admin dashboard scene
                Scene adminDashboard = adminScene.createAdminDashboardScene(primaryStage); // Create the admin dashboard scene
                primaryStage.setScene(adminDashboard);  // Switch to the admin dashboard scene
            } else if ("Receptionist".equalsIgnoreCase(role)) { // If user is receptionist
                ReceptionistDashboard receptionistScene = new ReceptionistDashboard(firstScene); // Receptionist dashboard scene
                Scene receptionistDashboard = receptionistScene.createReceptionistDashboard(primaryStage); // Create receptionist dashboard scene
                primaryStage.setScene(receptionistDashboard);  // Switch to the receptionist dashboard scene
            }
        } else {
         
            // Error label for invalid credentials
            Label errorLabel = new Label("Invalid credentials. Please try again.");
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-family: 'Times New Roman'; -fx-font-size: 14px; -fx-padding: 10px;");
            errorLabel.setTranslateY(130); // Adjust the position of the error label
            root.getChildren().add(errorLabel); // Add error label to the root pane

            // Add "Forgot Password?" link under the error message
            Label forgotPasswordLabel = new Label("Forgot Password?");
            forgotPasswordLabel.setStyle("-fx-text-fill: #808080 ; -fx-font-family: 'Times New Roman'; -fx-font-size: 14px; -fx-padding: 10px;");
            forgotPasswordLabel.setTranslateY(150); // Position it below the error message
            forgotPasswordLabel.setOnMouseClicked(event -> {
                // Open the Reset Password scene
                ResetPasswordScene resetPasswordScene = new ResetPasswordScene(primaryStage, this); // Pass LoginScene for back navigation
                Scene resetPassword = resetPasswordScene.createResetPasswordScene(primaryStage); // Create the reset password scene
                primaryStage.setScene(resetPassword); // Switch to the reset password scene
            });
            root.getChildren().add(forgotPasswordLabel); // Add the "Forgot Password" label
        }
    } catch (Exception ex) {
        ex.printStackTrace();
    }
});

        // Return the scene without setting its fixed size
        Scene loginScene = new Scene(root, 800, 800, Color.BLACK); // Create the login scene with a black background
        return loginScene; // Return the created login scene
    }
}
