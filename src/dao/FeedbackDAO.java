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

import models.FeedBack;

public class FeedbackDAO implements InterfaceFeedbackDAO {

	// Get feedback by ID
    @Override
    public FeedBack get(int id) {
        String query = "SELECT * FROM Feedback WHERE feedbackID = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new FeedBack(
                        resultSet.getInt("feedbackID"),
                        resultSet.getInt("clientID"),
                        resultSet.getInt("serviceProviderID"),
                        resultSet.getInt("bookingID"),
                        resultSet.getInt("serviceID"),
                        resultSet.getInt("rating"),
                        resultSet.getString("comments"),
                        resultSet.getTimestamp("feedbackDate")
                    );
            }
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }

    // Get all feedbacks
    @Override
    public Map<Integer, FeedBack> getAll() {
        String query = "SELECT * FROM Feedback";
        Map<Integer, FeedBack> feedbacks = new HashMap<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
            	FeedBack feedback = new FeedBack(
                        resultSet.getInt("feedbackID"),
                        resultSet.getInt("clientID"),
                        resultSet.getInt("serviceProviderID"),
                        resultSet.getInt("bookingID"),
                        resultSet.getInt("serviceID"),
                        resultSet.getInt("rating"),
                        resultSet.getString("comments"),
                        resultSet.getTimestamp("feedbackDate")
                    );
                feedbacks.put(feedback.getFeedbackID(), feedback);
            }
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return feedbacks;
    }

    @Override
    public int insert(FeedBack feedback) {
        // Query to check for duplicate feedback
        String checkDuplicateQuery = "SELECT feedbackID FROM Feedback WHERE clientID = ? AND serviceProviderID = ? AND bookingID = ?";
        String insertQuery = "INSERT INTO Feedback (clientID, serviceProviderID, bookingID, rating, comments) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkDuplicateQuery);
             PreparedStatement insertStmt = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

            // Check for duplicates
            checkStmt.setInt(1, feedback.getClientID());
            checkStmt.setInt(2, feedback.getServiceProviderID());
            checkStmt.setInt(3, feedback.getBookingID());
            //checkStmt.setInt(4, feedback.getServiceID());
            
            ResultSet resultSet = checkStmt.executeQuery();
            if (resultSet.next()) {
                // Duplicate feedback exists
                System.err.println("Duplicate feedback detected: Feedback already exists for the same client, service provider, booking, and service.");
                return -1; // Return -1 or an error code to indicate duplicate
            }

            // Insert new feedback if no duplicates
            insertStmt.setInt(1, feedback.getClientID());
            insertStmt.setInt(2, feedback.getServiceProviderID());
            insertStmt.setInt(3, feedback.getBookingID());
            //insertStmt.setInt(4, feedback.getServiceID());
            insertStmt.setInt(4, feedback.getRating());
            insertStmt.setString(5, feedback.getComments());

            int affectedRows = insertStmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet keys = insertStmt.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1); // Return generated feedback ID
                }
            }
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return 0; // Return 0 if insertion failed
    }

    // Update feedback
    @Override
    public int update(FeedBack feedback) {
        String query = "UPDATE Feedback SET rating = ?, comments = ? WHERE feedbackID = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, feedback.getRating());
            preparedStatement.setString(2, feedback.getComments());
            preparedStatement.setInt(3, feedback.getFeedbackID());

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
    }

    // Delete feedback
    @Override
    public int delete(FeedBack feedback) {
        String query = "DELETE FROM Feedback WHERE feedbackID = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, feedback.getFeedbackID());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
    }
    
    public FeedBack getByBuyerID(int buyerID) throws SQLException {
        String query = "SELECT * FROM Feedback WHERE clientID = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            preparedStatement.setInt(1, buyerID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new FeedBack(
                        resultSet.getInt("feedbackID"),
                        resultSet.getInt("clientID"),
                        resultSet.getInt("serviceProviderID"),
                        resultSet.getInt("bookingID"),
                        resultSet.getInt("serviceID"),
                        resultSet.getInt("rating"),
                        resultSet.getString("comments"),
                        resultSet.getTimestamp("feedbackDate")
                    );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching feedback by Buyer ID: " + e.getMessage());
            throw e;
        }
        return null;
    }
 // Retrieve feedback by Seller ID
    public FeedBack getBySellerID(int sellerID) throws SQLException {
        String query = "SELECT * FROM Feedback WHERE serviceProviderID = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            preparedStatement.setInt(1, sellerID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new FeedBack(
                        resultSet.getInt("feedbackID"),
                        resultSet.getInt("clientID"),
                        resultSet.getInt("serviceProviderID"),
                        resultSet.getInt("bookingID"),
                        resultSet.getInt("serviceID"),
                        resultSet.getInt("rating"),
                        resultSet.getString("comments"),
                        resultSet.getTimestamp("feedbackDate")
                    );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching feedback by Seller ID: " + e.getMessage());
            throw e;
        }
        return null;
    }
 // Retrieve all feedback for a specific Buyer ID
    public List<FeedBack> getAllByBuyerID(int buyerID) throws SQLException {
        String query = "SELECT * FROM Feedback WHERE clientID = ?";
        List<FeedBack> feedbackList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            preparedStatement.setInt(1, buyerID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                FeedBack feedback = new FeedBack(
                        resultSet.getInt("feedbackID"),
                        resultSet.getInt("clientID"),
                        resultSet.getInt("serviceProviderID"),
                        resultSet.getInt("bookingID"),
                        resultSet.getInt("serviceID"),
                        resultSet.getInt("rating"),
                        resultSet.getString("comments"),
                        resultSet.getTimestamp("feedbackDate")
                    );
                feedbackList.add(feedback);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all feedback for Buyer ID: " + e.getMessage());
            throw e;
        }
        return feedbackList;
    }

 // Retrieve all feedback for a specific Seller ID
    public List<FeedBack> getAllBySellerID(int sellerID) throws SQLException {
        String query = "SELECT * FROM Feedback WHERE serviceProviderID = ?";
        List<FeedBack> feedbackList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            preparedStatement.setInt(1, sellerID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                FeedBack feedback = new FeedBack(
                        resultSet.getInt("feedbackID"),
                        resultSet.getInt("clientID"),
                        resultSet.getInt("serviceProviderID"),
                        resultSet.getInt("bookingID"),
                        resultSet.getInt("serviceID"),
                        resultSet.getInt("rating"),
                        resultSet.getString("comments"),
                        resultSet.getTimestamp("feedbackDate")
                    );
                feedbackList.add(feedback);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all feedback for Seller ID: " + e.getMessage());
            throw e;
        }
        return feedbackList;
    }
    
 // Retrieve feedback by Booking ID
    public FeedBack getByBookingID(int bookingID) throws SQLException {
        String query = "SELECT * FROM Feedback WHERE bookingID = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            preparedStatement.setInt(1, bookingID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new FeedBack(
                        resultSet.getInt("feedbackID"),
                        resultSet.getInt("clientID"),
                        resultSet.getInt("serviceProviderID"),
                        resultSet.getInt("bookingID"),
                        resultSet.getInt("serviceID"),
                        resultSet.getInt("rating"),
                        resultSet.getString("comments"),
                        resultSet.getTimestamp("feedbackDate")
                    );
            }
        } catch (SQLException e) {
            System.err.println("Error fetching feedback by Booking ID: " + e.getMessage());
            throw e;
        }
        return null;
    }
    
    public List<FeedBack> getAllByBookingID(int bookingID) throws SQLException {
        String query = "SELECT * FROM Feedback WHERE bookingID = ?";
        List<FeedBack> feedbackList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, bookingID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                FeedBack feedback = new FeedBack(
                    resultSet.getInt("feedbackID"),
                    resultSet.getInt("clientID"),
                    resultSet.getInt("serviceProviderID"),
                    resultSet.getInt("bookingID"),
                    resultSet.getInt("serviceID"),
                    resultSet.getInt("rating"),
                    resultSet.getString("comments"),
                    resultSet.getTimestamp("feedbackDate")
                );
                feedbackList.add(feedback);
            }

            if (feedbackList.isEmpty()) {
                System.out.println("No feedback found for Booking ID: " + bookingID);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching feedback for Booking ID " + bookingID + ": " + e.getMessage());
            throw e; // Re-throw the exception for the caller to handle
        }

        return feedbackList;
    }
    
    // Retrieve a single feedback by Service ID
    public FeedBack getByServiceID(int serviceID) throws SQLException {
        String query = "SELECT * FROM Feedback WHERE serviceID = ?";
        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            preparedStatement.setInt(1, serviceID);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new FeedBack(
                    resultSet.getInt("feedbackID"),
                    resultSet.getInt("clientID"),
                    resultSet.getInt("serviceProviderID"),
                    resultSet.getInt("bookingID"),
                    resultSet.getInt("serviceID"),
                    resultSet.getInt("rating"),
                    resultSet.getString("comments"),
                    resultSet.getTimestamp("feedbackDate")
                );
            } else {
                System.out.println("No feedback found for Service ID: " + serviceID);
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching feedback by Service ID: " + e.getMessage());
            throw e;
        }
    }

    // Retrieve all feedback for a specific Service ID
    public List<FeedBack> getAllByServiceID(int serviceID) throws SQLException {
        String query = "SELECT * FROM Feedback WHERE serviceID = ?";
        List<FeedBack> feedbackList = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
             
            preparedStatement.setInt(1, serviceID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                FeedBack feedback = new FeedBack(
                    resultSet.getInt("feedbackID"),
                    resultSet.getInt("clientID"),
                    resultSet.getInt("serviceProviderID"),
                    resultSet.getInt("bookingID"),
                    resultSet.getInt("serviceID"),
                    resultSet.getInt("rating"),
                    resultSet.getString("comments"),
                    resultSet.getTimestamp("feedbackDate")
                );
                feedbackList.add(feedback);
            }

            if (feedbackList.isEmpty()) {
                System.out.println("No feedback found for Service ID: " + serviceID);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all feedback for Service ID: " + e.getMessage());
            throw e;
        }

        return feedbackList;
    }

}
