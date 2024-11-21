package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import models.Seller;
import models.User;
import utils.AlertUtils;

public class SellerDAO extends UserDAO {
	
	/*public User get(int id, boolean fetchAssociatedData) throws SQLException{
		
	}*/
	@Override
    public User get(int id) throws SQLException {
        String query = "SELECT * FROM User WHERE userID = ? AND role = 'Seller'";
        
        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
             
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Create a Seller object from the result set
                    Seller seller = new Seller();
                    seller.setUserID(rs.getInt("userID"));
                    seller.setUserName(rs.getString("name"));
                    seller.setUserPassword(rs.getString("password"));
                    seller.setUserEmail(rs.getString("email"));
                    seller.setUserPhoneNumber(rs.getString("phone"));
                    seller.setUserLocation(rs.getString("location"));
                    seller.setAvailable(rs.getBoolean("available"));
                    // Optionally, populate seller's services and bookings if needed
                    
                    return seller;
                }
            }
        } catch (SQLException e) {
            AlertUtils.showError("Failed to retrieve seller: " , "An error occurred while retrieving the user with ID " + id);
            e.printStackTrace();  // Optional: for debugging
        }

        return null; // Return null if seller not found
    }

    @Override
    public Map<Integer, User> getAll() throws SQLException {
        String query = "SELECT * FROM User WHERE role = 'Seller'";
        Map<Integer, User> sellers = new HashMap<>();

        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Seller seller = new Seller();
                seller.setUserID(rs.getInt("userID"));
                seller.setUserName(rs.getString("name"));
                seller.setUserPassword(rs.getString("password"));
                seller.setUserEmail(rs.getString("email"));
                seller.setUserPhoneNumber(rs.getString("phone"));
                seller.setUserLocation(rs.getString("location"));
                seller.setAvailable(rs.getBoolean("available"));
                // Optionally, populate seller's services and bookings if needed
                
                sellers.put(seller.getUserID(), seller);
            }

        } catch (SQLException e) {
            AlertUtils.showError("Failed to retrieve all sellers: " , e.getMessage());
            e.printStackTrace();  // Optional: for debugging
        }

        return sellers;
    }

    @Override
    public int insert(User user) throws SQLException {
        // Assuming the role 'Seller' is predefined
        String query = "INSERT INTO User (name, email, password, phone, location, role) VALUES (?, ?, ?, ?, ?, 'Seller')";

        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getUserEmail());
            stmt.setString(3, user.getUserPassword());
            stmt.setString(4, user.getUserPhoneNumber());
            stmt.setString(5, user.getUserLocation());

            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Return generated userID
                    }
                }
            } else {
                AlertUtils.showError("Unexpected Error","Failed to insert seller.");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            AlertUtils.showError("Duplicate entry: " , e.getMessage());
            e.printStackTrace(); // Optional: for debugging
        }

        return -1; // Return -1 if insertion failed
    }

    @Override
    public int update(User user) throws SQLException {
    	// Check if the seller exists before proceeding with the update
        if (!exists(user)) {
            AlertUtils.showError("Update Failed", "Seller does not exist.");
            return 0;
        }

        String query = "UPDATE User SET name = ?, email = ?, phone = ?, location = ?, available = ? WHERE userID = ? AND role = 'Seller'";

        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            // Set parameters for the prepared statement
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getUserEmail());
            stmt.setString(3, user.getUserPhoneNumber());
            stmt.setString(4, user.getUserLocation());
            stmt.setBoolean(5, ((Seller) user).isAvailable()); // Cast to Seller to access `available` property
            stmt.setInt(6, user.getUserID());

            // Execute the update query
            int rowsAffected = stmt.executeUpdate();

            // Check the result and show appropriate alerts
            if (rowsAffected > 0) {
                AlertUtils.showSuccess("Seller updated successfully.");
            } else {
                AlertUtils.showError("Update Failed", "Unable to update seller. Please try again.");
            }

            return rowsAffected;
        } catch (SQLException e) {
            // Handle any SQL exceptions and show error alert
            AlertUtils.showError("Update Failed", "Failed to update seller: " + e.getMessage());
            e.printStackTrace(); // Optional: for debugging
            return 0; // Return 0 if the update failed
        }
    }
	@Override
	public int delete(User user) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

}
