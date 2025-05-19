package dao;


import model.Product;
import java.util.List;

/**
 * Interface for Product data access operations.
 */
public interface ProductDAO {
    
    /**
     * Inserts a new product into the database.
     *
     * @param product The product to insert
     * @return The ID of the newly inserted product, or -1 if the operation failed
     */
    int insert(Product product);
    
    /**
     * Updates an existing product in the database.
     *
     * @param product The product to update
     * @return true if the update was successful, false otherwise
     */
    boolean update(Product product);
    
    /**
     * Deletes a product from the database.
     *
     * @param id The ID of the product to delete
     * @return true if the deletion was successful, false otherwise
     */
    boolean delete(int id);
    
    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve
     * @return The Product object if found, null otherwise
     */
    Product findById(int id);
    
    /**
     * Retrieves a product by its SKU.
     *
     * @param sku The SKU to search for
     * @return The Product object if found, null otherwise
     */
    Product findBySku(String sku);
    
    /**
     * Retrieves all products.
     *
     * @return A list of all products
     */
    List<Product> findAll();
    
    /**
     * Retrieves products by category.
     *
     * @param categoryId The category ID to filter by
     * @return A list of products in the specified category
     */
    List<Product> findByCategory(int categoryId);
    
    /**
     * Searches for products by name or description.
     *
     * @param query The search query
     * @return A list of products matching the search criteria
     */
    List<Product> search(String query);
    
    /**
     * Updates the stock level of a product.
     *
     * @param productId The ID of the product to update
     * @param quantityChange The quantity to add (positive) or subtract (negative)
     * @return true if the update was successful, false otherwise
     */
    boolean updateStock(int productId, int quantityChange);
    
    /**
     * Retrieves products with stock levels below their reorder levels.
     *
     * @return A list of products that need to be restocked
     */
    List<Product> findLowStockProducts();
}