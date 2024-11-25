package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Booking;
import models.Notification;

import java.sql.*;

public class BookingDAO implements InterfaceBookingDAO {


    private NotificationDAO notificationDAO = new NotificationDAO();

    // Get a specific booking by its ID
    @Override
    public Booking get(int id) throws SQLException {
        String query = "SELECT * FROM Booking WHERE bookingID = ?";
        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBooking(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching booking: " + e.getMessage());
            throw new SQLException("Error fetching booking with ID: " + id);
        }
        return null;
    }

    // Get all bookings
    @Override
    public Map<Integer, Booking> getAll() throws SQLException {
        String query = "SELECT * FROM Booking";
        Map<Integer, Booking> bookings = new HashMap<>();
        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Booking booking = mapResultSetToBooking(rs);
                bookings.put(booking.getBookingID(), booking);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all bookings: " + e.getMessage());
            throw new SQLException("Error fetching all bookings.");
        }
        return bookings;
    }

    // Insert a new booking
    @Override
    public int insert(Booking booking) throws SQLException {
        // Check for duplicate bookings
        if (isDuplicateBooking(booking)) {
            System.out.println("Booking already exists for this buyer, seller, and service.");
            return 0; // Return 0 to indicate no insertion
        }

        String insertBookingQuery = "INSERT INTO Booking (clientID, serviceProviderID, serviceID, bookingDate, preferredTime, status, paymentStatus) VALUES (?, ?, ?, ?, ?, 'Pending', 'Unpaid')";
        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(insertBookingQuery, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, booking.getClientID());
            stmt.setInt(2, booking.getServiceProviderID());
            stmt.setInt(3, booking.getServiceID());
            stmt.setTimestamp(4, booking.getBookingDate());
            stmt.setTimestamp(5, booking.getPreferredTime());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int bookingID = rs.getInt(1);
                        booking.setBookingID(bookingID);
                        java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis());


//                        // Generate notification for the seller
                        Notification notification = new Notification(
                        	    0, 
                        	    booking.getServiceProviderID(),
                        	    "New booking created by buyer: " + booking.getClientID(),
                        	    currentTimestamp, 
                        	    "Unread", 
                        	    "BookingConfirmation", 
                        	    "Seller" 
                        	);

                        notificationDAO.insert(notification);


                        return bookingID; // Return the generated booking ID
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting booking: " + e.getMessage());
            throw new SQLException("Error inserting booking.");
        }
        return 0; // Return 0 if insertion failed
    }

    // Update an existing booking
    @Override
    public int update(Booking booking) throws SQLException {
        String query = "UPDATE Booking SET status = ?, paymentStatus = ?, preferredTime = ? WHERE bookingID = ?";
        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, booking.getBookingStatus());
            stmt.setString(2, booking.getPaymentStatus());
            stmt.setTimestamp(3, booking.getPreferredTime());
            stmt.setInt(4, booking.getBookingID());

            return stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating booking: " + e.getMessage());
            throw new SQLException("Error updating booking.");
        }
    }

    // Delete a booking
    @Override
    public int delete(Booking booking) throws SQLException {
        String query = "DELETE FROM Booking WHERE bookingID = ?";
        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, booking.getBookingID());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting booking: " + e.getMessage());
            throw new SQLException("Error deleting booking.");
        }
    }

    // Helper method to map ResultSet to Booking object
    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        return new Booking(
            rs.getInt("bookingID"),
            rs.getInt("clientID"),
            rs.getInt("serviceProviderID"),
            rs.getInt("serviceID"),
            rs.getTimestamp("bookingDate"),
            rs.getTimestamp("preferredTime"),
            rs.getString("status"),
            rs.getString("paymentStatus")
        );
    }

    // Helper method to check for duplicate bookings
    private boolean isDuplicateBooking(Booking booking) throws SQLException {
        String query = "SELECT COUNT(*) FROM Booking WHERE clientID = ? AND serviceProviderID = ? AND serviceID = ?";
        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, booking.getClientID());
            stmt.setInt(2, booking.getServiceProviderID());
            stmt.setInt(3, booking.getServiceID());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }


	@Override
	public List<Booking> getAllByBuyerID(int buyerID) throws SQLException {
		String query = "SELECT * FROM Booking WHERE clientID = ?";
		List<Booking> bookings = new ArrayList<>();
		try (Connection con = DatabaseConnection.getInstance().getConnection();
				PreparedStatement stmt = con.prepareStatement(query)) {

			stmt.setInt(1, buyerID);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					bookings.add(mapResultSetToBooking(rs));
				}
			}
		} catch (SQLException e) {
			System.err.println("Error fetching bookings by buyer ID: " + e.getMessage());
			throw new SQLException("Error fetching bookings for buyer with ID: " + buyerID);
		}
		return bookings;
	}

	@Override
	public List<Booking> getAllBySellerID(int sellerID) throws SQLException {
		String query = "SELECT * FROM Booking WHERE serviceProviderID = ?";
		List<Booking> bookings = new ArrayList<>();
		try (Connection con = DatabaseConnection.getInstance().getConnection();
				PreparedStatement stmt = con.prepareStatement(query)) {

			stmt.setInt(1, sellerID);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					bookings.add(mapResultSetToBooking(rs));
				}
			}
		} catch (SQLException e) {
			System.err.println("Error fetching bookings by seller ID: " + e.getMessage());
			throw new SQLException("Error fetching bookings for seller with ID: " + sellerID);
		}
		return bookings;
	}

	public List<Booking> getByBuyerID(int buyerID) throws SQLException {
		String query = "SELECT * FROM Booking WHERE clientID = ?";
		List<Booking> bookings = new ArrayList<>();
		try (Connection con = DatabaseConnection.getInstance().getConnection();
				PreparedStatement stmt = con.prepareStatement(query)) {

			stmt.setInt(1, buyerID);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					bookings.add(mapResultSetToBooking(rs));
				}
			}
		} catch (SQLException e) {
			System.err.println("Error fetching bookings by buyer ID: " + e.getMessage());
			throw new SQLException("Error fetching bookings for buyer with ID: " + buyerID);
		}
		return bookings;
	}

	public List<Booking> getBySellerID(int sellerID) throws SQLException {
		String query = "SELECT * FROM Booking WHERE serviceProviderID = ?";
		List<Booking> bookings = new ArrayList<>();
		try (Connection con = DatabaseConnection.getInstance().getConnection();
				PreparedStatement stmt = con.prepareStatement(query)) {

			stmt.setInt(1, sellerID);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					bookings.add(mapResultSetToBooking(rs));
				}
			}
		} catch (SQLException e) {
			System.err.println("Error fetching bookings by seller ID: " + e.getMessage());
			throw new SQLException("Error fetching bookings for seller with ID: " + sellerID);
		}
		return bookings;
	}

	public List<Booking> getByServiceID(int serviceID) throws SQLException {
		String query = "SELECT * FROM Booking WHERE serviceID = ?";
		List<Booking> bookings = new ArrayList<>();
		try (Connection con = DatabaseConnection.getInstance().getConnection();
				PreparedStatement stmt = con.prepareStatement(query)) {

			stmt.setInt(1, serviceID);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					bookings.add(mapResultSetToBooking(rs));
				}
			}
		} catch (SQLException e) {
			System.err.println("Error fetching bookings by service ID: " + e.getMessage());
			throw new SQLException("Error fetching bookings for service with ID: " + serviceID);
		}
		return bookings;
	}

	public List<Booking> getBookingsByStatus(String status, int ID) throws SQLException {
		List<Booking> bookings = new ArrayList<>();
		String query = """
				    SELECT b.bookingID, b.clientID, b.serviceProviderID, b.serviceID, b.bookingDate,
				           b.preferredTime, b.status, b.paymentStatus,
				           u.name AS sellerName, s.serviceName
				    FROM booking b
				    JOIN user u ON b.serviceProviderID = u.userID
				    JOIN service s ON b.serviceID = s.serviceID
				    WHERE b.status = ? AND b.clientID = ?;
				""";

		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			// Set both parameters in the prepared statement
			preparedStatement.setString(1, status); // Set status
			preparedStatement.setInt(2, ID); // Set bookingID

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				bookings.add(new Booking(resultSet.getInt("bookingID"), resultSet.getInt("clientID"),
						resultSet.getInt("serviceProviderID"), resultSet.getInt("serviceID"),
						resultSet.getTimestamp("bookingDate"), resultSet.getTimestamp("preferredTime"),
						resultSet.getString("status"), resultSet.getString("paymentStatus"),
						resultSet.getString("sellerName"), // Seller's name from user table
						resultSet.getString("serviceName") // Service name from service table
				));
			}
		}
		System.out.println(bookings.size());
		return bookings;
	}
	public List<Booking> getBookingsByStatus2(String status, int ID) throws SQLException {
		List<Booking> bookings = new ArrayList<>();
		String query = """
				    SELECT b.bookingID, b.clientID, b.serviceProviderID, b.serviceID, b.bookingDate,
				           b.preferredTime, b.status, b.paymentStatus,
				           u.name AS sellerName, s.serviceName
				    FROM booking b
				    JOIN user u ON b.serviceProviderID = u.userID
				    JOIN service s ON b.serviceID = s.serviceID
				    WHERE b.status = ? AND b.serviceProviderID = ?;
				""";

		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			// Set both parameters in the prepared statement
			preparedStatement.setString(1, status); // Set status
			preparedStatement.setInt(2, ID); // Set bookingID

			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				bookings.add(new Booking(resultSet.getInt("bookingID"), resultSet.getInt("clientID"),
						resultSet.getInt("serviceProviderID"), resultSet.getInt("serviceID"),
						resultSet.getTimestamp("bookingDate"), resultSet.getTimestamp("preferredTime"),
						resultSet.getString("status"), resultSet.getString("paymentStatus"),
						resultSet.getString("sellerName"), // Seller's name from user table
						resultSet.getString("serviceName") // Service name from service table
				));
			}
		}
		System.out.println(bookings.size());
		return bookings;
	}

	public void ChangepaymentStatus(int bookingid) throws SQLException {
		// SQL query to update payment status
		String updateQuery = "UPDATE booking SET paymentStatus = ? WHERE bookingID = ?";

		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
			// Set the parameters for the query
			preparedStatement.setString(1, "Paid"); // New payment status ('Paid' or 'Unpaid')
			preparedStatement.setInt(2, bookingid); // Booking ID to update

			// Execute the update query
			int rowsUpdated = preparedStatement.executeUpdate();

			if (rowsUpdated > 0) {
				System.out.println("Payment status updated successfully for booking ID: " + bookingid);
			} else {
				System.out.println("No booking found with booking ID: " + bookingid);
			}

		} catch (SQLException e) {
			// Handle SQL exception
			System.out.println("Error updating payment status: " + e.getMessage());
		}
	}
	
	public void ChangeStatus(String status, int bookingid) throws SQLException {
		// SQL query to update payment status
		String updateQuery = "UPDATE booking SET status = ? WHERE bookingID = ?";

		try (Connection connection = DatabaseConnection.getInstance().getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
			// Set the parameters for the query
			preparedStatement.setString(1, status); // New payment status ('Paid' or 'Unpaid')
			preparedStatement.setInt(2, bookingid); // Booking ID to update

			// Execute the update query
			int rowsUpdated = preparedStatement.executeUpdate();

			if (rowsUpdated > 0) {
				System.out.println("Payment status updated successfully for booking ID: " + bookingid);
			} else {
				System.out.println("No booking found with booking ID: " + bookingid);
			}

		} catch (SQLException e) {
			// Handle SQL exception
			System.out.println("Error updating payment status: " + e.getMessage());
		}
	}

}
