package util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for database connection management.
 */
public class DatabaseConnection {
    
    private static final String URL = "jdbc:mysql://localhost:3306/inventory_db?createDatabaseIfNotExist=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    private static Connection connection;
    
    /**
     * Gets a connection to the database.
     *
     * @return The database connection
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Load the MySQL JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // Create the connection
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                
                // Initialize the database if needed
                initializeDatabase();
                
                System.out.println("Database connection established successfully");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                System.err.println("Failed to connect to the database: " + e.getMessage());
            }
        }
        
        return connection;
    }
    
    /**
     * Closes the database connection.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Database connection closed successfully");
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Failed to close the database connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Initializes the database by creating necessary tables if they don't exist.
     */
    private static void initializeDatabase() {
        try (Statement stmt = connection.createStatement()) {
            // Create users table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) NOT NULL UNIQUE, " +
                "password VARCHAR(255) NOT NULL, " +
                "full_name VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) NOT NULL, " +
                "role VARCHAR(20) NOT NULL, " +
                "active BOOLEAN DEFAULT true, " +
                "last_login TIMESTAMP NULL, " +
                "created_at TIMESTAMP NOT NULL, " +
                "updated_at TIMESTAMP NOT NULL" +
                ")"
            );
            
            // Create categories table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS categories (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50) NOT NULL UNIQUE, " +
                "description TEXT, " +
                "active BOOLEAN DEFAULT true, " +
                "created_at TIMESTAMP NOT NULL, " +
                "updated_at TIMESTAMP NOT NULL" +
                ")"
            );
            
            // Create products table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS products (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "description TEXT, " +
                "sku VARCHAR(50) NOT NULL UNIQUE, " +
                "price DECIMAL(10, 2) NOT NULL, " +
                "category_id INT, " +
                "current_stock INT NOT NULL DEFAULT 0, " +
                "reorder_level INT NOT NULL DEFAULT 0, " +
                "active BOOLEAN DEFAULT true, " +
                "created_at TIMESTAMP NOT NULL, " +
                "updated_at TIMESTAMP NOT NULL, " +
                "FOREIGN KEY (category_id) REFERENCES categories(id)" +
                ")"
            );
            
            // Create sales table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS sales (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "customer_name VARCHAR(100), " +
                "sale_date TIMESTAMP NOT NULL, " +
                "total_amount DECIMAL(10, 2) NOT NULL, " +
                "status VARCHAR(20) NOT NULL, " +
                "payment_method VARCHAR(20) NOT NULL, " +
                "notes TEXT, " +
                "created_at TIMESTAMP NOT NULL, " +
                "updated_at TIMESTAMP NOT NULL, " +
                "FOREIGN KEY (user_id) REFERENCES users(id)" +
                ")"
            );
            
            // Create sale_items table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS sale_items (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "sale_id INT NOT NULL, " +
                "product_id INT NOT NULL, " +
                "quantity INT NOT NULL, " +
                "unit_price DECIMAL(10, 2) NOT NULL, " +
                "discount DECIMAL(5, 2) DEFAULT 0, " +
                "FOREIGN KEY (sale_id) REFERENCES sales(id), " +
                "FOREIGN KEY (product_id) REFERENCES products(id)" +
                ")"
            );
            
            // Create stock_transactions table
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS stock_transactions (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "product_id INT NOT NULL, " +
                "transaction_type VARCHAR(10) NOT NULL, " +
                "quantity INT NOT NULL, " +
                "reason VARCHAR(20) NOT NULL, " +
                "reference VARCHAR(50), " +
                "user_id INT NOT NULL, " +
                "notes TEXT, " +
                "transaction_date TIMESTAMP NOT NULL, " +
                "created_at TIMESTAMP NOT NULL, " +
                "FOREIGN KEY (product_id) REFERENCES products(id), " +
                "FOREIGN KEY (user_id) REFERENCES users(id)" +
                ")"
            );
            
            // Check if admin user exists, create if not
            stmt.executeUpdate(
                "INSERT IGNORE INTO users (username, password, full_name, email, role, active, created_at, updated_at) " +
                "VALUES ('admin', '$2a$10$WNtxINX0iGOa0.1FQn9H4.zJ.2N8PYVO92VnCPZtRJ8t7i7EUAmy2', 'System Administrator', 'admin@example.com', 'ADMIN', true, NOW(), NOW())"
            );
            
            // Insert some default categories
            stmt.executeUpdate(
                "INSERT IGNORE INTO categories (name, description, active, created_at, updated_at) VALUES " +
                "('Electronics', 'Electronic devices and accessories', true, NOW(), NOW()), " +
                "('Office Supplies', 'Office stationery and supplies', true, NOW(), NOW()), " +
                "('Furniture', 'Office and home furniture', true, NOW(), NOW())"
            );
            
            System.out.println("Database initialized successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Failed to initialize the database: " + e.getMessage());
        }
    }
}