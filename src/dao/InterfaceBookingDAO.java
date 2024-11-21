package dao;

import java.sql.SQLException;
import java.util.List;

import models.Booking;

public interface InterfaceBookingDAO extends DAO<Booking> {
	List<Booking> getAllByBuyerID(int buyerID) throws SQLException;
    List<Booking> getAllBySellerID(int sellerID) throws SQLException;
}
