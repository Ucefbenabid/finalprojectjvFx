package dao.impli;


import dao.SaleDAO;
import model.Sale;
import model.SaleItem;
import util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the SaleDAO interface for MySQL database.
 */
public class SaleDAOImpl implements SaleDAO {
    
    private final Connection connection;
    
    public SaleDAOImpl() {
        this.connection = DatabaseConnection.getConnection();
    }
    
    @Override
    public int insert(Sale sale) {
        String sql = "INSERT INTO sales (user_id, customer_name, sale_date, total_amount, " +
                     "status, payment_method, notes, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, sale.getUserId());
            stmt.setString(2, sale.getCustomerName());
            stmt.setTimestamp(3, Timestamp.valueOf(sale.getSaleDate()));
            stmt.setBigDecimal(4, sale.getTotalAmount());
            stmt.setString(5, sale.getStatus());
            stmt.setString(6, sale.getPaymentMethod());
            stmt.setString(7, sale.getNotes());
            stmt.setTimestamp(8, Timestamp.valueOf(sale.getCreatedAt()));
            stmt.setTimestamp(9, Timestamp.valueOf(sale.getUpdatedAt()));
            
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
    public boolean update(Sale sale) {
        String sql = "UPDATE sales SET customer_name = ?, total_amount = ?, status = ?, " +
                     "payment_method = ?, notes = ?, updated_at = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, sale.getCustomerName());
            stmt.setBigDecimal(2, sale.getTotalAmount());
            stmt.setString(3, sale.getStatus());
            stmt.setString(4, sale.getPaymentMethod());
            stmt.setString(5, sale.getNotes());
            stmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(7, sale.getId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "UPDATE sales SET status = 'CANCELLED', updated_at = ? WHERE id = ?";
        
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
    public Sale findById(int id) {
        String sql = "SELECT id, user_id, customer_name, sale_date, total_amount, status, " +
                     "payment_method, notes, created_at, updated_at FROM sales WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSale(rs);
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
    public List<Sale> findAll() {
        String sql = "SELECT id, user_id, customer_name, sale_date, total_amount, status, " +
                     "payment_method, notes, created_at, updated_at FROM sales ORDER BY sale_date DESC";
        
        List<Sale> sales = new ArrayList<>();
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                sales.add(mapResultSetToSale(rs));
            }
            
            return sales;
        } catch (SQLException e) {
            e.printStackTrace();
            return sales;
        }
    }
    
    @Override
    public List<Sale> findByUser(int userId) {
        String sql = "SELECT id, user_id, customer_name, sale_date, total_amount, status, " +
                     "payment_method, notes, created_at, updated_at FROM sales WHERE user_id = ? " +
                     "ORDER BY sale_date DESC";
        
        List<Sale> sales = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sales.add(mapResultSetToSale(rs));
                }
            }
            
            return sales;
        } catch (SQLException e) {
            e.printStackTrace();
            return sales;
        }
    }
    
    @Override
    public List<Sale> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String sql = "SELECT id, user_id, customer_name, sale_date, total_amount, status, " +
                     "payment_method, notes, created_at, updated_at FROM sales " +
                     "WHERE sale_date BETWEEN ? AND ? ORDER BY sale_date DESC";
        
        List<Sale> sales = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(startDate));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sales.add(mapResultSetToSale(rs));
                }
            }
            
            return sales;
        } catch (SQLException e) {
            e.printStackTrace();
            return sales;
        }
    }
    
    @Override
    public List<Sale> findByStatus(String status) {
        String sql = "SELECT id, user_id, customer_name, sale_date, total_amount, status, " +
                     "payment_method, notes, created_at, updated_at FROM sales WHERE status = ? " +
                     "ORDER BY sale_date DESC";
        
        List<Sale> sales = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sales.add(mapResultSetToSale(rs));
                }
            }
            
            return sales;
        } catch (SQLException e) {
            e.printStackTrace();
            return sales;
        }
    }
    
    @Override
    public int insertSaleItem(SaleItem item, int saleId) {
        String sql = "INSERT INTO sale_items (sale_id, product_id, quantity, unit_price, discount) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, saleId);
            stmt.setInt(2, item.getProductId());
            stmt.setInt(3, item.getQuantity());
            stmt.setBigDecimal(4, item.getUnitPrice());
            stmt.setBigDecimal(5, item.getDiscount());
            
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
    public List<SaleItem> findSaleItems(int saleId) {
        String sql = "SELECT si.*, p.name as product_name FROM sale_items si " +
                     "JOIN products p ON si.product_id = p.id WHERE si.sale_id = ?";
        
        List<SaleItem> items = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, saleId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapResultSetToSaleItem(rs));
                }
            }
            
            return items;
        } catch (SQLException e) {
            e.printStackTrace();
            return items;
        }
    }
    
    @Override
    public boolean updateStatus(int saleId, String status) {
        String sql = "UPDATE sales SET status = ?, updated_at = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(3, saleId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private Sale mapResultSetToSale(ResultSet rs) throws SQLException {
        Sale sale = new Sale();
        
        sale.setId(rs.getInt("id"));
        sale.setUserId(rs.getInt("user_id"));
        sale.setCustomerName(rs.getString("customer_name"));
        sale.setSaleDate(rs.getTimestamp("sale_date").toLocalDateTime());
        sale.setTotalAmount(rs.getBigDecimal("total_amount"));
        sale.setStatus(rs.getString("status"));
        sale.setPaymentMethod(rs.getString("payment_method"));
        sale.setNotes(rs.getString("notes"));
        sale.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        sale.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        
        return sale;
    }
    
    private SaleItem mapResultSetToSaleItem(ResultSet rs) throws SQLException {
        SaleItem item = new SaleItem();
        
        item.setId(rs.getInt("id"));
        item.setSaleId(rs.getInt("sale_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setProductName(rs.getString("product_name"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        item.setDiscount(rs.getBigDecimal("discount"));
        
        return item;
    }
}