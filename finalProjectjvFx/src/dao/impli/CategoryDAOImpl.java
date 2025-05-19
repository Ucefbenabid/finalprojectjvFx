package dao.impli;


import dao.CategoryDAO;
import model.Category;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the CategoryDAO interface for MySQL database.
 */
public class CategoryDAOImpl implements CategoryDAO {
    
    private final Connection connection;
    
    public CategoryDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    @Override
    public int insert(Category category) {
        String sql = "INSERT INTO categories (name, description, active, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setBoolean(3, category.isActive());
            stmt.setTimestamp(4, Timestamp.valueOf(category.getCreatedAt()));
            stmt.setTimestamp(5, Timestamp.valueOf(category.getUpdatedAt()));
            
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
    public boolean update(Category category) {
        String sql = "UPDATE categories SET name = ?, description = ?, active = ?, updated_at = ? " +
                     "WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setBoolean(3, category.isActive());
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(5, category.getId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "UPDATE categories SET active = false, updated_at = ? WHERE id = ?";
        
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
    public Category findById(int id) {
        String sql = "SELECT id, name, description, active, created_at, updated_at " +
                     "FROM categories WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategory(rs);
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
    public List<Category> findAll() {
        String sql = "SELECT id, name, description, active, created_at, updated_at " +
                     "FROM categories ORDER BY name";
        
        List<Category> categories = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }
            
            return categories;
        } catch (SQLException e) {
            e.printStackTrace();
            return categories;
        }
    }
    
    @Override
    public Category findByName(String name) {
        String sql = "SELECT id, name, description, active, created_at, updated_at " +
                     "FROM categories WHERE name = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategory(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        
        category.setId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        category.setActive(rs.getBoolean("active"));
        category.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        category.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        
        return category;
    }
}