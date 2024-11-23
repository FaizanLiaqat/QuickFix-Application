package dao;

import java.sql.SQLException;
import java.util.List;

import models.Service;

public interface InterfaceServiceDAO extends DAO<Service> {
	List<Service> getAllBySellerID(int sellerID) throws SQLException;

	// Filter services by a partial or full serviceName
    List<Service> filterByServiceName(String name) throws SQLException;
    
    List<Service> filterByLocation(String location) throws SQLException;
}
