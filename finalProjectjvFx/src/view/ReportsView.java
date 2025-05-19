package view;


import controller.ProductController;
import controller.SaleController;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.chart.*;

public class ReportsView {
    private final ProductController productController;
    private final SaleController saleController;
    private final VBox content;
    
    public ReportsView(ProductController productController, SaleController saleController) {
        this.productController = productController;
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
        Label title = new Label("Reports");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #111827;");
        
        // Report types tabs
        TabPane reportTabs = new TabPane();
        reportTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Sales Report Tab
        Tab salesTab = new Tab("Sales Report");
        salesTab.setContent(createSalesReport());
        
        // Inventory Report Tab
        Tab inventoryTab = new Tab("Inventory Report");
        inventoryTab.setContent(createInventoryReport());
        
        reportTabs.getTabs().addAll(salesTab, inventoryTab);
        
        // Add components to container
        container.getChildren().addAll(title, reportTabs);
        
        return container;
    }
    
    private VBox createSalesReport() {
        VBox reportContainer = new VBox(20);
        reportContainer.setPadding(new Insets(20));
        
        // Date range selector
        HBox dateRange = new HBox(10);
        dateRange.getChildren().addAll(
            new Label("From:"),
            new DatePicker(),
            new Label("To:"),
            new DatePicker()
        );
        
        // Sales chart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> salesChart = new BarChart<>(xAxis, yAxis);
        salesChart.setTitle("Sales by Date");
        
        reportContainer.getChildren().addAll(dateRange, salesChart);
        return reportContainer;
    }
    
    private VBox createInventoryReport() {
        VBox reportContainer = new VBox(20);
        reportContainer.setPadding(new Insets(20));
        
        // Stock level chart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> stockChart = new BarChart<>(xAxis, yAxis);
        stockChart.setTitle("Current Stock Levels");
        
        reportContainer.getChildren().add(stockChart);
        return reportContainer;
    }
}