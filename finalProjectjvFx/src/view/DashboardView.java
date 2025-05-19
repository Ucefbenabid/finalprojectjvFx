package view;

import controller.AuthController;
import controller.ProductController;
import controller.SaleController;
import model.Product;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

/**
 * View class for the dashboard screen.
 */
public class DashboardView {
    
    private final AuthController authController;
    private final ProductController productController;
    private final SaleController saleController;
    private final Stage primaryStage;
    private final BorderPane root;
    private Button currentActiveButton;
    
    public DashboardView(AuthController authController, Stage primaryStage) {
        this.authController = authController;
        this.primaryStage = primaryStage;
        this.productController = new ProductController(authController);
        this.saleController = new SaleController(authController);
        this.root = createContent();
    }
    
    public BorderPane getRoot() {
        return root;
    }
    
    private BorderPane createContent() {
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: #F3F4F6;");
        
        // Top header
        borderPane.setTop(createHeader());
        
        // Left sidebar
        VBox sidebar = createSidebar();
        BorderPane.setMargin(sidebar, new Insets(0, 0, 0, 0));
        borderPane.setLeft(sidebar);
        
        // Center content with padding
        ScrollPane centerContent = createDashboardContent();
        BorderPane.setMargin(centerContent, new Insets(0));
        borderPane.setCenter(centerContent);
        
        return borderPane;
    }
    
    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15));
        header.setSpacing(20);
        header.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 0, 4, 0, 0);");
        
        Label titleLabel = new Label("Inventory Management System");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #147EFB;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        String userName = authController.getCurrentUser().getFullName();
        Label userLabel = new Label("Welcome, " + userName);
        userLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #374151;");
        
        Button logoutButton = new Button("Logout");
        logoutButton.getStyleClass().add("button-secondary");
        logoutButton.setOnAction(e -> handleLogout());
        
        header.getChildren().addAll(titleLabel, spacer, userLabel, logoutButton);
        return header;
    }
    
    private VBox createSidebar() {
        VBox sidebar = new VBox();
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 4, 0, 0, 0);");
        
        String[] menuItems = {"Dashboard", "Products", "Categories", "Sales", "Inventory", "Reports", "Users"};
        
        for (String item : menuItems) {
            Button menuButton = new Button(item);
            menuButton.setPrefWidth(Double.MAX_VALUE);
            menuButton.setPrefHeight(45);
            menuButton.setAlignment(Pos.CENTER_LEFT);
            menuButton.setPadding(new Insets(0, 0, 0, 20));
            menuButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #4B5563; -fx-font-size: 14px;");
            
            if (item.equals("Dashboard")) {
                menuButton.setStyle("-fx-background-color: #EBF5FF; -fx-text-fill: #147EFB; -fx-border-width: 0 0 0 4; -fx-border-color: #147EFB;");
                currentActiveButton = menuButton;
            }
            
            menuButton.setOnMouseEntered(e -> {
                if (menuButton != currentActiveButton) {
                    menuButton.setStyle("-fx-background-color: #F9FAFB; -fx-text-fill: #4B5563;");
                }
            });
            
            menuButton.setOnMouseExited(e -> {
                if (menuButton != currentActiveButton) {
                    menuButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #4B5563;");
                }
            });
            
            menuButton.setOnAction(e -> handleMenuClick(item, menuButton));
            sidebar.getChildren().add(menuButton);
        }
        
        return sidebar;
    }
    
    private ScrollPane createDashboardContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        
        // Dashboard header
        Label dashboardTitle = new Label("Dashboard Overview");
        dashboardTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #111827;");
        
        // Summary cards
        HBox summaryCards = new HBox(20);
        summaryCards.setAlignment(Pos.CENTER_LEFT);
        
        List<Product> allProducts = productController.getAllProducts();
        List<Product> lowStockProducts = productController.getLowStockProducts();
        
        VBox totalProductsCard = createSummaryCard("Total Products", String.valueOf(allProducts.size()), "#34C759");
        VBox lowStockCard = createSummaryCard("Low Stock Items", String.valueOf(lowStockProducts.size()), "#FF9500");
        VBox totalSalesCard = createSummaryCard("Total Sales", "0", "#147EFB");
        VBox revenueCard = createSummaryCard("Revenue", "$0.00", "#FF3B30");
        
        summaryCards.getChildren().addAll(totalProductsCard, lowStockCard, totalSalesCard, revenueCard);
        
        // Low stock alerts
        VBox alertsSection = new VBox(10);
        alertsSection.setPadding(new Insets(20));
        alertsSection.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 0, 4, 0, 0);");
        
        Label alertsTitle = new Label("Low Stock Alerts");
        alertsTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        TableView<Product> alertsTable = new TableView<>();
        alertsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<Product, String> nameCol = new TableColumn<>("Product Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        
        TableColumn<Product, String> skuCol = new TableColumn<>("SKU");
        skuCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSku()));
        
        TableColumn<Product, String> stockCol = new TableColumn<>("Current Stock");
        stockCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            String.valueOf(data.getValue().getCurrentStock())));
        
        TableColumn<Product, String> reorderCol = new TableColumn<>("Reorder Level");
        reorderCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            String.valueOf(data.getValue().getReorderLevel())));
        
        alertsTable.getColumns().addAll(nameCol, skuCol, stockCol, reorderCol);
        alertsTable.getItems().addAll(lowStockProducts);
        
        alertsSection.getChildren().addAll(alertsTitle, alertsTable);
        
        // Add all components to the main content
        content.getChildren().addAll(dashboardTitle, summaryCards, alertsSection);
        
        // Wrap in ScrollPane
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        
        return scrollPane;
    }
    
    private VBox createSummaryCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setPrefWidth(220);
        card.setStyle(String.format("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 0, 4, 0, 0); -fx-border-color: %s; -fx-border-width: 0 0 3 0; -fx-border-radius: 8;", color));
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #6B7280;");
        
        Label valueLabel = new Label(value);
        valueLabel.setStyle(String.format("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: %s;", color));
        
        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }
    
    private void handleMenuClick(String menuItem, Button clickedButton) {
        // Update button styles
        if (currentActiveButton != null) {
            currentActiveButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #4B5563; -fx-font-size: 14px;");
        }
        clickedButton.setStyle("-fx-background-color: #EBF5FF; -fx-text-fill: #147EFB; -fx-border-width: 0 0 0 4; -fx-border-color: #147EFB;");
        currentActiveButton = clickedButton;
        
        // Handle navigation
        switch (menuItem) {
            case "Dashboard":
                root.setCenter(createDashboardContent());
                break;
            case "Products":
                ProductsView productsView = new ProductsView(productController);
                root.setCenter(productsView.getContent());
                break;
            case "Categories":
                CategoriesView categoriesView = new CategoriesView(productController);
                root.setCenter(categoriesView.getContent());
                break;
            case "Sales":
                SalesView salesView = new SalesView(saleController);
                root.setCenter(salesView.getContent());
                break;
            case "Inventory":
                InventoryView inventoryView = new InventoryView(productController);
                root.setCenter(inventoryView.getContent());
                break;
            case "Reports":
                ReportsView reportsView = new ReportsView(productController, saleController);
                root.setCenter(reportsView.getContent());
                break;
            case "Users":
                if (authController.isAdmin()) {
                    UsersView usersView = new UsersView(authController);
                    root.setCenter(usersView.getContent());
                } else {
                    showAccessDeniedAlert();
                }
                break;
        }
    }
    
    private void showAccessDeniedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Access Denied");
        alert.setHeaderText("Permission Required");
        alert.setContentText("You need administrator privileges to access this section.");
        alert.showAndWait();
    }
    
    private void handleLogout() {
        if (authController.logout(primaryStage)) {
            LoginView loginView = new LoginView(authController, primaryStage);
            Scene loginScene = new Scene(loginView.getRoot(), 1024, 768);
            loginScene.getStylesheets().add(getClass().getResource("/styles/main.css").toExternalForm());
            primaryStage.setScene(loginScene);
        }
    }
    
    public void show() {
        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("/resources/main.css").toExternalForm());
        primaryStage.setScene(scene);
    }
}