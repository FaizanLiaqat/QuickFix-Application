package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Dispute;

public class DisputeDAO implements InterfaceDisputeDAO {



    
    public DisputeDAO() {
       
    }

    @Override
    public Dispute get(int id) {
        String query = "SELECT * FROM Dispute WHERE disputeID = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToDispute(resultSet);
                }
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<Integer, Dispute> getAll() {
        Map<Integer, Dispute> disputes = new HashMap<>();
        String query = "SELECT * FROM Dispute";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Dispute dispute = mapResultSetToDispute(resultSet);
                disputes.put(dispute.getDisputeID(), dispute);
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        return disputes;
    }

    @Override
    public int insert(Dispute dispute) {
        String query = "INSERT INTO Dispute (bookingID, buyerID, sellerID, disputeReason, disputeStatus, resolutionDetails, createdAt, resolvedAt) "
                     + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, dispute.getBookingID());
            statement.setInt(2, dispute.getBuyerID());
            statement.setInt(3, dispute.getSellerID());
            statement.setString(4, dispute.getDisputeReason());
            statement.setString(5, dispute.getDisputeStatus());
            statement.setString(6, dispute.getResolutionDetails());
            statement.setTimestamp(7, dispute.getCreatedAt());
            statement.setTimestamp(8, dispute.getResolvedAt());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        return 0;
    }
    
    @Override
    public int update(Dispute dispute) {
        String query = "UPDATE Dispute SET disputeStatus = ?, resolutionDetails = ?, resolvedAt = ? WHERE disputeID = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, dispute.getDisputeStatus());
            statement.setString(2, dispute.getResolutionDetails());
            statement.setTimestamp(3, dispute.getResolvedAt() != null ? dispute.getResolvedAt() : null);

            statement.setInt(4, dispute.getDisputeID());

            return statement.executeUpdate();
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int delete(Dispute dispute) {
        String query = "DELETE FROM Dispute WHERE disputeID = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, dispute.getDisputeID());
            return statement.executeUpdate();
        } catch (SQLException e) {
        	e.printStackTrace();
        }
        return 0;
    }
    
    public int insertWithoutResolvedAt(Dispute dispute) {
        String query = "INSERT INTO Dispute (bookingID, buyerID, sellerID, disputeReason, disputeStatus, resolutionDetails, createdAt) "
                     + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, dispute.getBookingID());
            statement.setInt(2, dispute.getBuyerID());
            statement.setInt(3, dispute.getSellerID());
            statement.setString(4, dispute.getDisputeReason());
            statement.setString(5, dispute.getDisputeStatus());
            statement.setString(6, dispute.getResolutionDetails());
            statement.setTimestamp(7, dispute.getCreatedAt());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Return the generated disputeID
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public List<Dispute> getDisputesByStatus(String status) {
        List<Dispute> disputes = new ArrayList<>();
        String query = "SELECT * FROM Dispute WHERE disputeStatus = ?";
        
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, status);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                	Dispute dispute = mapResultSetToDispute(resultSet);
                    disputes.add(dispute);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return disputes;
    }
    private Dispute mapResultSetToDispute(ResultSet resultSet) throws SQLException {
        int disputeID = resultSet.getInt("disputeID");
        int bookingID = resultSet.getInt("bookingID");
        int buyerID = resultSet.getInt("buyerID");
        int sellerID = resultSet.getInt("sellerID");
        String disputeReason = resultSet.getString("disputeReason");
        String disputeStatus = resultSet.getString("disputeStatus");
        String resolutionDetails = resultSet.getString("resolutionDetails");
        java.sql.Timestamp createdAt = resultSet.getTimestamp("createdAt");
        java.sql.Timestamp resolvedAt = resultSet.getTimestamp("resolvedAt");

        return new Dispute(disputeID, bookingID, buyerID, sellerID, disputeReason, disputeStatus,
                resolutionDetails, createdAt, resolvedAt);
    }
}
