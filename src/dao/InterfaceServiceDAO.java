package dao;

import java.sql.SQLException;
import java.util.List;

import models.Service;

public interface InterfaceServiceDAO extends DAO<Service> {
	List<Service> getAllBySellerID(int sellerID) throws SQLException;
}
