package model;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a sale transaction in the inventory system.
 */
public class Sale {
    private int id;
    private int userId;
    private String customerName;
    private LocalDateTime saleDate;
    private BigDecimal totalAmount;
    private String status; // PENDING, COMPLETED, CANCELLED
    private String paymentMethod; // CASH, CREDIT_CARD, BANK_TRANSFER
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<SaleItem> items;

    // Default constructor
    public Sale() {
        this.saleDate = LocalDateTime.now();
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.items = new ArrayList<>();
        this.totalAmount = BigDecimal.ZERO;
    }

    // Constructor with essential fields
    public Sale(int userId, String customerName, String paymentMethod) {
        this();
        this.userId = userId;
        this.customerName = customerName;
        this.paymentMethod = paymentMethod;
    }

    // Full constructor
    public Sale(int id, int userId, String customerName, LocalDateTime saleDate, 
               BigDecimal totalAmount, String status, String paymentMethod, String notes,
               LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.customerName = customerName;
        this.saleDate = saleDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.items = new ArrayList<>();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public List<SaleItem> getItems() {
        return items;
    }

    public void setItems(List<SaleItem> items) {
        this.items = items;
        recalculateTotal();
    }

    // Business logic methods
    public void addItem(SaleItem item) {
        this.items.add(item);
        recalculateTotal();
    }

    public void removeItem(SaleItem item) {
        this.items.remove(item);
        recalculateTotal();
    }

    private void recalculateTotal() {
        this.totalAmount = this.items.stream()
                .map(item -> item.getSubtotal())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void complete() {
        this.status = "COMPLETED";
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        this.status = "CANCELLED";
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", saleDate=" + saleDate +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                '}';
    }
}