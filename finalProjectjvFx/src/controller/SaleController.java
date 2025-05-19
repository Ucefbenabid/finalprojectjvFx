package controller;


import dao.ProductDAO;
import dao.SaleDAO;
import dao.StockTransactionDAO;
import dao.impli.ProductDAOImpl;
import dao.impli.SaleDAOImpl;
import dao.impli.StockTransactionDAOImpl;
import model.Product;
import model.Sale;
import model.SaleItem;
import model.StockTransaction;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for sale and transaction operations.
 */
public class SaleController {
    
    private final SaleDAO saleDAO;
    private final ProductDAO productDAO;
    private final StockTransactionDAO stockTransactionDAO;
    private final AuthController authController;
    
    public SaleController(AuthController authController) {
        this.saleDAO = new SaleDAOImpl();
        this.productDAO = new ProductDAOImpl();
        this.stockTransactionDAO = new StockTransactionDAOImpl();
        this.authController = authController;
    }
    
    /**
     * Creates a new sale transaction.
     *
     * @param sale The sale to create
     * @return The ID of the newly created sale, or -1 if the operation failed
     */
    public int createSale(Sale sale) {
        if (!authController.isAuthenticated()) {
            return -1;
        }
        
        // Set the current user ID
        sale.setUserId(authController.getCurrentUser().getId());
        
        // Insert the sale
        int saleId = saleDAO.insert(sale);
        
        if (saleId > 0) {
            // Insert each sale item
            for (SaleItem item : sale.getItems()) {
                saleDAO.insertSaleItem(item, saleId);
                
                // Update product stock levels
                updateProductStock(item.getProductId(), -item.getQuantity(), "SALE", String.valueOf(saleId));
            }
        }
        
        return saleId;
    }
    
    /**
     * Completes a sale transaction.
     *
     * @param saleId The ID of the sale to complete
     * @return true if the completion was successful, false otherwise
     */
    public boolean completeSale(int saleId) {
        return saleDAO.updateStatus(saleId, "COMPLETED");
    }
    
    /**
     * Cancels a sale transaction.
     *
     * @param saleId The ID of the sale to cancel
     * @return true if the cancellation was successful, false otherwise
     */
    public boolean cancelSale(int saleId) {
        Sale sale = saleDAO.findById(saleId);
        
        if (sale != null && "PENDING".equals(sale.getStatus())) {
            // Get all items for this sale
            List<SaleItem> items = saleDAO.findSaleItems(saleId);
            
            // Restore product stock levels
            for (SaleItem item : items) {
                updateProductStock(item.getProductId(), item.getQuantity(), "SALE_CANCELLED", String.valueOf(saleId));
            }
            
            // Update sale status
            return saleDAO.updateStatus(saleId, "CANCELLED");
        }
        
        return false;
    }
    
    /**
     * Retrieves a sale by its ID.
     *
     * @param saleId The ID of the sale to retrieve
     * @return The Sale object if found, null otherwise
     */
    public Sale getSale(int saleId) {
        Sale sale = saleDAO.findById(saleId);
        
        if (sale != null) {
            // Get all items for this sale
            List<SaleItem> items = saleDAO.findSaleItems(saleId);
            sale.setItems(items);
        }
        
        return sale;
    }
    
    /**
     * Retrieves all sales.
     *
     * @return A list of all sales
     */
    public List<Sale> getAllSales() {
        return saleDAO.findAll();
    }
    
    /**
     * Retrieves sales within a date range.
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of sales made within the specified date range
     */
    public List<Sale> getSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return saleDAO.findByDateRange(startDate, endDate);
    }
    
    /**
     * Retrieves sales by status.
     *
     * @param status The status to filter by
     * @return A list of sales with the specified status
     */
    public List<Sale> getSalesByStatus(String status) {
        return saleDAO.findByStatus(status);
    }
    
    /**
     * Adds an item to a pending sale.
     *
     * @param saleId The ID of the sale
     * @param productId The ID of the product to add
     * @param quantity The quantity to add
     * @return true if the item was added successfully, false otherwise
     */
    public boolean addSaleItem(int saleId, int productId, int quantity) {
        Sale sale = saleDAO.findById(saleId);
        
        if (sale != null && "PENDING".equals(sale.getStatus())) {
            Product product = productDAO.findById(productId);
            
            if (product != null && product.getCurrentStock() >= quantity) {
                SaleItem item = new SaleItem();
                item.setProductId(productId);
                item.setProductName(product.getName());
                item.setQuantity(quantity);
                item.setUnitPrice(product.getPrice());
                
                int itemId = saleDAO.insertSaleItem(item, saleId);
                
                if (itemId > 0) {
                    // Update product stock
                    return updateProductStock(productId, -quantity, "SALE", String.valueOf(saleId));
                }
            }
        }
        
        return false;
    }
    
    /**
     * Updates the stock level of a product.
     *
     * @param productId The ID of the product to update
     * @param quantity The quantity to add (positive) or subtract (negative)
     * @param reason The reason for the stock movement
     * @param reference A reference to related document/transaction
     * @return true if the stock update was successful, false otherwise
     */
    private boolean updateProductStock(int productId, int quantity, String reason, String reference) {
        if (!authController.isAuthenticated()) {
            return false;
        }
        
        // Create a stock transaction record
        StockTransaction transaction = new StockTransaction();
        transaction.setProductId(productId);
        transaction.setTransactionType(quantity >= 0 ? "IN" : "OUT");
        transaction.setQuantity(Math.abs(quantity));
        transaction.setReason(reason);
        transaction.setReference(reference);
        transaction.setUserId(authController.getCurrentUser().getId());
        
        // Update the product stock level
        boolean stockUpdated = productDAO.updateStock(productId, quantity);
        
        if (stockUpdated) {
            // Record the transaction
            stockTransactionDAO.insert(transaction);
        }
        
        return stockUpdated;
    }
}