package dao;


import model.StockTransaction;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface for StockTransaction data access operations.
 */
public interface StockTransactionDAO {
    
    /**
     * Inserts a new stock transaction into the database.
     *
     * @param transaction The stock transaction to insert
     * @return The ID of the newly inserted transaction, or -1 if the operation failed
     */
    int insert(StockTransaction transaction);
    
    /**
     * Retrieves a stock transaction by its ID.
     *
     * @param id The ID of the stock transaction to retrieve
     * @return The StockTransaction object if found, null otherwise
     */
    StockTransaction findById(int id);
    
    /**
     * Retrieves all stock transactions.
     *
     * @return A list of all stock transactions
     */
    List<StockTransaction> findAll();
    
    /**
     * Retrieves stock transactions by product.
     *
     * @param productId The product ID to filter by
     * @return A list of stock transactions for the specified product
     */
    List<StockTransaction> findByProduct(int productId);
    
    /**
     * Retrieves stock transactions by transaction type.
     *
     * @param type The transaction type to filter by
     * @return A list of stock transactions with the specified type
     */
    List<StockTransaction> findByType(String type);
    
    /**
     * Retrieves stock transactions within a date range.
     *
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of stock transactions made within the specified date range
     */
    List<StockTransaction> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Retrieves stock transactions by user.
     *
     * @param userId The user ID to filter by
     * @return A list of stock transactions made by the specified user
     */
    List<StockTransaction> findByUser(int userId);
    
    /**
     * Retrieves stock transactions by reason.
     *
     * @param reason The reason to filter by
     * @return A list of stock transactions with the specified reason
     */
    List<StockTransaction> findByReason(String reason);
}