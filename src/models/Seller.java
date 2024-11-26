package models;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Seller extends User {
	private Map<Integer,Service> sellerServices = new HashMap<>();
	private Map<Integer,Booking> sellerBookings = new HashMap<>();
	private boolean available ;
	private BigDecimal amount;
	
	 public String getAmount() {
	        return amount != null ? amount.toPlainString() : "0.00";
	    }

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
    // Add services to the seller's service map (if needed in the future)
    void addServices(Service service) {
        this.sellerServices.put(service.getServiceID(), service);
    }
    
	public Map<Integer, Service> getSellerServices() {
		return sellerServices;
	}

	public void setSellerServices(Map<Integer, Service> sellerServices) {
		this.sellerServices = sellerServices;
	}

	public Map<Integer, Booking> getSellerBookings() {
		return sellerBookings;
	}

	public void setSellerBookings(Map<Integer, Booking> sellerBookings) {
		this.sellerBookings = sellerBookings;
	}

	void updateJobStatus(int ServiceID,String newStatus) {
		// find the service in sellerBookings hashMap
		// update the status of that element in sellerBookings hashMap 
	}
	
	public boolean isAvailable() {
		return available;
	}
	
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
	@Override
	public String getUserType() {
		
		return "Seller";
	}
	
}
