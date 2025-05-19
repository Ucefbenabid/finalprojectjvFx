package view;

import controller.SaleController;
import model.Sale;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.time.format.DateTimeFormatter;

public class SalesView {
    private final SaleController saleController;
    private final VBox content;
    
    public SalesView(SaleController saleController) {
        this.saleController = saleController;
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
        Label title = new Label("Sales Management");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #111827;");
        
        // Actions bar
        HBox actionsBar = new HBox(10);
        actionsBar.setAlignment(Pos.CENTER_LEFT);
        
        Button newSaleButton = new Button("New Sale");
        newSaleButton.getStyleClass().add("button-primary");
        
        actionsBar.getChildren().add(newSaleButton);
        
        // Sales table
        TableView<Sale> salesTable = new TableView<>();
        salesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<Sale, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getSaleDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        
        TableColumn<Sale, String> customerCol = new TableColumn<>("Customer");
        customerCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getCustomerName()));
        
        TableColumn<Sale, String> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            "$" + data.getValue().getTotalAmount().toString()));
        
        TableColumn<Sale, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getStatus()));
        
        salesTable.getColumns().addAll(dateCol, customerCol, amountCol, statusCol);
        
        // Load sales
        salesTable.getItems().addAll(saleController.getAllSales());
        
        // Add components to container
        container.getChildren().addAll(title, actionsBar, salesTable);
        
        return container;
    }
}