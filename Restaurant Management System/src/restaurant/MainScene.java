package restaurant;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainScene extends Application {

    private Scene firstScene;  // Declare a variable to hold the first scene
    
    @Override
    public void start(Stage primaryStage) {
        StackPane root1 = new StackPane();  // Create the root layout for the first scene (StackPane)

        // Reuse the shared background setup method
        BackgroundUtils.setupBackground(root1, 0, -80, 600, 600);  // Set up background for the root layout (using utility method)

        // Create buttons for login and sign in
        Button loginButton = new Button("Log In");  // Create a Login button
        loginButton.setStyle("-fx-background-color: black; -fx-border-color: white; -fx-border-width: 1; -fx-text-fill: white; -fx-font-family: 'Times New Roman'; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-border-radius: 15px;");  // Style the Login button

        Button signInButton = new Button("Sign Up");  // Create a Sign In button
        signInButton.setStyle("-fx-background-color: black; -fx-border-color: white; -fx-border-width: 1; -fx-text-fill: white; -fx-font-family: 'Times New Roman'; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-border-radius: 15px;");  // Style the Sign In button

        // Create a label asking if the user is a guest
        Label guestLabel = new Label("Are you a new guest? Sign Up.");  // Display a message for guests
        guestLabel.setStyle("-fx-text-fill: grey; -fx-font-family: 'Times New Roman'; -fx-font-size: 13px;");  // Style the guest label

        // Create a VBox layout to hold the buttons and the label vertically
        VBox buttonContainer = new VBox(10);  // VBox with 10px spacing between elements
        buttonContainer.setAlignment(Pos.CENTER);  // Align the buttons and label in the center of the VBox
        buttonContainer.getChildren().addAll(loginButton, signInButton, guestLabel);  // Add buttons and label to the VBox
        buttonContainer.setTranslateY(120);  // Adjust vertical position of the VBox

        // Add the button container (VBox) to the root layout (StackPane)
        root1.getChildren().add(buttonContainer);

        // Create the first scene with the root layout
        firstScene = new Scene(root1, 800, 800);  // Scene with the specified root layout and size (600x400)

        // Create the LoginScene instance to transition to another scene
        LoginScene loginScene = new LoginScene();  // Instantiate the LoginScene class
        SignUpScene signUpScene = new SignUpScene();  // Instantiate the SignUpScene class

        // Handle login button action
        loginButton.setOnAction(e -> {  // Set an action handler for the login button
            Scene loginSceneInstance = loginScene.LoginScene(primaryStage, firstScene);  // Call the LoginScene method to get the login scene
            primaryStage.setScene(loginSceneInstance);  // Change the primaryStage scene to the login scene
        });

        // Handle sign in button action
        signInButton.setOnAction(e -> {
            Scene signUpSceneInstance = signUpScene.createSignUpScene(primaryStage, firstScene);
            primaryStage.setScene(signUpSceneInstance);
        });

        // Configure the primary stage for the application
        primaryStage.setTitle("Main Scene");  // Set the title of the main application window
        primaryStage.setScene(firstScene);  // Set the primary stage's scene to the first scene
        primaryStage.show();  // Display the primary stage

    }

    public static void main(String[] args) {
        launch(args);  // Launch the application
    }
}