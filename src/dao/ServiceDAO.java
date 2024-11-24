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

import models.Service;
import utils.AlertUtils;

public class ServiceDAO implements InterfaceServiceDAO {

	@Override
	public Service get(int id) throws SQLException {
	    String query = "SELECT * FROM Service WHERE serviceID = ?";
	    try (Connection connection = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	        preparedStatement.setInt(1, id);
	        ResultSet resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {
	            return new Service(
	                resultSet.getInt("serviceID"),
	                resultSet.getString("serviceName"),
	                resultSet.getString("serviceDescription"),
	                resultSet.getDouble("servicePrice"),
	                resultSet.getInt("ServiceProviderId"),
	                resultSet.getInt("serviceRating")
	            );
	        } else {
	            return null; // No service found
	        }
	    }
	}

	@Override
	public Map<Integer, Service> getAll() throws SQLException {
	    Map<Integer, Service> services = new HashMap<>();
	    String query = "SELECT * FROM Service";
	    
	    try (Connection connection = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	        ResultSet resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {
	            Service service = new Service(
	                resultSet.getInt("serviceID"),
	                resultSet.getString("serviceName"),
	                resultSet.getString("serviceDescription"),
	                resultSet.getDouble("servicePrice"),
	                resultSet.getInt("serviceIncrement"),
	                resultSet.getInt("serviceRating")
	            );
	            services.put(service.getServiceID(), service);
	        }
	    }
	    return services;
	}

	@Override
	public int insert(Service service) throws SQLException {
	    // Check if ServiceProvider has reached the limit of services
	    String checkQuery = "SELECT COUNT(*) FROM Service WHERE serviceProviderID = ?";
	    try (Connection connection = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(checkQuery)) {

	        preparedStatement.setInt(1, service.getServiceProviderID());
	        ResultSet resultSet = preparedStatement.executeQuery();
	        resultSet.next();
	        int serviceCount = resultSet.getInt(1);

//	        if (serviceCount >= 5) {  // Example limit of 5 services per provider
//	        	AlertUtils.showError("Service Limit Reached", "This service provider cannot have more than 5 services.");
//	            return -1;
//	        }
	    }

	    // If service count is below the limit, proceed with insertion
	    String insertQuery = "INSERT INTO Service (serviceName, serviceDescription, servicePrice,serviceProviderID ,serviceRating) VALUES ( ?, ?, ?, ?, ?)";
	    
	    try (Connection connection = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {

	        preparedStatement.setString(1, service.getServiceName());
	        preparedStatement.setString(2, service.getServiceDescription());
	        preparedStatement.setDouble(3, service.getServicePrice());
	        preparedStatement.setInt(4, service.getServiceProviderID());
	        preparedStatement.setInt(5, service.getServiceRating());

	        int rowsAffected = preparedStatement.executeUpdate();

	        if (rowsAffected > 0) {
	            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                return generatedKeys.getInt(1); // Return the generated serviceID
	            }
	        }
	        return 0; // Failed to insert
	    }
	}

	@Override
	public int update(Service service) throws SQLException {
	    String updateQuery = "UPDATE Service SET serviceName = ?, serviceDescription = ?, servicePrice = ?, serviceIncrement = ?, serviceRating = ? WHERE serviceID = ?";
	    
	    try (Connection connection = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

	        preparedStatement.setString(1, service.getServiceName());
	        preparedStatement.setString(2, service.getServiceDescription());
	        preparedStatement.setDouble(3, service.getServicePrice());
	        preparedStatement.setInt(4, service.getServiceIncrement());
	        preparedStatement.setInt(5, service.getServiceRating());
	        preparedStatement.setInt(6, service.getServiceID());

	        return preparedStatement.executeUpdate(); // Returns number of rows affected
	    }
	}

	@Override
	public int delete(Service service) throws SQLException {
	    String deleteQuery = "DELETE FROM Service WHERE serviceID = ?";
	    
	    try (Connection connection = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

	        preparedStatement.setInt(1, service.getServiceID());
	        return preparedStatement.executeUpdate(); // Returns number of rows affected
	    }
	}

	@Override
	public List<Service> getAllBySellerID(int sellerID) throws SQLException {
	    List<Service> services = new ArrayList<>();
	    String query = "SELECT s.*, u.name AS sellerName " +
	                   "FROM Service s " +
	                   "JOIN User u ON s.serviceProviderID = u.userID " +
	                   "WHERE s.serviceProviderID = ?";
	    
	    try (Connection connection = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	        preparedStatement.setInt(1, sellerID);
	        ResultSet resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {
	            services.add(new Service(
	                resultSet.getInt("serviceID"),
	                resultSet.getString("serviceName"),
	                resultSet.getString("serviceDescription"),
	                resultSet.getDouble("servicePrice"),
	                resultSet.getInt("serviceProviderID"),
	                resultSet.getInt("serviceRating"),
	                resultSet.getString("sellerName")
	            ));
	        }
	    }
	    return services;
	}

	@Override
	public List<Service> filterByServiceName(String name) throws SQLException {
	    List<Service> services = new ArrayList<>();
	    String query = "SELECT * FROM Service WHERE LOWER(serviceName) LIKE ?";
	    
	    try (Connection connection = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	        // Use wildcard search with case-insensitivity
	        preparedStatement.setString(1, "%" + name.toLowerCase() + "%");
	        ResultSet resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {
	            services.add(new Service(
	                resultSet.getInt("serviceID"),
	                resultSet.getString("serviceName"),
	                resultSet.getString("serviceDescription"),
	                resultSet.getDouble("servicePrice"),
	                resultSet.getInt("serviceIncrement"),
	                resultSet.getInt("serviceRating")
	            ));
	        }
	    }
	    return services;
	}

	@Override
	public List<Service> filterByLocation(String location) throws SQLException {
	    List<Service> services = new ArrayList<>();
	    
	    // Updated query to join the user table twice: once to get location, and once to get seller's name
	    String query = "SELECT service.*, u.name AS sellerName FROM service " +
	                   "JOIN serviceprovider sp ON service.serviceProviderID = sp.serviceProviderID " +
	                   "JOIN user u ON sp.serviceProviderID = u.userID " +
	                   "WHERE LOWER(u.location) LIKE ?";

	    try (Connection connection = DatabaseConnection.getInstance().getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	        // Use wildcard search with case-insensitivity for location
	        preparedStatement.setString(1, "%" + location.toLowerCase() + "%");

	        ResultSet resultSet = preparedStatement.executeQuery();

	        while (resultSet.next()) {
	            services.add(new Service(
	                resultSet.getInt("serviceID"),
	                resultSet.getString("serviceName"),
	                resultSet.getString("serviceDescription"),
	                resultSet.getDouble("servicePrice"),
	                resultSet.getInt("serviceProviderID"),  
	                resultSet.getInt("serviceRating"),
	                resultSet.getString("sellerName")  
	            ));
	        }
	    }
	    System.out.println("Services Found: " + services.size());

	    return services;
	}


}
