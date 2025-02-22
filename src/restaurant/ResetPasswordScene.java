package restaurant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

public class ResetPasswordScene {

    private Stage primaryStage;
    private LoginScene loginScene;
    private Scene firstScene; // Store the first scene

    public ResetPasswordScene(Stage primaryStage, LoginScene loginScene) {
        this.primaryStage = primaryStage;
        this.loginScene = loginScene;
    }

    public Scene createResetPasswordScene(Stage primaryStage) {
        StackPane root = new StackPane();
        BackgroundUtils.setupBackground(root, 0, -200, 600, 600);

        // Title Label
        Label resetPasswordLabel = new Label("Reset Password");
        resetPasswordLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Times New Roman'; -fx-font-size: 24px; -fx-padding: 10px;");
        resetPasswordLabel.setTranslateY(60); // Adjust Y position for alignment
        resetPasswordLabel.setTranslateX(15);
        
        // Username Label and Field
        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Times New Roman'; -fx-font-size: 16px;");
        TextField usernameField = new TextField();
        usernameField.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-family: 'Times New Roman'; -fx-font-size: 16px; -fx-padding: 10px;");
        usernameField.setMaxWidth(200);

        HBox usernameContainer = new HBox(10);
        usernameContainer.setAlignment(Pos.CENTER);
        usernameContainer.getChildren().addAll(usernameLabel, usernameField);
        usernameContainer.setTranslateY(60); // Adjust Y position for alignment
        usernameContainer.setTranslateX(-15);
        // Password Label and Field
        Label newPasswordLabel = new Label("New Password:");
        newPasswordLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Times New Roman'; -fx-font-size: 16px;");
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-family: 'Times New Roman'; -fx-font-size: 16px; -fx-padding: 10px;");
        newPasswordField.setMaxWidth(200);

        HBox passwordContainer = new HBox(10);
        passwordContainer.setAlignment(Pos.CENTER);
        passwordContainer.getChildren().addAll(newPasswordLabel, newPasswordField);
        passwordContainer.setTranslateY(70); // Align with username container
        passwordContainer.setTranslateX(-30);
        
        // Reset Button
        Button resetButton = new Button("Reset Password");
        resetButton.setStyle("-fx-background-color: black; -fx-border-color: white; -fx-border-width: 1; -fx-text-fill: white; -fx-font-family: 'Times New Roman'; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-border-radius: 15px;");
        resetButton.setPrefWidth(200);
        resetButton.setPrefHeight(40);
        resetButton.setTranslateY(100); // Adjust Y position for alignment
        resetButton.setTranslateX(15);
        
        resetButton.setOnAction(e -> {
            String username = usernameField.getText();
            String newPassword = newPasswordField.getText();

            try {
                // Reset password logic
                resetPassword(username, newPassword);

                // After success, return to the Login scene
                primaryStage.setScene(loginScene.LoginScene(primaryStage, firstScene));
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
                Label errorLabel = new Label("Error resetting password. Please try again.");
                errorLabel.setStyle("-fx-text-fill: red; -fx-font-family: 'Times New Roman'; -fx-font-size: 14px; -fx-padding: 10px;");
                errorLabel.setTranslateY(150);
                root.getChildren().add(errorLabel);
            }
        });

        // VBox Container for Alignment
        VBox resetContainer = new VBox(20);
        resetContainer.setAlignment(Pos.CENTER); // Center-align all elements vertically
        resetContainer.getChildren().addAll(resetPasswordLabel, usernameContainer, passwordContainer, resetButton);

        root.getChildren().add(resetContainer);

        return new Scene(root, 800, 800, Color.BLACK);
    }

    private void resetPassword(String username, String newPassword) throws IOException, ClassNotFoundException {
        File userFile = new File("Users.dat");
        if (!userFile.exists()) {
            throw new IOException("User file not found.");
        }

        ArrayList<User> users = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userFile))) {
            users = (ArrayList<User>) ois.readObject();
        }

        boolean userFound = false;
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                user.setPassword(newPassword);
                userFound = true;
                break;
            }
        }

        if (!userFound) {
            throw new IOException("User not found.");
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(userFile))) {
            oos.writeObject(users);
        }
    }
}
