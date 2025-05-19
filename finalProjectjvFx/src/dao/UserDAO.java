package dao;


import model.User;
import java.util.List;

/**
 * Interface for User data access operations.
 */
public interface UserDAO {
    
    /**
     * Inserts a new user into the database.
     *
     * @param user The user to insert
     * @return The ID of the newly inserted user, or -1 if the operation failed
     */
    int insert(User user);
    
    /**
     * Updates an existing user in the database.
     *
     * @param user The user to update
     * @return true if the update was successful, false otherwise
     */
    boolean update(User user);
    
    /**
     * Deletes a user from the database.
     *
     * @param id The ID of the user to delete
     * @return true if the deletion was successful, false otherwise
     */
    boolean delete(int id);
    
    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve
     * @return The User object if found, null otherwise
     */
    User findById(int id);
    
    /**
     * Retrieves a user by their username.
     *
     * @param username The username to search for
     * @return The User object if found, null otherwise
     */
    User findByUsername(String username);
    
    /**
     * Retrieves all users.
     *
     * @return A list of all users
     */
    List<User> findAll();
    
    /**
     * Authenticates a user by their username and password.
     *
     * @param username The username to authenticate
     * @param password The password to verify
     * @return The authenticated User object if successful, null otherwise
     */
    User authenticate(String username, String password);
    
    /**
     * Updates the last login timestamp for a user.
     *
     * @param userId The ID of the user to update
     * @return true if the update was successful, false otherwise
     */
    boolean updateLastLogin(int userId);

	boolean updatePassword(int userId, String newPassword);
}