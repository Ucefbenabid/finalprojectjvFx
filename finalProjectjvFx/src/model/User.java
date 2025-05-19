package model;


import java.time.LocalDateTime;

/**
 * Model class representing a user in the inventory system.
 */
public class User {
    private int id;
    private String username;
    private String password; // Hashed password
    private String fullName;
    private String email;
    private String role; // ADMIN, MANAGER, STAFF
    private boolean active;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public User() {
        this.active = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Constructor with essential fields
    public User(String username, String password, String fullName, String email, String role) {
        this();
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    // Full constructor
    public User(int id, String username, String password, String fullName, String email, 
               String role, boolean active, LocalDateTime lastLogin, 
               LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.active = active;
        this.lastLogin = lastLogin;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean hasRole(String roleToCheck) {
        return this.role.equals(roleToCheck);
    }

    public boolean isAdmin() {
        return "ADMIN".equals(this.role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}