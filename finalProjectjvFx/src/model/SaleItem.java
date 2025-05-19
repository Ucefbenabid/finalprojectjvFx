package model;


import java.math.BigDecimal;

/**
 * Model class representing an item in a sale transaction.
 */
public class SaleItem {
    private int id;
    private int saleId;
    private int productId;
    private String productName; // Transient field for display purposes
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal discount;
    private BigDecimal subtotal;

    // Default constructor
    public SaleItem() {
        this.quantity = 1;
        this.discount = BigDecimal.ZERO;
    }

    // Constructor with essential fields
    public SaleItem(int productId, String productName, int quantity, BigDecimal unitPrice) {
        this();
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        calculateSubtotal();
    }

    // Full constructor
    public SaleItem(int id, int saleId, int productId, String productName, 
                   int quantity, BigDecimal unitPrice, BigDecimal discount) {
        this.id = id;
        this.saleId = saleId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discount = discount;
        calculateSubtotal();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateSubtotal();
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        calculateSubtotal();
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
        calculateSubtotal();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    // Business logic methods
    private void calculateSubtotal() {
        if (unitPrice != null) {
            BigDecimal totalBeforeDiscount = unitPrice.multiply(BigDecimal.valueOf(quantity));
            if (discount != null && discount.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal discountAmount = totalBeforeDiscount.multiply(discount).divide(BigDecimal.valueOf(100));
                this.subtotal = totalBeforeDiscount.subtract(discountAmount);
            } else {
                this.subtotal = totalBeforeDiscount;
            }
        } else {
            this.subtotal = BigDecimal.ZERO;
        }
    }

    @Override
    public String toString() {
        return "SaleItem{" +
                "productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", subtotal=" + subtotal +
                '}';
    }
}