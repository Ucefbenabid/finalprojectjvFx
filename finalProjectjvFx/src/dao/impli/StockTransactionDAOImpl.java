package dao.impli;


import dao.StockTransactionDAO;
import model.StockTransaction;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the StockTransactionDAO interface for MySQL database.
 */
public class StockTransactionDAOImpl implements StockTransactionDAO {
    
    private final Connection connection;
    
    public StockTransactionDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    @Override
    public int insert(StockTransaction transaction) {
        String sql = "INSERT INTO stock_transactions (product_id, transaction_type, quantity, " +
                     "reason, reference, user_id, notes, transaction_date, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, transaction.getProductId());
            stmt.setString(2, transaction.getTransactionType());
            stmt.setInt(3, transaction.getQuantity());
            stmt.setString(4, transaction.getReason());
            stmt.setString(5, transaction.getReference());
            stmt.setInt(6, transaction.getUserId());
            stmt.setString(7, transaction.getNotes());
            stmt.setTimestamp(8, Timestamp.valueOf(transaction.getTransactionDate()));
            stmt.setTimestamp(9, Timestamp.valueOf(transaction.getCreatedAt()));
            
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
    public StockTransaction findById(int id) {
        String sql = "SELECT st.*, p.name as product_name FROM stock_transactions st " +
                     "JOIN products p ON st.product_id = p.id WHERE st.id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStockTransaction(rs);
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
    public List<StockTransaction> findAll() {
        String sql = "SELECT st.*, p.name as product_name FROM stock_transactions st " +
                     "JOIN products p ON st.product_id = p.id ORDER BY transaction_date DESC";
        
        List<StockTransaction> transactions = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                transactions.add(mapResultSetToStockTransaction(rs));
            }
            
            return transactions;
        } catch (SQLException e) {
            e.printStackTrace();
            return transactions;
        }
    }
    
    @Override
    public List<StockTransaction> findByProduct(int productId) {
        String sql = "SELECT st.*, p.name as product_name FROM stock_transactions st " +
                     "JOIN products p ON st.product_id = p.id WHERE st.product_id = ? " +
                     "ORDER BY transaction_date DESC";
        
        List<StockTransaction> transactions = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToStockTransaction(rs));
                }
            }
            
            return transactions;
        } catch (SQLException e) {
            e.printStackTrace();
            return transactions;
        }
    }
    
    @Override
    public List<StockTransaction> findByType(String type) {
        String sql = "SELECT st.*, p.name as product_name FROM stock_transactions st " +
                     "JOIN products p ON st.product_id = p.id WHERE st.transaction_type = ? " +
                     "ORDER BY transaction_date DESC";
        
        List<StockTransaction> transactions = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, type);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToStockTransaction(rs));
                }
            }
            
            return transactions;
        } catch (SQLException e) {
            e.printStackTrace();
            return transactions;
        }
    }
    
    @Override
    public List<StockTransaction> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT st.*, p.name as product_name FROM stock_transactions st " +
                     "JOIN products p ON st.product_id = p.id " +
                     "WHERE st.transaction_date BETWEEN ? AND ? ORDER BY transaction_date DESC";
        
        List<StockTransaction> transactions = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(startDate));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToStockTransaction(rs));
                }
            }
            
            return transactions;
        } catch (SQLException e) {
            e.printStackTrace();
            return transactions;
        }
    }
    
    @Override
    public List<StockTransaction> findByUser(int userId) {
        String sql = "SELECT st.*, p.name as product_name FROM stock_transactions st " +
                     "JOIN products p ON st.product_id = p.id WHERE st.user_id = ? " +
                     "ORDER BY transaction_date DESC";
        
        List<StockTransaction> transactions = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToStockTransaction(rs));
                }
            }
            
            return transactions;
        } catch (SQLException e) {
            e.printStackTrace();
            return transactions;
        }
    }
    
    @Override
    public List<StockTransaction> findByReason(String reason) {
        String sql = "SELECT st.*, p.name as product_name FROM stock_transactions st " +
                     "JOIN products p ON st.product_id = p.id WHERE st.reason = ? " +
                     "ORDER BY transaction_date DESC";
        
        List<StockTransaction> transactions = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, reason);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToStockTransaction(rs));
                }
            }
            
            return transactions;
        } catch (SQLException e) {
            e.printStackTrace();
            return transactions;
        }
    }
    
    private StockTransaction mapResultSetToStockTransaction(ResultSet rs) throws SQLException {
        StockTransaction transaction = new StockTransaction();
        
        transaction.setId(rs.getInt("id"));
        transaction.setProductId(rs.getInt("product_id"));
        transaction.setProductName(rs.getString("product_name"));
        transaction.setTransactionType(rs.getString("transaction_type"));
        transaction.setQuantity(rs.getInt("quantity"));
        transaction.setReason(rs.getString("reason"));
        transaction.setReference(rs.getString("reference"));
        transaction.setUserId(rs.getInt("user_id"));
        transaction.setNotes(rs.getString("notes"));
        transaction.setTransactionDate(rs.getTimestamp("transaction_date").toLocalDateTime());
        transaction.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        
        return transaction;
    }
}