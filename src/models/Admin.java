package models;

public class Admin extends User {

	@Override
	public String getUserType() {
		return "Admin";
	}

}
