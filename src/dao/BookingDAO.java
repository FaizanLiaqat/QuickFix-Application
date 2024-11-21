package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import models.Booking;

public class BookingDAO implements InterfaceBookingDAO {

	@Override
	public Booking get(int id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Integer, Booking> getAll() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(Booking booking) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Booking booking) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Booking booking) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public List<Booking> getAllByBuyerID(int buyerID) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	
	public List<Booking> getAllBySellerID(int sellerID) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
