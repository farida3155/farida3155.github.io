package restaurant;

import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ManageMenu {

    private VBox contentArea;
    private ArrayList<String> menuCategories;

    // Constructor to initialize menu categories
    public ManageMenu() {
        menuCategories = new ArrayList<>();
        menuCategories.add("Breakfast");
        menuCategories.add("Lunch");
        menuCategories.add("Dinner");
        menuCategories.add("Drinks");
        menuCategories.add("Appetizer");
    }

    // Main layout
    private BorderPane createMainLayout() {
        BorderPane mainLayout = new BorderPane();

        // Sidebar with buttons
        VBox sidebar = new VBox(20);
        sidebar.setStyle("-fx-background-color: #2F4F4F; -fx-padding: 20px;");
        sidebar.setAlignment(Pos.TOP_CENTER);

        Button addButton = new Button("Add Item");
        Button editButton = new Button("Edit Menu");
        Button removeButton = new Button("Remove Item");

        styleButton(addButton);
        styleButton(editButton);
        styleButton(removeButton);

        addButton.setOnAction(e -> contentArea.getChildren().setAll(getAddMenuForm()));
        editButton.setOnAction(e -> contentArea.getChildren().setAll(getEditMenuForm()));
        removeButton.setOnAction(e -> contentArea.getChildren().setAll(getRemoveMenuForm()));

        sidebar.getChildren().addAll(addButton, editButton, removeButton);

        // Content area for dynamic forms
        contentArea = new VBox(20);
        contentArea.setAlignment(Pos.CENTER);

        mainLayout.setLeft(sidebar);
        mainLayout.setCenter(contentArea);

        return mainLayout;
    }

    // Returns the Add Menu form as a VBox
    public VBox getAddMenuForm() {
        VBox addForm = new VBox(20);
        addForm.setAlignment(Pos.CENTER);

        Label title = new Label("Add Item");
        title.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-weight: bold;");

        TextField nameField = new TextField();
        nameField.setPromptText("Item Name");

       ComboBox<String> descriptionCombo = new ComboBox<>();
descriptionCombo.getItems().addAll(menuCategories);
descriptionCombo.setPromptText("Select Category");
descriptionCombo.setStyle("-fx-font-size: 14px; -fx-font-family: Arial; -fx-background-color: #FFFFFF; -fx-prompt-text-fill: #888888;");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        Button addButton = new Button("Add Item");
        styleButton(addButton);

        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-pref-width: 60px;");
        closeButton.setOnAction(e -> addForm.getChildren().clear());
// Add item action
    addButton.setOnAction(e -> {
        // Assuming you are adding the item to your menu list here
        String itemName = nameField.getText();
        String category = descriptionCombo.getValue();
        String price = priceField.getText();
        
        // Code to add item to the menu
        // For example:
        // menu.add(new MenuItem(itemName, category, price));

        showAlert("Item Added", "The item has been added successfully!");

        // Optionally clear the fields after adding the item
        nameField.clear();
        priceField.clear();
        descriptionCombo.setValue(null);
    });
        // Create HBox to arrange buttons horizontally
        HBox buttonBox = new HBox(10); // 10 is the spacing between buttons
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(addButton, closeButton);

        // Add all elements, including only the HBox for buttons
        addForm.getChildren().addAll(title, nameField,  priceField, descriptionCombo, buttonBox);
        return addForm;
    }

    // Returns the Edit Menu form as a VBox
    public VBox getEditMenuForm() {
        VBox editForm = new VBox(20);
        editForm.setAlignment(Pos.CENTER);

        Label title = new Label("Edit Item");
        title.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-weight: bold;");

        TextField nameField = new TextField();
        nameField.setPromptText("Item Name");

        ComboBox<String> descriptionCombo = new ComboBox<>();
        descriptionCombo.getItems().addAll(menuCategories);
        descriptionCombo.setPromptText("Select Category");
descriptionCombo.setStyle("-fx-font-size: 14px; -fx-font-family: Arial; -fx-background-color: #FFFFFF; -fx-prompt-text-fill: #888888;");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        Button editButton = new Button("Edit Item");
        styleButton(editButton);

        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-pref-width: 60px;");
        closeButton.setOnAction(e -> editForm.getChildren().clear());
// Edit item action
    editButton.setOnAction(e -> {
        // Assuming you are editing the item in your menu list here
        String itemName = nameField.getText();
        String category = descriptionCombo.getValue();
        String price = priceField.getText();

        // Code to update item in the menu
        // For example:
        // menu.edit(new MenuItem(itemName, category, price));

        showAlert("Item Edited", "The item has been edited successfully!");

        // Optionally clear the fields after editing the item
        nameField.clear();
        priceField.clear();
        descriptionCombo.setValue(null);
    });
        // Create HBox to arrange buttons horizontally
        HBox buttonBox = new HBox(10); // 10 is the spacing between buttons
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(editButton, closeButton);

        // Add all elements, including only the HBox for buttons
        editForm.getChildren().addAll(title, nameField, priceField, descriptionCombo,buttonBox);
        return editForm;
    }

    // Returns the Remove Menu form as a VBox
    public VBox getRemoveMenuForm() {
        VBox removeForm = new VBox(20);
        removeForm.setAlignment(Pos.CENTER);

        Label title = new Label("Remove Item");
        title.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-weight: bold;");

        TextField nameField = new TextField();
        nameField.setPromptText("Item Name");

        ComboBox<String> descriptionCombo = new ComboBox<>();
        descriptionCombo.getItems().addAll(menuCategories);
        descriptionCombo.setPromptText("Select Category");
descriptionCombo.setStyle("-fx-font-size: 14px; -fx-font-family: Arial; -fx-background-color: #FFFFFF; -fx-prompt-text-fill: #888888;");

        Button removeButton = new Button("Remove Item");
        styleButton(removeButton);

        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-pref-width: 60px;");
        closeButton.setOnAction(e -> removeForm.getChildren().clear());

         removeButton.setOnAction(e -> {
        // Assuming you are removing the item from your menu list here
        String itemName = nameField.getText();
        String category = descriptionCombo.getValue();

        // Code to remove item from the menu
        // For example:
        // menu.remove(new MenuItem(itemName, category));

        showAlert("Item Removed", "The item has been removed successfully!");

        // Optionally clear the fields after removing the item
        nameField.clear();
        descriptionCombo.setValue(null);
    });
        // Create HBox to arrange buttons horizontally
        HBox buttonBox = new HBox(10); // 10 is the spacing between buttons
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(removeButton, closeButton);

        // Add all elements, including only the HBox for buttons
        removeForm.getChildren().addAll(title, nameField, descriptionCombo, buttonBox);
        return removeForm;
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

    // Close the current form
    private void closeForm() {
        contentArea.getChildren().clear();
    }

    // Create and display the main window
    public void showMenuManagementWindow(Stage primaryStage) {
        BorderPane mainLayout = createMainLayout();
        Scene scene = new Scene(mainLayout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Manage Menu");
        primaryStage.show();
    }
}
