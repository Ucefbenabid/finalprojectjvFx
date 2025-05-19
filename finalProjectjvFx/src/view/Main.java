//import src.inventory.controller.AuthController;
//import src.inventory.view.LoginView;
package view;




import controller.AuthController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class that initializes and starts the Inventory Management System.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Set up the primary stage
        primaryStage.setTitle("Inventory Management System");
        
        // Initialize the authentication controller
        AuthController authController = new AuthController();
        
        // Create and configure the login view
        LoginView loginView = new LoginView(authController, primaryStage);
        Scene loginScene = new Scene(loginView.getRoot(), 1024, 768);
        
        // Load CSS styles
        loginScene.getStylesheets().add(getClass().getResource("/resources/main.css").toExternalForm());
        
        // Set the scene and show the stage
        primaryStage.setScene(loginScene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}