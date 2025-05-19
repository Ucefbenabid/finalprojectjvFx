package dao;


import model.Category;
import java.util.List;

/**
 * Interface for Category data access operations.
 */
public interface CategoryDAO {
    
    /**
     * Inserts a new category into the database.
     *
     * @param category The category to insert
     * @return The ID of the newly inserted category, or -1 if the operation failed
     */
    int insert(Category category);
    
    /**
     * Updates an existing category in the database.
     *
     * @param category The category to update
     * @return true if the update was successful, false otherwise
     */
    boolean update(Category category);
    
    /**
     * Deletes a category from the database.
     *
     * @param id The ID of the category to delete
     * @return true if the deletion was successful, false otherwise
     */
    boolean delete(int id);
    
    /**
     * Retrieves a category by its ID.
     *
     * @param id The ID of the category to retrieve
     * @return The Category object if found, null otherwise
     */
    Category findById(int id);
    
    /**
     * Retrieves all categories.
     *
     * @return A list of all categories
     */
    List<Category> findAll();
    
    /**
     * Retrieves a category by its name.
     *
     * @param name The name to search for
     * @return The Category object if found, null otherwise
     */
    Category findByName(String name);
}