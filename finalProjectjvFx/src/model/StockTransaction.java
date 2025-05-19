package model;


import java.time.LocalDateTime;

/**
 * Model class representing a stock transaction (movement) in the inventory system.
 */
public class StockTransaction {
    private int id;
    private int productId;
    private String productName; // Transient field for display purposes
    private String transactionType; // IN, OUT, ADJUSTMENT
    private int quantity;
    private String reason; // PURCHASE, SALE, RETURN, DAMAGE, CORRECTION
    private String reference; // Reference to related document/transaction
    private int userId;
    private String notes;
    private LocalDateTime transactionDate;
    private LocalDateTime createdAt;

    // Default constructor
    public StockTransaction() {
        this.transactionDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    // Constructor with essential fields
    public StockTransaction(int productId, String transactionType, int quantity, 
                           String reason, int userId) {
        this();
        this.productId = productId;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.reason = reason;
        this.userId = userId;
    }

    // Full constructor
    public StockTransaction(int id, int productId, String productName, String transactionType, 
                           int quantity, String reason, String reference, int userId, 
                           String notes, LocalDateTime transactionDate, LocalDateTime createdAt) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.transactionType = transactionType;
        this.quantity = quantity;
        this.reason = reason;
        this.reference = reference;
        this.userId = userId;
        this.notes = notes;
        this.transactionDate = transactionDate;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Business logic methods
    public boolean isIncoming() {
        return "IN".equals(transactionType);
    }

    public boolean isOutgoing() {
        return "OUT".equals(transactionType);
    }

    @Override
    public String toString() {
        return "StockTransaction{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", quantity=" + quantity +
                ", reason='" + reason + '\'' +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
