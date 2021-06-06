package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class that runs the the Inventory Management System application.
 *
 * FUTURE ENHANCEMENT: Use a single form for Add/Modify Part and a single form for Add/Modify Product to reduce the amount of time spent maintaining duplicate features.
 *
 * Javadocs are in the main level /javadoc folder.
 *
 * @author lydia00
 */
public class Main extends Application {

    /**
     * Shows the Inventory Management System main screen.
     * @param stage Stage to load for the main screen.
     */
    @Override
    public void start(Stage stage) throws Exception{
        // set hierarchy of first screen
        Parent root = FXMLLoader.load(getClass().getResource("/view_controller/MainScreen.fxml"));

        // create first scene
        Scene scene = new Scene(root);
        stage.setTitle("Inventory Management System");

        // set the scene on the stage
        stage.setScene(scene);

        // show the scene
        stage.show();
    }

    /**
     * The start of the program.
     * @param args Command line argument to run the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
