package view;


import controller.ProductController;
import model.Product;
import model.StockTransaction;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.format.DateTimeFormatter;

public class InventoryView {
    private final ProductController productController;
    private final VBox content;
    
    public InventoryView(ProductController productController) {
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
        Label title = new Label("Inventory Management");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #111827;");
        
        // Actions bar
        HBox actionsBar = new HBox(10);
        actionsBar.setAlignment(Pos.CENTER_LEFT);
        
        Button stockInButton = new Button("Stock In");
        stockInButton.getStyleClass().add("button-primary");
        
        Button stockOutButton = new Button("Stock Out");
        stockOutButton.getStyleClass().add("button-secondary");
        
        actionsBar.getChildren().addAll(stockInButton, stockOutButton);
        
        // Stock transactions table
        TableView<StockTransaction> transactionsTable = new TableView<>();
        transactionsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<StockTransaction, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getTransactionDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        
        TableColumn<StockTransaction, String> productCol = new TableColumn<>("Product");
        productCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getProductName()));
        
        TableColumn<StockTransaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getTransactionType()));
        
        TableColumn<StockTransaction, String> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            String.valueOf(data.getValue().getQuantity())));
        
        TableColumn<StockTransaction, String> reasonCol = new TableColumn<>("Reason");
        reasonCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getReason()));
        
        transactionsTable.getColumns().addAll(dateCol, productCol, typeCol, quantityCol, reasonCol);
        
        // Add components to container
        container.getChildren().addAll(title, actionsBar, transactionsTable);
        
        return container;
    }
}