package restaurant;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static javafx.application.Application.launch;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class GuestDashboardScene {
    
    
    private Scene firstScene; // Store the first scene

    // Constructor to accept the firstScene
    public GuestDashboardScene(Scene firstScene) {
        this.firstScene = firstScene;
    }

    
    private final int MAX_STARS = 5;
    private final ImageView[] stars = new ImageView[MAX_STARS];
    private Image emptyStar;
    private Image filledStar;
    private final int STAR_SIZE = 50; // Set the star size
    private int currentRating = 0; // Track the current rating
    private String userPhoneNumber; // Store the user's phone number
    private Scene previousScene; // Store the previous scene (main scene)
    private Menu menu; // Store the menu object

    // No-argument constructor
    public GuestDashboardScene() {
        // Initialize with default values or handle no-argument initialization
    }

    // Constructor to initialize the phone number and previous scene
    public GuestDashboardScene(String phoneNumber, Scene previousScene) {
        this.userPhoneNumber = phoneNumber;
        this.previousScene = previousScene;
    }

   
   public Scene createGuestDashboardScene(Stage primaryStage){
        menu = new Menu(); // Initialize the menu
        menu.initializeFiles(); // Initialize menu files

        BorderPane root = new BorderPane();

        // Initialize the images
        emptyStar = loadResourceImage("/restaurant/blankstar.png");
        filledStar = loadResourceImage("/restaurant/fullstar.png");

        VBox mainSidebar = createGuestSidebar(primaryStage, root);
        root.setLeft(mainSidebar);

        VBox mainContent = new VBox(20);
        mainContent.setAlignment(Pos.CENTER);
        mainContent.setStyle("-fx-background-color: #333; -fx-text-fill: white;");
        Label placeholderLabel = new Label("Select an option from the sidebar");
        placeholderLabel.setStyle("-fx-text-fill: white;");
        mainContent.getChildren().add(placeholderLabel);
        root.setCenter(mainContent);

        Scene scene = new Scene(root, 800, 800);
        primaryStage.setScene(scene);

        // Add key event handler for backspace key
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.BACK_SPACE) {
                displayMenu(root);
            }
        });

     return new Scene(root, 800, 800, Color.BLACK); // Return a new Scene with black background and size 800x800  
    }

    private VBox createGuestSidebar(Stage primaryStage, BorderPane root) {
        VBox sidebar = new VBox(20);
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setStyle("-fx-background-color: #333;");
        sidebar.setPrefWidth(200);

        Label dashboardTitle = new Label("Guest Dashboard");
        dashboardTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10px;");

        Button viewMenuButton = createGuestSidebarButton("View Menu", primaryStage, root);
        Button viewReservationsButton = createGuestSidebarButton("View Reservations", primaryStage, root);
        Button ratingButton = createGuestSidebarButton("Rate Us", primaryStage, root); 
        ratingButton.setStyle("-fx-background-color: #FFDB58; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px; -fx-pref-width: 180px;");
        Button contactButton = createGuestSidebarButton("Contact Us", primaryStage, root);
        contactButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px; -fx-pref-width: 180px;");
        Button logoutButton = createGuestSidebarButton("Log Out", primaryStage, root);
        logoutButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px; -fx-pref-width: 180px;");

        sidebar.getChildren().addAll(
                dashboardTitle,
                viewMenuButton,
                viewReservationsButton,
                ratingButton,
                contactButton,
                logoutButton
        );

        return sidebar;
    }

    private Button createGuestSidebarButton(String buttonText, Stage primaryStage, BorderPane root) {
        Button button = new Button(buttonText);
        button.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px; -fx-pref-width: 180px;");

        button.setOnAction(e -> {
            if (buttonText.equals("View Menu")) {
                displayMenu(root);
            } else if (buttonText.equals("View Reservations")) {
                viewReservations(root, userPhoneNumber);
            } else if (buttonText.equals("Rate Us")) {
                displayRatingForm(root);
            } else if (buttonText.equals("Contact Us")) {
                displayContactForm(root);
            } else if (buttonText.equals("Log Out")) {
                showLogoutConfirmation(primaryStage);
            }
        });

        return button;
    }

    private void showLogoutConfirmation(Stage primaryStage) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Log Out");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to log out?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed logout
            primaryStage.setScene(new LoginScene().LoginScene(primaryStage, previousScene));
        }
        // If the user selects CANCEL or closes the dialog, do nothing
    }

    
 private VBox createCategoryButtons(BorderPane root) {
    VBox categoryButtons = new VBox(20);
    categoryButtons.setAlignment(Pos.CENTER);
    categoryButtons.setPadding(new Insets(20));
    categoryButtons.setStyle("-fx-background-color: #333;");

    // Initialize buttons for each category with images and labels
    VBox breakfastButton = createBreakfastButton(root);
    VBox lunchButton = createLunchButton(root);
    VBox dinnerButton = createDinnerButton(root);
    VBox drinksButton = createDrinksButton(root);
    VBox appetizersButton = createAppetizersButton(root);

    // Create HBoxes to display categories two per line
    HBox firstRow = new HBox(20, breakfastButton, lunchButton);
    firstRow.setAlignment(Pos.CENTER);

    HBox secondRow = new HBox(20, dinnerButton, drinksButton);
    secondRow.setAlignment(Pos.CENTER);

    // Center the last category underneath
    HBox thirdRow = new HBox(20, appetizersButton);
    thirdRow.setAlignment(Pos.CENTER);

    // Add all HBoxes to the VBox
    categoryButtons.getChildren().addAll(firstRow, secondRow, thirdRow);

    return categoryButtons;
}

private VBox createBreakfastButton(BorderPane root) {
    return createImageCategoryButton("Breakfast", "/restaurant/breakfast.png", menu.getBreakfast(), root);
}

private VBox createLunchButton(BorderPane root) {
    return createImageCategoryButton("Lunch", "/restaurant/lunch.png", menu.getLunch(), root);
}

private VBox createDinnerButton(BorderPane root) {
    return createImageCategoryButton("Dinner", "/restaurant/dinner.png", menu.getDinner(), root);
}

private VBox createDrinksButton(BorderPane root) {
    return createImageCategoryButton("Drinks", "/restaurant/drinks.png", menu.getDrinks(), root);
}

private VBox createAppetizersButton(BorderPane root) {
    return createImageCategoryButton("Appetizers", "/restaurant/appetizers.png", menu.getAppetizers(), root);
}

private VBox createImageCategoryButton(String categoryName, String imageName, ArrayList<Meal> meals, BorderPane root) {
    ImageView imageView = null;
    try {
        imageView = new ImageView(loadResourceImage(imageName));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
    } catch (Exception e) {
        System.err.println("Image not found: " + imageName + ". " + e.getMessage());
        imageView = new ImageView();
    }

    Label label = new Label(categoryName);
    label.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

    VBox vbox = new VBox(5, imageView, label);
    vbox.setAlignment(Pos.CENTER);
    vbox.setOnMouseClicked(e -> displayMeals(meals, categoryName, root));

    return vbox;
}


private void displayMeals(ArrayList<Meal> meals, String categoryName, BorderPane root) {
    VBox mealContainer = new VBox(10);
    mealContainer.setPadding(new Insets(10));
    mealContainer.setAlignment(Pos.CENTER);
    mealContainer.setStyle("-fx-background-color: #333; -fx-text-fill: white;");

    Label categoryLabel = new Label(categoryName);
    categoryLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

    VBox mealBox = new VBox(10); // Use VBox for vertical list
    mealBox.setPadding(new Insets(10));
    mealBox.setAlignment(Pos.CENTER);

    for (Meal meal : meals) {
        Label mealLabel = new Label(meal.getName() + " - $" + meal.getPrice());
        mealLabel.setStyle("-fx-font-size: 16px; -fx-background-color: #555; -fx-text-fill: white; -fx-padding: 5px;");
        mealBox.getChildren().add(mealLabel);
    }

    Button backButton = new Button("Back");
    backButton.setOnAction(e -> displayMenu(root));
    backButton.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-font-size: 16px;");

    mealContainer.getChildren().addAll(categoryLabel, mealBox, backButton);

    // Add fade transition for a smooth display
    FadeTransition fadeIn = new FadeTransition(Duration.millis(500), mealContainer);
    fadeIn.setFromValue(0);
    fadeIn.setToValue(1);
    fadeIn.play();

    root.setCenter(mealContainer);
}

private void displayMenu(BorderPane root) {
    VBox menuContainer = new VBox(20);
    menuContainer.setAlignment(Pos.TOP_CENTER);
    menuContainer.setStyle("-fx-background-color: #333; -fx-padding: 20px;");
    menuContainer.setPrefWidth(800); // Adjust the width to accommodate the HBox

    Label menuTitle = new Label("Menu");
    menuTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

    // Create category buttons horizontally
    VBox categoryButtons = createCategoryButtons(root);

    menuContainer.getChildren().addAll(menuTitle, categoryButtons);
    root.setCenter(menuContainer);
}
    private void viewReservations(BorderPane root, String phone) {
        Receptionist receptionist = new Receptionist();
        receptionist.loadReservations(phone);
        List<Reservation> guestReservations = receptionist.getReservations();
        displayReservations(root, guestReservations);
    }

   private void displayReservations(BorderPane root, List<Reservation> reservations) {
    VBox reservationsContainer = new VBox(20);
    reservationsContainer.setAlignment(Pos.TOP_CENTER);
    reservationsContainer.setStyle("-fx-background-color: #333; -fx-padding: 20px;");
    reservationsContainer.setPrefWidth(400);

    Label reservationsTitle = new Label("Reservations");
    reservationsTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
    reservationsContainer.getChildren().add(reservationsTitle);

    if (reservations.isEmpty()) {
        VBox noReservationsContainer = new VBox();
        noReservationsContainer.setAlignment(Pos.CENTER);
        noReservationsContainer.setPrefHeight(200); // Adjust height as needed
        noReservationsContainer.setStyle("-fx-background-color: #333;");

        Label noReservationsLabel = new Label("No reservations yet!\nCall our hotline to make a reservation now ;)");
        noReservationsLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
        noReservationsLabel.setAlignment(Pos.CENTER);
        
        noReservationsContainer.getChildren().add(noReservationsLabel);
        reservationsContainer.getChildren().add(noReservationsContainer);
    } else {
        for (Reservation reservation : reservations) {
            VBox reservationBox = new VBox(10);
            reservationBox.setStyle("-fx-background-color: #555; -fx-padding: 10px; -fx-border-color: #ccc; -fx-border-width: 1px;");

            Label reservationDetail = new Label(
                "Reservation ID: " + reservation.getResID() + "\n" +
                "Guest Name: " + reservation.getGuestName() + "\n" +
                "Phone: " + reservation.getPhone() + "\n" +
                "Date and Time: " + reservation.getDateTime() + "\n" +
                "Table ID: " + reservation.getTableID()
            );
            reservationDetail.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

            Label orderedMealsTitle = new Label("Ordered Meals:");
            orderedMealsTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");

            VBox mealsBox = new VBox(5);
            for (Meal meal : reservation.getOrderedMeals()) {
                Label mealDetail = new Label(
                    "  Meal Name: " + meal.getName() + "\n" +
                    "  Category: " + meal.getCategory() + "\n" +
                    "  Price: $" + meal.getPrice()
                );
                mealDetail.setStyle("-fx-font-size: 12px; -fx-text-fill: white;");
                mealsBox.getChildren().add(mealDetail);
            }

            reservationBox.getChildren().addAll(reservationDetail, orderedMealsTitle, mealsBox);
            reservationsContainer.getChildren().add(reservationBox);
        }
    }

    root.setCenter(reservationsContainer);
}

  private void displayRatingForm(BorderPane root) {
    // Set the background color of the entire pane to black
    root.setStyle("-fx-background-color: black;");

    HBox ratingContainer = new HBox(10);
    ratingContainer.setAlignment(Pos.CENTER);
    ratingContainer.setStyle("-fx-background-color: black; -fx-padding: 20px;");

    Label ratingTitle = new Label("RATING");
    ratingTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

    VBox ratingBox = new VBox(10, ratingTitle, ratingContainer);
    ratingBox.setAlignment(Pos.CENTER);
    ratingBox.setStyle("-fx-background-color: black;"); // Ensure the VBox background is also black

    // Initialize star ImageViews with fixed size
    for (int i = 0; i < MAX_STARS; i++) {
        stars[i] = new ImageView(emptyStar);
        stars[i].setFitWidth(STAR_SIZE);
        stars[i].setFitHeight(STAR_SIZE);
        int starIndex = i;

        // Handle mouse click to set rating
        stars[i].setOnMouseClicked(event -> {
            if (starIndex == 0 && currentRating == 1) {
                resetRating();
            } else {
                setRating(starIndex + 1);
            }
        });

        // Handle mouse enter to preview rating
        stars[i].setOnMouseEntered(event -> previewRating(starIndex + 1));

        // Handle mouse exit to reset preview
        stars[i].setOnMouseExited(event -> resetPreviewRating());

        ratingContainer.getChildren().add(stars[i]);
    }

    root.setCenter(ratingBox);
}
    private void displayContactForm(BorderPane root) {
        VBox contactContainer = new VBox(20);
        contactContainer.setAlignment(Pos.CENTER);
        contactContainer.setStyle("-fx-background-color: #333; -fx-padding: 20px;");
        contactContainer.setPrefWidth(400);

        Label contactTitle = new Label("Contact Us");
        contactTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");

        // Contact information placeholder
        Label contactInfoLabel = new Label("Hotline: 19991\nEmail: jazel.co@gmail.com\nInstagram: @jazeleg\n\nBranches:\nGarden 8, New Cairo\nDegla, Maadi\nAl Hay Al Thamen, Nasr City");
        contactInfoLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

        // Complaint form
        Label complaintsLabel = new Label("For complaints:");
        complaintsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label emailLabel = new Label("Your Email:");
        emailLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Times New Roman'; -fx-font-size: 16px;");
        
        TextField emailField = new TextField();
        emailField.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-family: 'Times New Roman'; -fx-font-size: 16px; -fx-padding: 10px;");
        emailField.setMaxWidth(200);

        Label messageLabel = new Label("Your Message:");
        messageLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Times New Roman'; -fx-font-size: 16px;");
        
        TextArea messageArea = new TextArea();
        messageArea.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-font-family: 'Times New Roman'; -fx-font-size: 16px; -fx-padding: 10px;");
        messageArea.setMaxWidth(200);
        messageArea.setPrefHeight(100);

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px;");
        submitButton.setOnAction(e -> {
            String email = emailField.getText();
            String message = messageArea.getText();
            if (!email.isEmpty() && !message.isEmpty()) {
                // Handle the submission of the complaint (e.g., save to a file or send an email)
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Complaint Submitted");
                alert.setHeaderText(null);
                alert.setContentText("Thank you for your feedback!");
                alert.showAndWait();
                emailField.clear();
                messageArea.clear();
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Both fields are required!");
                alert.showAndWait();
            }
        });

        contactContainer.getChildren().addAll(contactTitle, contactInfoLabel, complaintsLabel, emailLabel, emailField, messageLabel, messageArea, submitButton);
        root.setCenter(contactContainer);
    }

 
private void previewRating(int rating) {
    // Update star images based on the preview rating
    for (int i = 0; i < MAX_STARS; i++) {
        if (i < rating) {
            stars[i].setImage(filledStar);
        } else {
            stars[i].setImage(emptyStar);
        }
    }
}

private void resetPreviewRating() {
    // Reset stars to their state before preview
    for (int i = 0; i < MAX_STARS; i++) {
        if (i < currentRating) {
            stars[i].setImage(filledStar);
        } else {
            stars[i].setImage(emptyStar);
        }
    }
}

private void resetRating() {
    // Reset all stars to empty
    for (int i = 0; i < MAX_STARS; i++) {
        stars[i].setImage(emptyStar);
    }
    currentRating = 0;
}

public void setRating(int rating) {
    currentRating = rating;
    // Update star images based on the rating
    for (int i = 0; i < MAX_STARS; i++) {
        if (i < rating) {
            stars[i].setImage(filledStar);
        } else {
            stars[i].setImage(emptyStar);
        }
    }

    // Display rating alert
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Rating");
    alert.setHeaderText(null);
    if (rating == 4 || rating == 5) {
        alert.setContentText("Thank you for your rating of " + rating + " stars!\n Feel free to let us know of any improvements we could do :)");
    } else {
        alert.setContentText("Thank you for your rating of " + rating + " stars!\n We're sorry this was your experience. Please let us know how we can improve our services :)");
    }
    alert.showAndWait();
}

    private Image loadResourceImage(String path) {
        InputStream inputStream = getClass().getResourceAsStream(path);
        if (inputStream == null) {
            throw new IllegalArgumentException("Image resource not found: " + path);
        }
        return new Image(inputStream);
    }
}