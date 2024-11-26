package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;

import models.Admin;
import models.User;
import utils.AlertUtils;

public class AdminDAO extends UserDAO {

	@Override
	public User get(int id) {
	    String query = """
	            SELECT u.*, a.balance
	            FROM User u
	            JOIN Admin a ON u.userID = a.adminID
	            WHERE u.userID = ? AND u.role = 'Admin'
	            """;

	    Admin admin = null;

	    try (Connection con = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement stmt = con.prepareStatement(query)) {

	        stmt.setInt(1, id);

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                // Create an Admin object from the result set
	                admin = new Admin();
	                admin.setUserID(rs.getInt("userID"));
	                admin.setAmount(rs.getBigDecimal("balance")); // Admin-specific field from Admin table
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Failed to retrieve admin with ID " + id + ": " + e.getMessage());
	        e.printStackTrace(); // Optional: useful for debugging during development
	    }

	    return admin; // Return the admin object, or null if not found
	}

	@Override
	public Map<Integer, User> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(User user) {
		
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
	
	public boolean updateAllAdminBalances(BigDecimal amount) throws SQLException {
	    String selectAdminsQuery = "SELECT userID FROM user WHERE role = 'Admin'";
	    String selectBalanceQuery = "SELECT balance FROM admin WHERE adminID = ?";
	    String updateBalanceQuery = "UPDATE admin SET balance = ? WHERE adminID = ?";

	    try (Connection con = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement selectAdminsStmt = con.prepareStatement(selectAdminsQuery);
	         PreparedStatement selectBalanceStmt = con.prepareStatement(selectBalanceQuery);
	         PreparedStatement updateBalanceStmt = con.prepareStatement(updateBalanceQuery)) {

	        // Retrieve all admin userIDs
	        try (ResultSet adminUsers = selectAdminsStmt.executeQuery()) {
	            boolean updated = false;

	            while (adminUsers.next()) {
	                int adminID = adminUsers.getInt("userID");

	                // Retrieve the current balance for this admin
	                selectBalanceStmt.setInt(1, adminID);
	                try (ResultSet balanceResult = selectBalanceStmt.executeQuery()) {
	                    if (balanceResult.next()) {
	                        BigDecimal currentBalance = balanceResult.getBigDecimal("balance");
	                        BigDecimal newBalance = currentBalance.add(amount); // Calculate new balance using BigDecimal

	                        // Update the balance in the admin table
	                        updateBalanceStmt.setBigDecimal(1, newBalance);
	                        updateBalanceStmt.setInt(2, adminID);

	                        int rowsAffected = updateBalanceStmt.executeUpdate();
	                        if (rowsAffected > 0) {
	                            updated = true; // At least one admin balance was updated
	                        }
	                    } else {
	                        // Handle case where adminID is missing in the admin table
	                        AlertUtils.showError("Admin Entry Missing", 
	                            "No corresponding entry in 'admin' table for userID: " + adminID);
	                    }
	                }
	            }

	            if (!updated) {
	                AlertUtils.showWarning("No admin balances were updated.");
	            }

	            return updated;
	        }

	    } catch (SQLIntegrityConstraintViolationException e) {
	        AlertUtils.showError("Integrity Error", e.getMessage());
	        e.printStackTrace(); // Optional: for debugging
	    }

	    return false; // Return false if update failed
	}
	@Override
	public int delete(User user) {
		// TODO Auto-generated method stub
		return 0;
	}

}
