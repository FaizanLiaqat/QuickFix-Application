package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.User;
import utils.AlertUtils;

public abstract class UserDAO implements DAO<User> {
	
	public boolean exists(User user) throws SQLException{
		
		// Update query to check by email instead of name
	    String query = "SELECT userID FROM User WHERE email = ? AND password = ?";

	    try (Connection con = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement stmt = con.prepareStatement(query)) {
	        
	        stmt.setString(1, user.getUserEmail()); // Use email instead of name
	        stmt.setString(2, user.getUserPassword()); // Use password
	        
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                // If the user is found in the database, show success alert and return true
	                AlertUtils.showSuccess("Login successful! Welcome back, " + user.getUserEmail());
	                return true;
	            } else {
	                // If no user found in the database, show error alert
	                AlertUtils.showError("Login Failed", "Invalid email or password.");
	                return false;
	            }
	        }
	    } catch (SQLException e) {
	        // Handle database connection errors
	        e.printStackTrace();
	        AlertUtils.showError("Database Error", "Failed to connect to the database.");
	        return false;
	    }
	}
	public boolean emailExists(User user) throws SQLException{
		
		// Update query to check by email instead of name
	    String query = "SELECT userID FROM User WHERE email = ?  AND role = ?";

	    try (Connection con = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement stmt = con.prepareStatement(query)) {
	        
	        stmt.setString(1, user.getUserEmail()); // Use email instead of name
	        stmt.setString(2, user.getUserType());
	        
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                // If the user is found in the database, show success alert and return true
	                //AlertUtils.showSuccess("Login successful! Welcome back, " + user.getUserEmail());
	                return true;
	            } else {
	                // If no user found in the database, show error alert
	                // AlertUtils.showError("Login Failed", "Invalid email or password.");
	                return false;
	            }
	        }
	    } catch (SQLException e) {
	        // Handle database connection errors
	        e.printStackTrace();
	        AlertUtils.showError("Database Error", "Failed to connect to the database.");
	        return false;
	    }
	}

}
