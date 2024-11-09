package quickFixAllClasses;

import java.util.HashMap;
import java.util.Map;

public class Seller extends User {
	private Map<Integer,Service> sellerServices = new HashMap<>();
	private Map<Integer,Booking> sellerBookings = new HashMap<>();
	private boolean available ;
	
	void addServices() {
		//add seller services to sellerServices hashMap
	}
	void updateJobStatus(int ServiceID,String newStatus) {
		// find the service in sellerBookings hashMap
		// update the status of that element in sellerBookings hashMap 
	}
}
