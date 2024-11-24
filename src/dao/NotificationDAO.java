package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import models.Notification;

public class NotificationDAO implements InterfaceNotificationDAO {

	// Get a specific notification by its ID
	@Override
	public Notification get(int id) throws SQLException {
	    String query = "SELECT * FROM Notification WHERE notificationID = ?";
	    try (Connection conn = DatabaseConnection.getInstance().getConnection(); 
	         PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setInt(1, id);
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                // Convert the Timestamp to java.util.Date
	            	return this.mapResultSetToNotification(rs);
	            }
	        }
	    }
	    return null;
	}


    // Get all notifications from the database
    @Override
    public Map<Integer, Notification> getAll() throws SQLException {
        String query = "SELECT * FROM Notification";
        Map<Integer, Notification> notifications = new HashMap<>();
        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
        		ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Notification notification = this.mapResultSetToNotification(rs);
                notifications.put(notification.getNotificationID(), notification);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all notifications: " + e.getMessage());
            throw new SQLException("Error fetching all notifications.");
        }
        return notifications;
    }

    // Insert a new notification into the Notification table
    @Override
    public int insert(Notification notification) throws SQLException {
        String query = "INSERT INTO notification (recipientID, message, status, timestamp, type, recipientRole) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Set parameters for the query
            stmt.setInt(1, notification.getRecipientID());
            stmt.setString(2, notification.getNotificationMessage());
            stmt.setString(3, notification.getStatus());
            Timestamp sqlTimestamp = (notification.getTimestamp() != null) 
                    ? new Timestamp(notification.getTimestamp().getTime()) 
                    : null;
                stmt.setTimestamp(4, sqlTimestamp); // Use the converted Timestamp
            stmt.setString(5, notification.getType());
            stmt.setString(6, notification.getRecipientRole());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);  // Return the generated notificationID
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting notification: " + e.getMessage());
            throw new SQLException("Error inserting notification.");
        }
        return 0;
    }


    // Update an existing notification in the Notification table
    @Override
    public int update(Notification notification) throws SQLException {
        String query = "UPDATE notification SET message = ?, status = ?, timestamp = ?, type = ?, recipientRole = ? WHERE notificationID = ?";
        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, notification.getNotificationMessage());
            stmt.setString(2, notification.getStatus());
            
            // Convert java.util.Date to java.sql.Timestamp
            Timestamp sqlTimestamp = (notification.getTimestamp() != null) 
                ? new Timestamp(notification.getTimestamp().getTime()) 
                : null;
            stmt.setTimestamp(3, sqlTimestamp); // Use the converted Timestamp

            stmt.setString(4, notification.getType());
            stmt.setString(5, notification.getRecipientRole());
            stmt.setInt(6, notification.getNotificationID());

            return stmt.executeUpdate(); // Returns the number of rows affected

        } catch (SQLException e) {
            System.err.println("Error updating notification: " + e.getMessage());
            throw new SQLException("Error updating notification.");
        }
    }


    // Delete a notification from the database
    @Override
    public int delete(Notification notification) throws SQLException {
        String query = "DELETE FROM Notification WHERE notificationID = ?";
        try (Connection con = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setInt(1, notification.getNotificationID());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting notification: " + e.getMessage());
            throw new SQLException("Error deleting notification.");
        }
    }

 // Helper method to map ResultSet to Notification object
    private Notification mapResultSetToNotification(ResultSet rs) throws SQLException {
    	return new Notification(
                rs.getInt("notificationID"),
                rs.getInt("recipientID"),
                rs.getString("message"),
                rs.getDate("timestamp"),
                rs.getString("status"),
                rs.getString("type"),
                rs.getString("recipientRole")
            );
        
    }



}
