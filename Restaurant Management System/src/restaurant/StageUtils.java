package restaurant;

import javafx.stage.Stage;

public class StageUtils {

    // Method to keep the stage size consistent when switching scenes
    public static void setStageSize(Stage primaryStage, double width, double height) {
        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
    }
}
