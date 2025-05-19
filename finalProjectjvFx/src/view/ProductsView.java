package view;


import controller.ProductController;
import model.Product;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.List;

public class ProductsView {
    private final ProductController productController;
    private final VBox content;
    
    public ProductsView(ProductController productController) {
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
        Label title = new Label("Products Management");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #111827;");
        
        // Actions bar
        HBox actionsBar = new HBox(10);
        actionsBar.setAlignment(Pos.CENTER_LEFT);
        
        TextField searchField = new TextField();
        searchField.setPromptText("Search products...");
        searchField.setPrefWidth(300);
        
        Button addButton = new Button("Add New Product");
        addButton.getStyleClass().add("button-primary");
        
        actionsBar.getChildren().addAll(searchField, addButton);
        
        // Products table
        TableView<Product> productsTable = new TableView<>();
        productsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<Product, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        
        TableColumn<Product, String> skuCol = new TableColumn<>("SKU");
        skuCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSku()));
        
        TableColumn<Product, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCategoryName()));
        
        TableColumn<Product, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            "$" + data.getValue().getPrice().toString()));
        
        TableColumn<Product, String> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            String.valueOf(data.getValue().getCurrentStock())));
        
        productsTable.getColumns().addAll(nameCol, skuCol, categoryCol, priceCol, stockCol);
        
        // Load products
        List<Product> products = productController.getAllProducts();
        productsTable.getItems().addAll(products);
        
        // Add components to container
        container.getChildren().addAll(title, actionsBar, productsTable);
        
        return container;
    }
}