package restaurant;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ManageTables {

    private VBox contentArea;

    // Main layout
    private BorderPane createMainLayout() {
        BorderPane mainLayout = new BorderPane();

        // Sidebar with buttons
        VBox sidebar = new VBox(20);
        sidebar.setStyle("-fx-background-color: #2F4F4F; -fx-padding: 20px;");
        sidebar.setAlignment(Pos.TOP_CENTER);

        Button addButton = new Button("Add Table");
        Button editButton = new Button("Edit Table");
        Button removeButton = new Button("Remove Table");

        styleButton(addButton);
        styleButton(editButton);
        styleButton(removeButton);

        addButton.setOnAction(e -> getAddTableForm());
        editButton.setOnAction(e -> getEditTableForm());
        removeButton.setOnAction(e -> getRemoveTableForm());

        sidebar.getChildren().addAll(addButton, editButton, removeButton);

        // Content area for dynamic forms
        contentArea = new VBox(20);
        contentArea.setAlignment(Pos.CENTER);

        mainLayout.setLeft(sidebar);
        mainLayout.setCenter(contentArea);

        return mainLayout;
    }

    public VBox getAddTableForm() {
        VBox addForm = new VBox(20);
        addForm.setAlignment(Pos.CENTER);

        Label title = new Label("Add Table");
        title.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-weight: bold;");

        TextField tableIdField = new TextField();
        tableIdField.setPromptText("Table ID");

        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");

        TextField capacityField = new TextField();
        capacityField.setPromptText("Capacity");

        CheckBox reservedCheckbox = new CheckBox("Reserved");
        reservedCheckbox.setStyle("-fx-font-size: 16px; -fx-font-family: Arial; -fx-text-fill: #333333; -fx-padding: 5px;");
        reservedCheckbox.setScaleX(1.0);
        reservedCheckbox.setScaleY(1.0);

        Button addButton = new Button("Add Table");
        styleButton(addButton);
        addButton.setOnAction(e -> {
            String tableId = tableIdField.getText();
            String category = categoryField.getText();
            String capacity = capacityField.getText();

            manageTables(1, tableId, category, capacity); // Add the table to the system
            showAlert("Success", "Table added successfully!"); // Success alert
        });

        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-pref-width: 60px;");
        closeButton.setOnAction(e -> addForm.getChildren().clear());

        HBox buttonBox = new HBox(10); // Spacing between buttons
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(addButton, closeButton);

        addForm.getChildren().addAll(title, tableIdField, categoryField, capacityField, reservedCheckbox, buttonBox);
        return addForm;
    }

    public VBox getEditTableForm() {
        VBox editForm = new VBox(20);
        editForm.setAlignment(Pos.CENTER);

        Label title = new Label("Edit Table");
        title.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-weight: bold;");

        TextField tableIdField = new TextField();
        tableIdField.setPromptText("Table ID");

        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");

        TextField capacityField = new TextField();
        capacityField.setPromptText("Capacity");

        CheckBox reservedCheckbox = new CheckBox("Reserved");
        reservedCheckbox.setStyle("-fx-font-size: 16px; -fx-font-family: Arial; -fx-text-fill: #333333; -fx-padding: 5px;");
        reservedCheckbox.setScaleX(1.0);
        reservedCheckbox.setScaleY(1.0);

        Button editButton = new Button("Edit Table");
        styleButton(editButton);
        editButton.setOnAction(e -> {
            String tableId = tableIdField.getText();
            String category = categoryField.getText();
            String capacity = capacityField.getText();

            manageTables(2, tableId, category, capacity); // Edit the table in the system
            showAlert("Success", "Table edited successfully!"); // Success alert
        });

        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-pref-width: 60px;");
        closeButton.setOnAction(e -> editForm.getChildren().clear());

        HBox buttonBox = new HBox(10); // Spacing between buttons
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(editButton, closeButton);

        editForm.getChildren().addAll(title, tableIdField, categoryField, capacityField, reservedCheckbox, buttonBox);
        return editForm;
    }

    public VBox getRemoveTableForm() {
        VBox removeForm = new VBox(20);
        removeForm.setAlignment(Pos.CENTER);

        Label title = new Label("Remove Table");
        title.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-weight: bold;");

        TextField tableIdField = new TextField();
        tableIdField.setPromptText("Table ID");

        Button removeButton = new Button("Remove Table");
        styleButton(removeButton);
        removeButton.setOnAction(e -> {
            String tableId = tableIdField.getText();
            manageTables(3, tableId); // Remove the table from the system
            showAlert("Success", "Table removed successfully!"); // Success alert
        });

        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-pref-width: 60px;");
        closeButton.setOnAction(e -> removeForm.getChildren().clear());

        HBox buttonBox = new HBox(10); // Spacing between buttons
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(removeButton, closeButton);

        removeForm.getChildren().addAll(title, tableIdField, buttonBox);
        return removeForm;
    }

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
    private void manageTables(int action, String... args) {
        // Handle table addition, edition, or removal based on the action
        switch (action) {
            case 1:
                // Add table logic here
                break;
            case 2:
                // Edit table logic here
                break;
            case 3:
                // Remove table logic here
                break;
            default:
                break;
        }
    }

    // Create and display the main window
    public void showTableManagementWindow(Stage primaryStage) {
        BorderPane mainLayout = createMainLayout();
        Scene scene = new Scene(mainLayout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Manage Tables");
        primaryStage.show();
    }
}
