package com.assistant;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseManager implements AutoCloseable {
    // Database configuration - UPDATE THESE VALUES!
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ai_assistant?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";  // Change to your MySQL username
    private static final String DB_PASSWORD = "nikhil12";  // Change to your MySQL password
    
    private Connection connection;
    
    // Constructor - establishes database connection
    public DatabaseManager() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connected successfully!");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }
    
    // Add knowledge to database
    public boolean addKnowledge(String topic, String information) {
        String sql = "INSERT INTO knowledge (topic, information) VALUES (?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, topic);
            stmt.setString(2, information);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding knowledge: " + e.getMessage());
            return false;
        }
    }
    
    // Get all knowledge from database
    public List<Map<String, Object>> getKnowledge() {
        List<Map<String, Object>> knowledgeList = new ArrayList<>();
        String sql = "SELECT id, topic, information, timestamp FROM knowledge ORDER BY timestamp DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", rs.getInt("id"));
                item.put("topic", rs.getString("topic"));
                item.put("information", rs.getString("information"));
                item.put("timestamp", rs.getTimestamp("timestamp").getTime());
                knowledgeList.add(item);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting knowledge: " + e.getMessage());
        }
        
        return knowledgeList;
    }
    
    // Search knowledge by topic
    public String searchKnowledge(String query) {
        String sql = "SELECT information FROM knowledge WHERE LOWER(topic) LIKE ? OR LOWER(information) LIKE ? LIMIT 1";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchTerm = "%" + query.toLowerCase() + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("information");
            }
            
        } catch (SQLException e) {
            System.err.println("Error searching knowledge: " + e.getMessage());
        }
        
        return null;
    }
    
    // Delete knowledge by ID
    public boolean deleteKnowledge(int id) {
        String sql = "DELETE FROM knowledge WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting knowledge: " + e.getMessage());
            return false;
        }
    }
    
    // Add reminder to database
    public boolean addReminder(String message, long reminderTime) {
        String sql = "INSERT INTO reminders (message, reminder_time) VALUES (?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, message);
            stmt.setLong(2, reminderTime);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding reminder: " + e.getMessage());
            return false;
        }
    }
    
    // Get all reminders from database
    public List<Map<String, Object>> getReminders() {
        List<Map<String, Object>> reminderList = new ArrayList<>();
        String sql = "SELECT id, message, reminder_time, created_at FROM reminders ORDER BY reminder_time ASC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", rs.getInt("id"));
                item.put("message", rs.getString("message"));
                item.put("reminderTime", rs.getLong("reminder_time"));
                item.put("createdAt", rs.getTimestamp("created_at").getTime());
                reminderList.add(item);
            }
            
        } catch (SQLException e) {
            System.err.println("Error getting reminders: " + e.getMessage());
        }
        
        return reminderList;
    }
    
    // Delete reminder by ID
    public boolean deleteReminder(int id) {
        String sql = "DELETE FROM reminders WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting reminder: " + e.getMessage());
            return false;
        }
    }
    
    
    // Test database connection
    public boolean testConnection() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
         
        }
    }
    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}