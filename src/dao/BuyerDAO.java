package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Map;

import models.Buyer;
import models.User;
import utils.AlertUtils;

public class BuyerDAO extends UserDAO {

	@Override
	public User get(int id) throws SQLException {
	    String query = "SELECT userID, name, email, phone, location FROM User WHERE userID = ?";

	    try (Connection con = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement stmt = con.prepareStatement(query)) {

	        // Set the userID parameter
	        stmt.setInt(1, id);

	        // Execute the query
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                // If user is found, create and return a User object
	                User user = new Buyer();
	                user.setUserID(rs.getInt("userID"));
	                user.setUserName(rs.getString("name"));
	                user.setUserEmail(rs.getString("email"));
	                user.setUserPhoneNumber(rs.getString("phone"));
	                user.setUserLocation(rs.getString("location"));
	                return user;
	            } else {
	                // user does not exist against that ID
	                AlertUtils.showError("User not found", "No user found with ID " + id);
	                return null;
	            }
	        }

	    } catch (SQLException e) {
	        // Handle any exceptions related to database access
	        AlertUtils.showError("Failed to fetch user", "An error occurred while retrieving the user with ID " + id);
	        e.printStackTrace();  
	        return null;
	    }
	}


	@Override
	public Map<Integer, User> getAll() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(User user) throws SQLException {
	    String query = "INSERT INTO User (name, email, password, phone, location, role) VALUES (?, ?, ?, ?, ?, ?)";

	    try (Connection con = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

	        stmt.setString(1, user.getUserName());
	        stmt.setString(2, user.getUserEmail());
	        stmt.setString(3, user.getUserPassword());
	        stmt.setString(4, user.getUserPhoneNumber());
	        stmt.setString(5, user.getUserLocation());
	        stmt.setString(6, user.getUserType());  // Using user.getUserType() instead of hardcoding "Buyer"

	        // Execute the update query
	        int rowsAffected = stmt.executeUpdate();

	        if (rowsAffected > 0) {
	            // Retrieve the generated userID
	            try (ResultSet rs = stmt.getGeneratedKeys()) {
	                if (rs.next()) {
	                    return rs.getInt(1); // Return generated userID
	                }
	            }
	        }

	    } catch (SQLIntegrityConstraintViolationException e) {
	        // Handle duplicate email or phone number
	        AlertUtils.showError("Duplicate Entry", "User with this email or phone already exists.");
	        e.printStackTrace(); // Optional: Log for debugging
	    } catch (SQLException e) {
	        // General SQLException handling
	        AlertUtils.showError("Failed to insert user", "An error occurred while inserting the user: " + e.getMessage());
	        e.printStackTrace(); // Optional: Log for debugging
	    }

	    return -1; // Return -1 in case of failure
	}


	@Override
	public int update(User user) throws SQLException {

	    // Check if user exists before attempting to update
//	    if (exists(user)==-1) {
//	        AlertUtils.showError("User not found", "User with the specified ID does not exist.");
//	        return 0;
//	    }

	    // Get a new database connection using the singleton class
	    try (Connection con = DatabaseConnection.getInstance().getConnection()) {

	        String query = "UPDATE User SET name = ?, email = ?, phone = ?, password = ? WHERE userID = ? AND role = ?";
	        
	        try (PreparedStatement stmt = con.prepareStatement(query)) {
	            stmt.setString(1, user.getUserName());
	            stmt.setString(2, user.getUserEmail());
	            stmt.setString(3, user.getUserPhoneNumber());
	            stmt.setString(4, user.getUserPassword());
	            stmt.setInt(5, user.getUserID());
	            stmt.setString(6, "Buyer");

	            int rowsAffected = stmt.executeUpdate();

	            if (rowsAffected > 0) {
	                AlertUtils.showSuccess("User updated successfully.");
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
