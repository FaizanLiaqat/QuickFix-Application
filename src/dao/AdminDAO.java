package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import models.User;
import utils.AlertUtils;

public class AdminDAO extends UserDAO {

	@Override
	public User get(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Integer, User> getAll() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(User user) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(User user) throws SQLException {
		
		 // Check if user exists before attempting to update
	    
	    // Get a new database connection using the singleton class
	    try (Connection con = DatabaseConnection.getInstance().getConnection()) {

	        String query = "UPDATE User SET password = ? WHERE userID = ? AND role = ?";
	        
	        try (PreparedStatement stmt = con.prepareStatement(query)) {
	            
	            stmt.setString(1, user.getUserPassword());
	            stmt.setInt(2, user.getUserID());
	            stmt.setString(3, "Admin");

	            int rowsAffected = stmt.executeUpdate();

	            if (rowsAffected > 0) {
	                AlertUtils.showSuccess("Password Upadted successfully.");
	            } else {
	                AlertUtils.showError("Update failed", "User update was unsuccessful. Please check the provided data.");
	            }

	            return rowsAffected;
	        } catch (SQLException e) {
	            AlertUtils.showError("Failed to update user", "An error occurred while updating the user: " + e.getMessage());
	            e.printStackTrace(); // Optional: for debugging
	            return 0;
	        }
	    } catch (SQLException e) {
	        AlertUtils.showError("Database connection error", "Unable to establish a database connection: " + e.getMessage());
	        e.printStackTrace(); // Optional: for debugging
	        return 0;
	    }
	}

	@Override
	public int delete(User user) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

}
