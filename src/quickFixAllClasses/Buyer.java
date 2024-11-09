package quickFixAllClasses;
import java.util.HashMap;
import java.util.Map;
public class Buyer extends User {
	private Map<Integer,FeedBack> feedBack = new HashMap<>();
	private Map<Integer,Booking> booking = new HashMap<>();
	
	void bookService(Service serviceBooked) {
		//insert to booking hashMap
	}
	void provideFeedBack(int serviceID,int serviceRating,String serviceComments) {
		//insert to feedBack hashMap
	}
}
