package dao.impli;



import dao.ProductDAO;

import model.Product;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the ProductDAO interface for MySQL database.
 */
public class ProductDAOImpl implements ProductDAO {
    
    private final Connection connection;
    
    public ProductDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    @Override
    public int insert(Product product) {
        String sql = "INSERT INTO products (name, description, sku, price, category_id, " +
                     "current_stock, reorder_level, active, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setString(3, product.getSku());
            stmt.setBigDecimal(4, product.getPrice());
            stmt.setInt(5, product.getCategoryId());
            stmt.setInt(6, product.getCurrentStock());
            stmt.setInt(7, product.getReorderLevel());
            stmt.setBoolean(8, product.isActive());
            stmt.setTimestamp(9, Timestamp.valueOf(product.getCreatedAt()));
            stmt.setTimestamp(10, Timestamp.valueOf(product.getUpdatedAt()));
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                return -1;
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    return -1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    @Override
    public boolean update(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, sku = ?, price = ?, " +
                     "category_id = ?, reorder_level = ?, active = ?, updated_at = ? " +
                     "WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setString(3, product.getSku());
            stmt.setBigDecimal(4, product.getPrice());
            stmt.setInt(5, product.getCategoryId());
            stmt.setInt(6, product.getReorderLevel());
            stmt.setBoolean(7, product.isActive());
            stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(9, product.getId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "UPDATE products SET active = false, updated_at = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(2, id);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public Product findById(int id) {
        String sql = "SELECT p.*, c.name as category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "WHERE p.id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Product findBySku(String sku) {
        String sql = "SELECT p.*, c.name as category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "WHERE p.sku = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, sku);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public List<Product> findAll() {
        String sql = "SELECT p.*, c.name as category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "ORDER BY p.name";
        
        List<Product> products = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
            
            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            return products;
        }
    }
    
    @Override
    public List<Product> findByCategory(int categoryId) {
        String sql = "SELECT p.*, c.name as category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "WHERE p.category_id = ? " +
                     "ORDER BY p.name";
        
        List<Product> products = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, categoryId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
            
            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            return products;
        }
    }
    
    @Override
    public List<Product> search(String query) {
        String sql = "SELECT p.*, c.name as category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "WHERE p.name LIKE ? OR p.description LIKE ? OR p.sku LIKE ? " +
                     "ORDER BY p.name";
        
        List<Product> products = new ArrayList<>();
        String searchPattern = "%" + query + "%";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
            
            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            return products;
        }
    }
    
    @Override
    public boolean updateStock(int productId, int quantityChange) {
        String sql = "UPDATE products SET current_stock = current_stock + ?, updated_at = ? " +
                     "WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, quantityChange);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(3, productId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<Product> findLowStockProducts() {
        String sql = "SELECT p.*, c.name as category_name FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.id " +
                     "WHERE p.current_stock <= p.reorder_level AND p.active = true " +
                     "ORDER BY p.name";
        
        List<Product> products = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
            
            return products;
        } catch (SQLException e) {
            e.printStackTrace();
            return products;
        }
    }
    
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setSku(rs.getString("sku"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setCategoryId(rs.getInt("category_id"));
        product.setCategoryName(rs.getString("category_name"));
        product.setCurrentStock(rs.getInt("current_stock"));
        product.setReorderLevel(rs.getInt("reorder_level"));
        product.setActive(rs.getBoolean("active"));
        product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        product.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        
        return product;
    }


}