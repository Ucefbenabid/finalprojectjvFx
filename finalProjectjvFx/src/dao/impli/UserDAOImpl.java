package dao.impli;


import dao.UserDAO;
import model.User;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the UserDAO interface for MySQL database.
 */
public class UserDAOImpl implements UserDAO {
    
    private final Connection connection;
    
    public UserDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    @Override
    public int insert(User user) {
        String sql = "INSERT INTO users (username, password, full_name, email, role, active, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword()); // Store password as plain text
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getRole());
            stmt.setBoolean(6, user.isActive());
            stmt.setTimestamp(7, Timestamp.valueOf(user.getCreatedAt()));
            stmt.setTimestamp(8, Timestamp.valueOf(user.getUpdatedAt()));
            
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
    public boolean update(User user) {
        String sql = "UPDATE users SET username = ?, full_name = ?, email = ?, role = ?, " +
                     "active = ?, updated_at = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getFullName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getRole());
            stmt.setBoolean(5, user.isActive());
            stmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(7, user.getId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "UPDATE users SET active = false, updated_at = ? WHERE id = ?";
        
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
    public User findById(int id) {
        String sql = "SELECT id, username, password, full_name, email, role, active, " +
                     "last_login, created_at, updated_at FROM users WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
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
    public User findByUsername(String username) {
        String sql = "SELECT id, username, password, full_name, email, role, active, " +
                     "last_login, created_at, updated_at FROM users WHERE username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
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
    public List<User> findAll() {
        String sql = "SELECT id, username, password, full_name, email, role, active, " +
                     "last_login, created_at, updated_at FROM users ORDER BY username";
        
        List<User> users = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
            return users;
        }
    }
    
    @Override
    public User authenticate(String username, String password) {
        User user = findByUsername(username);
        
        if (user != null && password.equals(user.getPassword())) {
            updateLastLogin(user.getId());
            return user;
        }
        
        return null;
    }
    
    @Override
    public boolean updateLastLogin(int userId) {
        String sql = "UPDATE users SET last_login = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(2, userId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ?, updated_at = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newPassword); // Store password as plain text
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(3, userId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));
        user.setActive(rs.getBoolean("active"));
        
        Timestamp lastLogin = rs.getTimestamp("last_login");
        if (lastLogin != null) {
            user.setLastLogin(lastLogin.toLocalDateTime());
        }
        
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        
        return user;
    }
}