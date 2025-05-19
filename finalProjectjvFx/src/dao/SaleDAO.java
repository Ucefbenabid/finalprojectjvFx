package dao;


import model.Sale;
import model.SaleItem;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface for Sale data access operations.
 */
public interface SaleDAO {
    
    /**
     * Inserts a new sale into the database.
     *
     * @param sale The sale to insert
     * @return The ID of the newly inserted sale, or -1 if the operation failed
     */
    int insert(Sale sale);
    
    /**
     * Updates an existing sale in the database.
     *
     * @param sale The sale to update
     * @return true if the update was successful, false otherwise
     */
    boolean update(Sale sale);
    
    /**
     * Deletes a sale from the database.
     *
     * @param id The ID of the sale to delete
     * @return true if the deletion was successful, false otherwise
     */
    boolean delete(int id);
    
    /**
     * Retrieves a sale by its ID.
     *
     * @param id The ID of the sale to retrieve
     * @return The Sale object if found, null otherwise
     */
    Sale findById(int id);
    
    /**
     * Retrieves all sales.
     *
     * @return A list of all sales
     */
    List<Sale> findAll();
    
    /**
     * Retrieves sales by user.
     *
     * @param userId The user ID to filter by
     * @return A list of sales made by the specified user
     */
    List<Sale> findByUser(int userId);
    
    /**
     * Retrieves sales within a date range.
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of sales made within the specified date range
     */
    List<Sale> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Retrieves sales by status.
     *
     * @param status The status to filter by
     * @return A list of sales with the specified status
     */
    List<Sale> findByStatus(String status);
    
    /**
     * Inserts a sale item into the database.
     *
     * @param saleItem The sale item to insert
     * @param saleId The ID of the sale to which the item belongs
     * @return The ID of the newly inserted sale item, or -1 if the operation failed
     */
    int insertSaleItem(SaleItem saleItem, int saleId);
    
    /**
     * Retrieves all items for a sale.
     *
     * @param saleId The ID of the sale
     * @return A list of items for the specified sale
     */
    List<SaleItem> findSaleItems(int saleId);
    
    /**
     * Updates the status of a sale.
     *
     * @param saleId The ID of the sale to update
     * @param status The new status
     * @return true if the update was successful, false otherwise
     */
    boolean updateStatus(int saleId, String status);
}