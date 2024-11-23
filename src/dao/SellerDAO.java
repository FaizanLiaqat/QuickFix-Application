package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Booking;
import models.Seller;
import models.Service;
import models.User;
import utils.AlertUtils;

public class SellerDAO extends UserDAO {
	
	// Method to get the services of a seller
    public Map<Integer, Service> getSellerServices(int sellerID) throws SQLException {
        // Use ServiceDAO to get all services for the seller
        ServiceDAO serviceDAO = new ServiceDAO();
        List<Service> servicesList = serviceDAO.getAllBySellerID(sellerID);
        
        // Create a Map to hold the services, with serviceID as the key
        Map<Integer, Service> services = new HashMap<>();
        
        // Populate the Map with services
        for (Service service : servicesList) {
            services.put(service.getServiceID(), service);
        }

        return services;
    }
 // Method to get the bookings of a seller
    public Map<Integer, Booking> getSellerBookings(int sellerID) throws SQLException {
        // Use BookingDAO to get all bookings for the seller
        BookingDAO bookingDAO = new BookingDAO();
        List<Booking> bookingsList = bookingDAO.getAllBySellerID(sellerID);
        
        // Create a Map to hold the bookings, with bookingID as the key
        Map<Integer, Booking> bookings = new HashMap<>();
        
        // Populate the Map with bookings
        for (Booking booking : bookingsList) {
            bookings.put(booking.getBookingID(), booking);
        }

        return bookings;
    }
 
    public User get(int id, boolean fetchAssociatedData) throws SQLException {
        // Join User and ServiceProvider tables to get seller details
        String query = "SELECT u.*, sp.availability FROM User u " +
                       "LEFT JOIN ServiceProvider sp ON u.userID = sp.serviceProviderID " +
                       "WHERE u.userID = ? AND u.role = 'Seller'";

        Seller seller = null;

        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
             
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Create a Seller object from the result set
                    seller = new Seller();
                    seller.setUserID(rs.getInt("userID"));
                    seller.setUserName(rs.getString("name"));
                    seller.setUserPassword(rs.getString("password"));
                    seller.setUserEmail(rs.getString("email"));
                    seller.setUserPhoneNumber(rs.getString("phone"));
                    seller.setUserLocation(rs.getString("location"));
                    seller.setAvailable(rs.getBoolean("availability")); // Availability fetched from ServiceProvider table

                    // Fetch the associated data (e.g., services) if requested
                    if (fetchAssociatedData) {
                        // Load the services of the seller
                        seller.setSellerServices(getSellerServices(id)); // Populating services
                    }
                }
            }
        } catch (SQLException e) {
            AlertUtils.showError("Failed to retrieve seller: ", "An error occurred while retrieving the user with ID " + id);
            e.printStackTrace();  // Optional: for debugging
        }

        return seller; // Return the seller object with associated data
    }

	
	@Override
    public User get(int id) throws SQLException {
		String query = "SELECT u.*, sp.availability FROM User u " +
                "LEFT JOIN ServiceProvider sp ON u.userID = sp.serviceProviderID " +
                "WHERE u.userID = ? AND u.role = 'Seller'";
        
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
        // Join User and ServiceProvider tables to get all sellers
        String query = "SELECT u.*, sp.availability FROM User u " +
                       "LEFT JOIN ServiceProvider sp ON u.userID = sp.serviceProviderID " +
                       "WHERE u.role = 'Seller'";

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
                seller.setAvailable(rs.getBoolean("availability")); // Availability from ServiceProvider table
                
                // Optionally, populate seller's services and bookings if needed
                sellers.put(seller.getUserID(), seller);
            }

        } catch (SQLException e) {
            AlertUtils.showError("Failed to retrieve all sellers: ", e.getMessage());
            e.printStackTrace();  // Optional: for debugging
        }

        return sellers;
    }

    @Override
    public int insert(User user) throws SQLException {
        // Assuming the role 'Seller' is predefined
        String insertUserQuery = "INSERT INTO User (name, email, password, phone, location, role) VALUES (?, ?, ?, ?, ?, 'Seller')";
        String insertServiceProviderQuery = "INSERT INTO ServiceProvider (serviceProviderID, availability) VALUES (?, TRUE)";

        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement userStmt = con.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement serviceProviderStmt = con.prepareStatement(insertServiceProviderQuery)) {

            // Insert into the User table
            userStmt.setString(1, user.getUserName());
            userStmt.setString(2, user.getUserEmail());
            userStmt.setString(3, user.getUserPassword());
            userStmt.setString(4, user.getUserPhoneNumber());
            userStmt.setString(5, user.getUserLocation());

            int rowsAffected = userStmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet rs = userStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int userID = rs.getInt(1);  // Get the generated userID
                        
                        // Now insert into ServiceProvider table
                        serviceProviderStmt.setInt(1, userID);  // serviceProviderID will be the same as userID
                        serviceProviderStmt.executeUpdate();

                        return userID; // Return generated userID (also serviceProviderID)
                    }
                }
            } else {
                AlertUtils.showError("Unexpected Error", "Failed to insert seller.");
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            AlertUtils.showError("Duplicate entry: ", e.getMessage());
            e.printStackTrace(); // Optional: for debugging
        }

        return 0; // Return 0 if insertion failed
    }


    @Override
    public int update(User user) throws SQLException {
        // Check if the seller exists before proceeding with the update
        if (exists(user)==-1) {
            AlertUtils.showError("Update Failed", "Seller does not exist.");
            return 0;
        }

        String updateUserQuery = "UPDATE User SET name = ?, email = ?, phone = ?, location = ?, role = 'Seller' WHERE userID = ? AND role = 'Seller'";
        String updateServiceProviderQuery = "UPDATE ServiceProvider SET availability = ? WHERE serviceProviderID = ?";

        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement userStmt = con.prepareStatement(updateUserQuery);
             PreparedStatement serviceProviderStmt = con.prepareStatement(updateServiceProviderQuery)) {

            // Update User table
            userStmt.setString(1, user.getUserName());
            userStmt.setString(2, user.getUserEmail());
            userStmt.setString(3, user.getUserPhoneNumber());
            userStmt.setString(4, user.getUserLocation());
            userStmt.setInt(5, user.getUserID());

            int rowsAffected = userStmt.executeUpdate();

            // Update ServiceProvider table
            serviceProviderStmt.setBoolean(1, ((Seller) user).isAvailable()); // Cast to Seller to access availability
            serviceProviderStmt.setInt(2, user.getUserID());

            serviceProviderStmt.executeUpdate();

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
