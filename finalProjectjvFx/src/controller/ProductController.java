package controller;



import dao.CategoryDAO;
import dao.ProductDAO;
import dao.StockTransactionDAO;
import dao.impli.CategoryDAOImpl;
import dao.impli.ProductDAOImpl;
import dao.impli.StockTransactionDAOImpl;
import model.Category;
import model.Product;
import model.StockTransaction;
import model.User;

import java.util.List;

/**
 * Controller for product and inventory management operations.
 */
public class ProductController {
    
    private final ProductDAO productDAO;
    private final CategoryDAO categoryDAO;
    private final StockTransactionDAO stockTransactionDAO;
    private final AuthController authController;
    
    public ProductController(AuthController authController) {
        this.productDAO = new ProductDAOImpl();
        this.categoryDAO = new CategoryDAOImpl();
        this.stockTransactionDAO = new StockTransactionDAOImpl();
        this.authController = authController;
    }
    
    /**
     * Creates a new product.
     *
     * @param product The product to create
     * @return The ID of the newly created product, or -1 if the operation failed
     */
    public int createProduct(Product product) {
        return productDAO.insert(product);
    }
    
    /**
     * Updates an existing product.
     *
     * @param product The product to update
     * @return true if the update was successful, false otherwise
     */
    public boolean updateProduct(Product product) {
        return productDAO.update(product);
    }
    
    /**
     * Deletes a product.
     *
     * @param productId The ID of the product to delete
     * @return true if the deletion was successful, false otherwise
     */
    public boolean deleteProduct(int productId) {
        return productDAO.delete(productId);
    }
    
    /**
     * Retrieves a product by its ID.
     *
     * @param productId The ID of the product to retrieve
     * @return The Product object if found, null otherwise
     */
    public Product getProduct(int productId) {
        return productDAO.findById(productId);
    }
    
    /**
     * Retrieves all products.
     *
     * @return A list of all products
     */
    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }
    
    /**
     * Searches for products matching the query.
     *
     * @param query The search query
     * @return A list of products matching the search criteria
     */
    public List<Product> searchProducts(String query) {
        return productDAO.search(query);
    }
    
    /**
     * Retrieves products by category.
     *
     * @param categoryId The category ID to filter by
     * @return A list of products in the specified category
     */
    public List<Product> getProductsByCategory(int categoryId) {
        return productDAO.findByCategory(categoryId);
    }
    
    /**
     * Updates the stock level of a product.
     *
     * @param productId The ID of the product to update
     * @param quantity The quantity to add (positive) or subtract (negative)
     * @param reason The reason for the stock movement
     * @param reference A reference to related document/transaction
     * @param notes Additional notes
     * @return true if the stock update was successful, false otherwise
     */
    public boolean updateStock(int productId, int quantity, String reason, String reference, String notes) {
        if (!authController.isAuthenticated()) {
            return false;
        }
        
        User currentUser = authController.getCurrentUser();
        
        // Create a stock transaction record
        StockTransaction transaction = new StockTransaction();
        transaction.setProductId(productId);
        transaction.setTransactionType(quantity >= 0 ? "IN" : "OUT");
        transaction.setQuantity(Math.abs(quantity));
        transaction.setReason(reason);
        transaction.setReference(reference);
        transaction.setUserId(currentUser.getId());
        transaction.setNotes(notes);
        
        // Update the product stock level
        boolean stockUpdated = productDAO.updateStock(productId, quantity);
        
        if (stockUpdated) {
            // Record the transaction
            stockTransactionDAO.insert(transaction);
        }
        
        return stockUpdated;
    }
    
    /**
     * Retrieves products with stock levels below their reorder levels.
     *
     * @return A list of products that need to be restocked
     */
    public List<Product> getLowStockProducts() {
        return productDAO.findLowStockProducts();
    }
    
    /**
     * Creates a new category.
     *
     * @param category The category to create
     * @return The ID of the newly created category, or -1 if the operation failed
     */
    public int createCategory(Category category) {
        return categoryDAO.insert(category);
    }
    
    /**
     * Updates an existing category.
     *
     * @param category The category to update
     * @return true if the update was successful, false otherwise
     */
    public boolean updateCategory(Category category) {
        return categoryDAO.update(category);
    }
    
    /**
     * Deletes a category.
     *
     * @param categoryId The ID of the category to delete
     * @return true if the deletion was successful, false otherwise
     */
    public boolean deleteCategory(int categoryId) {
        return categoryDAO.delete(categoryId);
    }
    
    /**
     * Retrieves a category by its ID.
     *
     * @param categoryId The ID of the category to retrieve
     * @return The Category object if found, null otherwise
     */
    public Category getCategory(int categoryId) {
        return categoryDAO.findById(categoryId);
    }
    
    /**
     * Retrieves all categories.
     *
     * @return A list of all categories
     */
    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }
    
    /**
     * Retrieves stock transactions for a specific product.
     *
     * @param productId The ID of the product
     * @return A list of stock transactions for the specified product
     */
    public List<StockTransaction> getStockTransactionsForProduct(int productId) {
        return stockTransactionDAO.findByProduct(productId);
    }
}