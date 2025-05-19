package controller;

import dao.UserDAO;
import dao.impli.UserDAOImpl;
import model.User;
import view.DashboardView;
import javafx.stage.Stage;

/**
 * Controller for authentication operations.
 */
public class AuthController {
    
    private final UserDAO userDAO;
    private User currentUser;
    
    public AuthController() {
        this.userDAO = new UserDAOImpl();
    }
    
    /**
     * Attempts to authenticate a user with the provided credentials.
     *
     * @param username The username
     * @param password The password
     * @return true if authentication was successful, false otherwise
     */
    public boolean login(String username, String password, Stage primaryStage) {
        User user = userDAO.authenticate(username, password);
        
        if (user != null) {
            this.currentUser = user;
            
            // Navigate to dashboard view
            DashboardView dashboardView = new DashboardView(this, primaryStage);
            dashboardView.show();
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Logs out the current user.
     *
     * @return true if logout was successful, false otherwise
     */
    public boolean logout(Stage primaryStage) {
        this.currentUser = null;
        // Return to login view
        return true;
    }
    
    /**
     * Gets the currently authenticated user.
     *
     * @return The current User object, or null if no user is authenticated
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Checks if a user is currently authenticated.
     *
     * @return true if a user is authenticated, false otherwise
     */
    public boolean isAuthenticated() {
        return currentUser != null;
    }
    
    /**
     * Checks if the current user has the specified role.
     *
     * @param role The role to check for
     * @return true if the user has the role, false otherwise
     */
    public boolean hasRole(String role) {
        return isAuthenticated() && currentUser.hasRole(role);
    }
    
    /**
     * Checks if the current user is an administrator.
     *
     * @return true if the user is an admin, false otherwise
     */
    public boolean isAdmin() {
        return isAuthenticated() && currentUser.isAdmin();
    }
}