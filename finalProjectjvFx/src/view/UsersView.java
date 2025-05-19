package view;

import controller.AuthController;
import model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.format.DateTimeFormatter;

public class UsersView {
    private final AuthController authController;
    private final VBox content;
    
    public UsersView(AuthController authController) {
        this.authController = authController;
        this.content = createContent();
    }
    
    public VBox getContent() {
        return content;
    }
    
    private VBox createContent() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));
        container.setStyle("-fx-background-color: #F3F4F6;");
        
        // Header
        Label title = new Label("User Management");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #111827;");
        
        // Actions bar
        HBox actionsBar = new HBox(10);
        actionsBar.setAlignment(Pos.CENTER_LEFT);
        
        Button addButton = new Button("Add New User");
        addButton.getStyleClass().add("button-primary");
        
        actionsBar.getChildren().add(addButton);
        
        // Users table
        TableView<User> usersTable = new TableView<>();
        usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getUsername()));
        
        TableColumn<User, String> nameCol = new TableColumn<>("Full Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getFullName()));
        
        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getEmail()));
        
        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getRole()));
        
        TableColumn<User, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().isActive() ? "Active" : "Inactive"));
        
        TableColumn<User, String> lastLoginCol = new TableColumn<>("Last Login");
        lastLoginCol.setCellValueFactory(data -> {
            if (data.getValue().getLastLogin() != null) {
                return new javafx.beans.property.SimpleStringProperty(
                    data.getValue().getLastLogin().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }
            return new javafx.beans.property.SimpleStringProperty("Never");
        });
        
        usersTable.getColumns().addAll(usernameCol, nameCol, emailCol, roleCol, statusCol, lastLoginCol);
        
        // Add components to container
        container.getChildren().addAll(title, actionsBar, usersTable);
        
        return container;
    }
}