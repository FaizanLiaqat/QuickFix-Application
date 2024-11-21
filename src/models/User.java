package models;

public abstract class User {
	private int userID;
	private String userName;
	private String userPassword;
	private String userEmail;
	private String userPhoneNumber;
	private String userLocation;
	
	
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public String getUserPassword() {
		return userPassword;
	}



	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}



	public String getUserEmail() {
		return userEmail;
	}



	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}



	public String getUserPhoneNumber() {
		return userPhoneNumber;
	}



	public void setUserPhoneNumber(String userPhoneNumber) {
		this.userPhoneNumber = userPhoneNumber;
	}



	public String getUserLocation() {
		return userLocation;
	}



	public void setUserLocation(String userLocation) {
		this.userLocation = userLocation;
	}


	
	public abstract String getUserType();
	
}
