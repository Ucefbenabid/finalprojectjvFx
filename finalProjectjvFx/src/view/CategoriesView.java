package view;

import controller.ProductController;
import model.Category;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CategoriesView {
    private final ProductController productController;
    private final VBox content;
    
    public CategoriesView(ProductController productController) {
        this.productController = productController;
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
        Label title = new Label("Categories Management");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #111827;");
        
        // Actions bar
        HBox actionsBar = new HBox(10);
        actionsBar.setAlignment(Pos.CENTER_LEFT);
        
        Button addButton = new Button("Add New Category");
        addButton.getStyleClass().add("button-primary");
        
        actionsBar.getChildren().add(addButton);
        
        // Categories table
        TableView<Category> categoriesTable = new TableView<>();
        categoriesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<Category, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        
        TableColumn<Category, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));
        
        TableColumn<Category, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().isActive() ? "Active" : "Inactive"));
        
        categoriesTable.getColumns().addAll(nameCol, descriptionCol, statusCol);
        
        // Load categories
        categoriesTable.getItems().addAll(productController.getAllCategories());
        
        // Add components to container
        container.getChildren().addAll(title, actionsBar, categoriesTable);
        
        return container;
    }
}