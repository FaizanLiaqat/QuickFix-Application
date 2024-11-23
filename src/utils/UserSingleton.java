package utils;

import models.Buyer;
import models.Seller;
import models.User;

public class UserSingleton {
	private static final UserSingleton instance = new UserSingleton();
	private User user;
	
	private UserSingleton() {}
	
	public static UserSingleton getInstance() {
		return instance;
	}
	
	public void setUserObject(String userType) {
		if(userType.equalsIgnoreCase("Buyer")) {
		this.user = new Buyer();
		}else if (userType.equalsIgnoreCase("Seller")) {
			this.user = new Seller();
		}
	}
	public User getUserObject() {
		return this.user;
	}
	
	 public void setUserObject(User userObject) {
	        this.user = userObject;
	    }
}
