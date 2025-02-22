package restaurant;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class BackgroundUtils {

    public static void setupBackground(StackPane root, double translateX, double translateY, double width, double height) {
        // Load the background image
        Image backgroundImage = new Image("file:C:/Users/joann/OneDrive/Documents/NetBeansProjects/Restaurant/Restaurant/src/restaurant/logo.png");
        ImageView backgroundImageView = new ImageView(backgroundImage);

        // Set the width and height of the image
        backgroundImageView.setPreserveRatio(true);
        backgroundImageView.setFitWidth(width);
        backgroundImageView.setFitHeight(height);

        // Set the translation of the image
        backgroundImageView.setTranslateX(translateX);
        backgroundImageView.setTranslateY(translateY);

        // Add the background image to the root node
        root.getChildren().add(backgroundImageView);

        // Set the black background color for the StackPane
        root.setStyle("-fx-background-color: black;");
    }

  
}