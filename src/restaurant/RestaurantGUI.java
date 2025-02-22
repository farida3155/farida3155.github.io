package restaurant;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class RestaurantGUI extends Application {
     public static Boolean authenticateUser(String username, String password) throws IOException, ClassNotFoundException {
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

    @Override
    public void start(Stage primaryStage) {
        // Initial Role Selection Scene
        VBox roleSelectionPane = new VBox(10);
        roleSelectionPane.setPadding(new Insets(10));

        Label roleLabel = new Label("Select Role:");
        Button adminButton = new Button("Admin");
        Button guestButton = new Button("Guest");
        Button receptionistButton = new Button("Receptionist");

        roleSelectionPane.getChildren().addAll(roleLabel, adminButton, guestButton, receptionistButton);
        Scene roleSelectionScene = new Scene(roleSelectionPane, 300, 200);

        // Login Scene
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Label loginStatus = new Label();

        GridPane loginPane = new GridPane();
        loginPane.setPadding(new Insets(10));
        loginPane.setVgap(10);
        loginPane.setHgap(10);
        loginPane.add(usernameLabel, 0, 0);
        loginPane.add(usernameField, 1, 0);
        loginPane.add(passwordLabel, 0, 1);
        loginPane.add(passwordField, 1, 1);
        loginPane.add(loginButton, 1, 2);
        loginPane.add(loginStatus, 1, 3);

        Scene loginScene = new Scene(loginPane, 300, 200);

        // Admin Dashboard
        TabPane adminTabs = new TabPane();

        // Manage Tables Tab
        Tab manageTablesTab = new Tab("Manage Tables");
        VBox manageTablesPane = new VBox(10);
        manageTablesPane.setPadding(new Insets(10));
        manageTablesPane.getChildren().add(new Label("Table management functionality here."));
        manageTablesTab.setContent(manageTablesPane);

        // Manage Menus Tab
        Tab manageMenusTab = new Tab("Manage Menus");
        VBox manageMenusPane = new VBox(10);
        manageMenusPane.setPadding(new Insets(10));
        manageMenusPane.getChildren().add(new Label("Menu management functionality here."));
        manageMenusTab.setContent(manageMenusPane);

        // Manage Users Tab
        Tab manageUsersTab = new Tab("Manage Users");
        VBox manageUsersPane = new VBox(10);
        manageUsersPane.setPadding(new Insets(10));
        manageUsersPane.getChildren().add(new Label("User management functionality here."));
        manageUsersTab.setContent(manageUsersPane);

        // View Reports Tab
        Tab viewReportsTab = new Tab("View Reports");
        VBox viewReportsPane = new VBox(10);
        viewReportsPane.setPadding(new Insets(10));
        viewReportsPane.getChildren().add(new Label("Reports viewing functionality here."));
        viewReportsTab.setContent(viewReportsPane);

        // View Data Tab
        Tab viewDataTab = new Tab("View Data");
        VBox viewDataPane = new VBox(10);
        viewDataPane.setPadding(new Insets(10));
        viewDataPane.getChildren().add(new Label("Data viewing functionality here."));
        viewDataTab.setContent(viewDataPane);

        adminTabs.getTabs().addAll(manageTablesTab, manageMenusTab, manageUsersTab, viewReportsTab, viewDataTab);
        adminTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Scene adminScene = new Scene(adminTabs, 600, 400);

        // Guest Dashboard
        TabPane guestTabs = new TabPane();

        // Menu Tab
        Tab menuTab = new Tab("Menu");
        VBox menuPane = new VBox(10);
        menuPane.setPadding(new Insets(10));
        menuPane.getChildren().add(new Label("Menu display functionality here."));
        menuTab.setContent(menuPane);

        // Reservations Tab
        Tab reservationsTab = new Tab("Reservations");
        VBox reservationsPane = new VBox(10);
        reservationsPane.setPadding(new Insets(10));
        reservationsPane.getChildren().add(new Label("Reservation functionality here."));
        reservationsTab.setContent(reservationsPane);

        // Feedback Tab
        Tab feedbackTab = new Tab("Feedback");
        VBox feedbackPane = new VBox(10);
        feedbackPane.setPadding(new Insets(10));
        feedbackPane.getChildren().add(new Label("Feedback functionality here."));
        feedbackTab.setContent(feedbackPane);

        guestTabs.getTabs().addAll(menuTab, reservationsTab, feedbackTab);
        guestTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Scene guestScene = new Scene(guestTabs, 600, 400);

        // Receptionist Dashboard
        TabPane receptionistTabs = new TabPane();

        // Manage Reservations Tab
        Tab manageReservationsTab = new Tab("Manage Reservations");
        VBox manageReservationsPane = new VBox(10);
        manageReservationsPane.setPadding(new Insets(10));
        manageReservationsPane.getChildren().add(new Label("Reservation management functionality here."));
        manageReservationsTab.setContent(manageReservationsPane);

        // Add Meals Tab
        Tab addMealsTab = new Tab("Add Meals");
        VBox addMealsPane = new VBox(10);
        addMealsPane.setPadding(new Insets(10));
        addMealsPane.getChildren().add(new Label("Meal addition functionality here."));
        addMealsTab.setContent(addMealsPane);

        // View Reports Tab
        Tab receptionistViewReportsTab = new Tab("View Reports");
        VBox receptionistViewReportsPane = new VBox(10);
        receptionistViewReportsPane.setPadding(new Insets(10));
        receptionistViewReportsPane.getChildren().add(new Label("Reports viewing functionality here."));
        receptionistViewReportsTab.setContent(receptionistViewReportsPane);

        receptionistTabs.getTabs().addAll(manageReservationsTab, addMealsTab, receptionistViewReportsTab);
        receptionistTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Scene receptionistScene = new Scene(receptionistTabs, 600, 400);

        // Event Handlers for Role Selection
        adminButton.setOnAction(e -> primaryStage.setScene(loginScene));
        guestButton.setOnAction(e -> {
            usernameField.setText("guest"); // Pre-fill for Guest
            primaryStage.setScene(loginScene);
        });
        receptionistButton.setOnAction(e -> {
            usernameField.setText("receptionist"); // Pre-fill for Receptionist
            primaryStage.setScene(loginScene);
        });

        // Login Button Action
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            try {
                // Call authenticateUser from Restaurant class
                if (Restaurant.authenticateUser(username, password)) {
                    if (username.equals("guest")) {
                        primaryStage.setScene(guestScene);
                        primaryStage.setTitle("Guest Dashboard");
                    } else if (username.equals("receptionist")) {
                        primaryStage.setScene(receptionistScene);
                        primaryStage.setTitle("Receptionist Dashboard");
                    } else {
                        primaryStage.setScene(adminScene);
                        primaryStage.setTitle("Admin Dashboard");
                    }
                } else {
                    loginStatus.setText("Invalid username or password.");
                }
            } catch (Exception ex) {
                loginStatus.setText("Error: " + ex.getMessage());
            }
        });

        // Set up the stage
        primaryStage.setTitle("Restaurant Management System");
        primaryStage.setScene(roleSelectionScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
