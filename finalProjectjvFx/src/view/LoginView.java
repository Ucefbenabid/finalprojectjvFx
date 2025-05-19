package view;

import controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * View class for the login screen.
 */
public class LoginView {
    
    private final AuthController authController;
    private final Stage primaryStage;
    private final VBox root;
    private TextField usernameField;
    private PasswordField passwordField;
    private Text errorText;
    
    public LoginView(AuthController authController, Stage primaryStage) {
        this.authController = authController;
        this.primaryStage = primaryStage;
        this.root = createContent();
    }
    
    /**
     * Creates the content for the login view.
     *
     * @return The root node for the login view
     */
    private VBox createContent() {
        // Create the form container
        VBox loginForm = new VBox(15);
        loginForm.setPadding(new Insets(30));
        loginForm.setMaxWidth(400);
        loginForm.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.2), 10, 0, 0, 3);");
        
        // Title
        Label titleLabel = new Label("Inventory Management System");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #147EFB;");
        loginForm.getChildren().add(titleLabel);
        
        // Subtitle
        Label subtitleLabel = new Label("Log in to your account");
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #6B7280;");
        loginForm.getChildren().add(subtitleLabel);
        
        // Add some space
        loginForm.getChildren().add(new Separator());
        
        // Username field
        Label usernameLabel = new Label("Username");
        usernameLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #374151;");
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");
        usernameField.setPrefHeight(40);
        usernameField.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #E5E7EB; -fx-border-width: 1;");
        loginForm.getChildren().addAll(usernameLabel, usernameField);
        
        // Password field
        Label passwordLabel = new Label("Password");
        passwordLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #374151;");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setPrefHeight(40);
        passwordField.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-border-color: #E5E7EB; -fx-border-width: 1;");
        loginForm.getChildren().addAll(passwordLabel, passwordField);
        
        // Error message
        errorText = new Text();
        errorText.setFill(Color.RED);
        errorText.setStyle("-fx-font-size: 12px;");
        errorText.setVisible(false);
        loginForm.getChildren().add(errorText);
        
        // Login button
        Button loginButton = new Button("Log In");
        loginButton.setPrefHeight(40);
        loginButton.setPrefWidth(Double.MAX_VALUE);
        loginButton.setStyle("-fx-background-color: #147EFB; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        loginButton.setOnAction(e -> handleLogin());
        loginForm.getChildren().add(loginButton);
        
        // Demo account info
        VBox demoBox = new VBox(5);
        demoBox.setAlignment(Pos.CENTER);
        demoBox.setPadding(new Insets(10, 0, 0, 0));
        
        Label demoLabel = new Label("Demo Account");
        demoLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6B7280; -fx-font-weight: bold;");
        
        HBox demoInfo = new HBox(10);
        demoInfo.setAlignment(Pos.CENTER);
        
        VBox usernameInfo = new VBox(2);
        usernameInfo.setAlignment(Pos.CENTER);
        Label demoUsernameLabel = new Label("Username");
        demoUsernameLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #6B7280;");
        Label demoUsername = new Label("admin");
        demoUsername.setStyle("-fx-font-size: 12px; -fx-text-fill: #374151; -fx-font-weight: bold;");
        usernameInfo.getChildren().addAll(demoUsernameLabel, demoUsername);
        
        VBox passwordInfo = new VBox(2);
        passwordInfo.setAlignment(Pos.CENTER);
        Label demoPasswordLabel = new Label("Password");
        demoPasswordLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #6B7280;");
        Label demoPassword = new Label("admin123");
        demoPassword.setStyle("-fx-font-size: 12px; -fx-text-fill: #374151; -fx-font-weight: bold;");
        passwordInfo.getChildren().addAll(demoPasswordLabel, demoPassword);
        
        demoInfo.getChildren().addAll(usernameInfo, passwordInfo);
        demoBox.getChildren().addAll(demoLabel, demoInfo);
        
        loginForm.getChildren().add(demoBox);
        
        // Create a center-aligned container for the form
        StackPane container = new StackPane(loginForm);
        container.setStyle("-fx-background-color: #F3F4F6;");
        StackPane.setAlignment(loginForm, Pos.CENTER);
        
        // Create the root layout
        VBox root = new VBox();
        root.getChildren().add(container);
        root.setStyle("-fx-background-color: #F3F4F6;");
        
        // Make the container fill all available space
        container.prefWidthProperty().bind(root.widthProperty());
        container.prefHeightProperty().bind(root.heightProperty());
        
        return root;
    }
    
    /**
     * Handles the login button click.
     */
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }
        
        boolean success = authController.login(username, password, primaryStage);
        
        if (!success) {
            showError("Invalid username or password");
        }
    }
    
    /**
     * Shows an error message.
     *
     * @param message The error message to display
     */
    private void showError(String message) {
        errorText.setText(message);
        errorText.setVisible(true);
    }
    
    /**
     * Gets the root node for the login view.
     *
     * @return The root node
     */
    public Parent getRoot() {
        return root;
    }
}