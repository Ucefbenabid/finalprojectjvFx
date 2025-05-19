package model;


import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Model class representing a product in the inventory system.
 */
public class Product {
    private int id;
    private String name;
    private String description;
    private String sku;
    private BigDecimal price;
    private int categoryId;
    private String categoryName; // Transient field for display purposes
    private int currentStock;
    private int reorderLevel;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public Product() {
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor with essential fields
    public Product(String name, String description, String sku, BigDecimal price, 
                  int categoryId, int currentStock, int reorderLevel) {
        this();
        this.name = name;
        this.description = description;
        this.sku = sku;
        this.price = price;
        this.categoryId = categoryId;
        this.currentStock = currentStock;
        this.reorderLevel = reorderLevel;
    }

    // Full constructor
    public Product(int id, String name, String description, String sku, BigDecimal price, 
                  int categoryId, String categoryName, int currentStock, int reorderLevel,
                  boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.sku = sku;
        this.price = price;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.currentStock = currentStock;
        this.reorderLevel = reorderLevel;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Business logic methods
    public boolean isLowStock() {
        return currentStock <= reorderLevel;
    }
    
    public void updateStock(int quantity) {
        this.currentStock += quantity;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sku='" + sku + '\'' +
                ", price=" + price +
                ", currentStock=" + currentStock +
                '}';
    }
}