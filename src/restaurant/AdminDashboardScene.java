package restaurant;

import static javafx.application.Application.launch;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AdminDashboardScene {
        Receptionist receptionist=new Receptionist();


    private Scene firstScene; // Store the first scene

    // Constructor to accept the firstScene
    public AdminDashboardScene(Scene firstScene) {
        this.firstScene = firstScene;
    }

    // Creates the admin dashboard scene
    public Scene createAdminDashboardScene(Stage primaryStage) {
        BorderPane root = new BorderPane(); // Create a BorderPane layout to structure the UI

        // Create and set the main sidebar (left side of the screen)
        VBox mainSidebar = createAdminSidebar(primaryStage, root);
        root.setLeft(mainSidebar); // Set the main sidebar to the left of the BorderPane

        // Placeholder for the center content of the dashboard
        VBox mainContent = new VBox(20); // Create a vertical box layout with 20px spacing between elements
        mainContent.setAlignment(Pos.CENTER); // Align the content to the center
        Label placeholderLabel = new Label("Select an option from the sidebar"); // Placeholder label to prompt user
        mainContent.getChildren().add(placeholderLabel); // Add the label to the main content
        root.setCenter(mainContent); // Set the main content in the center of the BorderPane

        
        return new Scene(root, 800, 800, Color.BLACK); // Return a new Scene with black background and size 800x800  
    }
    
 

    // Creates the admin sidebar with navigation buttons
    private VBox createAdminSidebar(Stage primaryStage, BorderPane root) {
        VBox sidebar = new VBox(20); // Create a vertical box layout with 20px spacing between elements
        sidebar.setAlignment(Pos.TOP_CENTER); // Align content in the sidebar to the top center
        sidebar.setStyle("-fx-background-color: #333;"); // Set the sidebar's background color to dark gray
        sidebar.setPrefWidth(200); // Set the sidebar's preferred width to 200px

        
        // Title for the sidebar
        Label dashboardTitle = new Label("Admin Dashboard");
        dashboardTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10px;");

        // Buttons for different sections of the admin dashboard
        Button manageTablesButton = createAdminSidebarButton("Manage Tables", primaryStage, root);
        Button manageMenusButton = createAdminSidebarButton("Manage Menus", primaryStage, root);
        Button manageUsersButton = createAdminSidebarButton("Manage Users", primaryStage, root);
        Button viewDataButton = createAdminSidebarButton("View Data", primaryStage, root);
         
           // Back Button to return to MainScene
        Button backButton = createAdminSidebarButton("Back", primaryStage, root);
        backButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white;; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10px;-fx-pref-width: 180px;");
       
        
    
        
        // Add the title and buttons to the sidebar
        sidebar.getChildren().addAll(
                dashboardTitle, // Add the dashboard title to the sidebar
                manageTablesButton, // Add the "Manage Tables" button
                manageMenusButton, // Add the "Manage Menus" button
                manageUsersButton, // Add the "Manage Users" button
                viewDataButton,
                backButton
        );
        

        return sidebar; // Return the sidebar to be used in the root layout
    }
       
     
    // Creates a button for the admin sidebar
    private Button createAdminSidebarButton(String buttonText, Stage primaryStage, BorderPane root) {
        Button button = new Button(buttonText); // Create a new button with the given text
        button.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px; -fx-pref-width: 180px;"); // Style the button with colors and padding
        
        
        // Set an action when the button is clicked
        button.setOnAction(e -> {
            if (buttonText.equals("Manage Tables")) {
                openSecondSidebar(primaryStage, root, createSidebarForManageTables(root));
                // Open sidebar for managing tables
            } else if (buttonText.equals("Manage Menus")) {
                openSecondSidebar(primaryStage, root, createSidebarForManageMenus(root)); // Open sidebar for managing menus
            } else if (buttonText.equals("Manage Users")) {
                openSecondSidebar(primaryStage, root, createSidebarForManageUsers(root)); // Open sidebar for managing users
            } else if (buttonText.equals("View Data")) {
                openSecondSidebar(primaryStage, root, createSidebarForViewData(root)); // Open sidebar for viewing data
            }else if (buttonText.equals("Back")){
                primaryStage.setScene(firstScene);
            }
            });
        
       
        return button; // Return the button
    }
                
    

    // Open a second sidebar next to the main sidebar
    private void openSecondSidebar(Stage primaryStage, BorderPane root, VBox secondSidebar) {
        VBox mainSidebar = createAdminSidebar(primaryStage, root); // Create the main sidebar again for consistency

        HBox combinedSidebar = new HBox(); // Create a horizontal box layout to combine both sidebars
        combinedSidebar.setSpacing(0); // Set no space between the two sidebars
        combinedSidebar.getChildren().addAll(mainSidebar, secondSidebar); // Add both sidebars to the horizontal box

        root.setLeft(combinedSidebar); // Set the combined sidebars on the left of the root layout
    }

    // Method to close the second sidebar
    private void closeSecondSidebar(VBox secondSidebar) {
        BorderPane root = (BorderPane) secondSidebar.getParent().getParent(); // Access the root layout
        HBox combinedSidebar = (HBox) root.getLeft(); // Get the combined sidebars
        combinedSidebar.getChildren().remove(secondSidebar); // Remove the second sidebar
    }

   
    
  private VBox createSidebarForManageTables(BorderPane root) {
    ManageTables manageTables = new ManageTables(); // Create instance of ManageTables

    VBox sidebar = new VBox(20);
    sidebar.setAlignment(Pos.TOP_CENTER);
    sidebar.setStyle("-fx-background-color: #444;");
    sidebar.setPrefWidth(200);

    Label title = new Label("Manage Tables");
    title.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10px;");

    Button addTableButton = new Button("Add Table");
    Button editTableButton = new Button("Edit Table");
    Button removeTableButton = new Button("Remove Table");

    styleSidebarButton(addTableButton);
    styleSidebarButton(editTableButton);
    styleSidebarButton(removeTableButton);

    // Button actions to display forms from ManageTables
    addTableButton.setOnAction(e -> root.setCenter(manageTables.getAddTableForm()));
    editTableButton.setOnAction(e -> root.setCenter(manageTables.getEditTableForm()));
    removeTableButton.setOnAction(e -> root.setCenter(manageTables.getRemoveTableForm()));

    Button closeButton = new Button("Close");
    closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-pref-width: 180px;");
    closeButton.setOnAction(e -> closeSecondSidebar(sidebar));

    sidebar.getChildren().addAll(title, addTableButton, editTableButton, removeTableButton, closeButton);
    return sidebar;
}
  


  private VBox createSidebarForManageMenus(BorderPane root) {
    ManageMenu manageMenu = new ManageMenu(); // Create instance of ManageMenu

    VBox sidebar = new VBox(20);
    sidebar.setAlignment(Pos.TOP_CENTER);
    sidebar.setStyle("-fx-background-color: #444;");
    sidebar.setPrefWidth(200);

    Label title = new Label("Manage Menus");
    title.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10px;");

    // Buttons for managing menu items
    Button addMenuItemButton = new Button("Add Item");
    Button editMenuItemButton = new Button("Edit Item");
    Button removeMenuItemButton = new Button("Remove Item");

    // Apply common button styling
    styleSidebarButton(addMenuItemButton);
    styleSidebarButton(editMenuItemButton);
    styleSidebarButton(removeMenuItemButton);

    // Button actions to display forms from ManageMenu
    addMenuItemButton.setOnAction(e -> root.setCenter(manageMenu.getAddMenuForm()));
    editMenuItemButton.setOnAction(e -> root.setCenter(manageMenu.getEditMenuForm()));
    removeMenuItemButton.setOnAction(e -> root.setCenter(manageMenu.getRemoveMenuForm()));

    // Close button to close the sidebar
    Button closeButton = new Button("Close");
    closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-pref-width: 180px;");
    closeButton.setOnAction(e -> closeSecondSidebar(sidebar));

    // Add all components to the sidebar
    sidebar.getChildren().addAll(title, addMenuItemButton, editMenuItemButton, removeMenuItemButton, closeButton);
    
    return sidebar; // Return the completed sidebar
}


    // Sidebar for managing users
    private VBox createSidebarForManageUsers(BorderPane root) {
        
     ManageUsers manageUsers= new ManageUsers();
  
        VBox sidebar = new VBox(20); // Create a vertical box layout with 20px spacing
        sidebar.setAlignment(Pos.TOP_CENTER); // Align content at the top center
        sidebar.setStyle("-fx-background-color: #444;"); // Set a background color for the sidebar
        sidebar.setPrefWidth(200); // Set the sidebar's preferred width to 200px

        Label title = new Label("   Manage Users"); // Label for the "Manage Users" section
        title.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10px; -fx-pref-width: 180px;");

        // Buttons for managing users
        Button addUserButton = new Button("Add User");
        Button editUserButton = new Button("Edit User");
        Button removeUserButton = new Button("Remove User");

        // Apply common button styling
        styleSidebarButton(addUserButton);
        styleSidebarButton(editUserButton);
        styleSidebarButton(removeUserButton);
        
         // Button actions to display forms from ManageMenu
    addUserButton.setOnAction(e -> root.setCenter(manageUsers.getAddUserForm()));
    editUserButton.setOnAction(e -> root.setCenter(manageUsers.getEditUserForm()));
    removeUserButton.setOnAction(e -> root.setCenter(manageUsers.getRemoveUserForm()));

        // Close button
        Button closeButton = new Button("Close");
        closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-pref-width: 180px;");
        closeButton.setOnAction(e -> closeSecondSidebar(sidebar));

        sidebar.getChildren().addAll(title, addUserButton, editUserButton, removeUserButton, closeButton); // Add buttons to sidebar
        return sidebar; // Return the completed sidebar
    }

   // Sidebar for viewing data
private VBox createSidebarForViewData(BorderPane root) {
    ViewData viewdata=new ViewData();
    
    VBox sidebar = new VBox(20); // Create a vertical box layout with 20px spacing
    sidebar.setAlignment(Pos.TOP_CENTER); // Align content at the top center
    sidebar.setStyle("-fx-background-color: #444;"); // Set a background color for the sidebar
    sidebar.setPrefWidth(200); // Set the sidebar's preferred width to 200px

    Label title = new Label("     View Data"); // Label for the "View Data" section
    title.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 10px; -fx-pref-width: 180px;");

    // Buttons for viewing data
    Button viewUsersButton = new Button("View Users");
    Button viewTablesButton = new Button("View Tables");
    Button viewMenuItemsButton = new Button("View Menu Items");
    Button viewReservationsButton = new Button("View Reservations");

    // Apply common button styling
    styleSidebarButton(viewUsersButton);
    styleSidebarButton(viewTablesButton);
    styleSidebarButton(viewMenuItemsButton);
    styleSidebarButton(viewReservationsButton);

    // Define actions for each button
    viewUsersButton.setOnAction(e ->root.setCenter(viewdata.UsersView()));
    viewTablesButton.setOnAction(e -> root.setCenter(viewdata.TablesView()));
    viewMenuItemsButton.setOnAction(e ->root.setCenter(viewdata.MenuView()));
   viewReservationsButton.setOnAction(e ->root.setCenter(viewdata.ReservationsView()));

    // Close button
    Button closeButton = new Button("Close");
    closeButton.setStyle("-fx-background-color: #d9534f; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-pref-width: 180px;");
    closeButton.setOnAction(e -> closeSecondSidebar(sidebar));

    sidebar.getChildren().addAll(title, viewUsersButton, viewTablesButton, viewMenuItemsButton, viewReservationsButton, closeButton); // Add buttons to sidebar
    return sidebar; // Return the completed sidebar
}

    // Helper method to style sidebar buttons
    private void styleSidebarButton(Button button) {
        button.setStyle("-fx-background-color: #555; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px; -fx-pref-width: 180px;");
    }
     
}