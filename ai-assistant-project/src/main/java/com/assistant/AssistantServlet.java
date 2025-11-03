package com.assistant;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/assistant")
public class AssistantServlet extends HttpServlet {
    
    private Gson gson = new Gson();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // Enable CORS
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        
        String action = request.getParameter("action");
        PrintWriter out = response.getWriter();
        
        try (DatabaseManager db = new DatabaseManager()) {
            
            Map<String, Object> result = new HashMap<>();
            
            switch (action != null ? action : "") {
                case "getKnowledge":
                    List<Map<String, Object>> knowledge = db.getKnowledge();
                    result.put("success", true);
                    result.put("data", knowledge);
                    break;
                    
                case "getReminders":
                    List<Map<String, Object>> reminders = db.getReminders();
                    result.put("success", true);
                    result.put("data", reminders);
                    break;
                    
                case "testConnection":
                    result.put("success", db.testConnection());
                    result.put("message", "Database connection OK");
                    break;
                    
                default:
                    result.put("success", false);
                    result.put("message", "Invalid action");
            }
            
            out.print(gson.toJson(result));
            
        } catch (SQLException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Database error: " + e.getMessage());
            out.print(gson.toJson(error));
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // Enable CORS
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        
        PrintWriter out = response.getWriter();
        
        try {
            // Read JSON request body
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            
            JsonObject jsonRequest = gson.fromJson(sb.toString(), JsonObject.class);
            String action = jsonRequest.get("action").getAsString();
            
            Map<String, Object> result = new HashMap<>();
            
            try (DatabaseManager db = new DatabaseManager()) {
                
                switch (action) {
                    case "addKnowledge":
                        String topic = jsonRequest.get("topic").getAsString();
                        String information = jsonRequest.get("information").getAsString();
                        boolean knowledgeAdded = db.addKnowledge(topic, information);
                        
                        result.put("success", knowledgeAdded);
                        result.put("message", knowledgeAdded ? "Knowledge added" : "Failed to add knowledge");
                        break;
                        
                    case "deleteKnowledge":
                        int knowledgeId = jsonRequest.get("id").getAsInt();
                        boolean knowledgeDeleted = db.deleteKnowledge(knowledgeId);
                        
                        result.put("success", knowledgeDeleted);
                        result.put("message", knowledgeDeleted ? "Knowledge deleted" : "Failed to delete knowledge");
                        break;
                        
                    case "addReminder":
                        String message = jsonRequest.get("message").getAsString();
                        long reminderTime = jsonRequest.get("reminderTime").getAsLong();
                        boolean reminderAdded = db.addReminder(message, reminderTime);
                        
                        result.put("success", reminderAdded);
                        result.put("message", reminderAdded ? "Reminder added" : "Failed to add reminder");
                        break;
                        
                    case "deleteReminder":
                        int reminderId = jsonRequest.get("id").getAsInt();
                        boolean reminderDeleted = db.deleteReminder(reminderId);
                        
                        result.put("success", reminderDeleted);
                        result.put("message", reminderDeleted ? "Reminder deleted" : "Failed to delete reminder");
                        break;
                        
                    case "queryAssistant":
                        String query = jsonRequest.get("query").getAsString();
                        String answer = db.searchKnowledge(query);
                        
                        if (answer != null) {
                            result.put("success", true);
                            result.put("response", answer);
                            result.put("source", "database");
                        } else {
                            result.put("success", false);
                            result.put("message", "No matching knowledge found");
                        }
                        break;
                        
                    default:
                        result.put("success", false);
                        result.put("message", "Invalid action");
                }
                
            } catch (SQLException e) {
                result.put("success", false);
                result.put("message", "Database error: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
            
            out.print(gson.toJson(result));
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Server error: " + e.getMessage());
            out.print(gson.toJson(error));
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Handle CORS preflight request
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}