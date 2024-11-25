package models;

public class Admin extends User {
	private String adminEmail;
	private String admintPassword;
	
	public String getAdminEmail() {
		return adminEmail;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public String getAdmintPassword() {
		return admintPassword;
	}

	public void setAdmintPassword(String admintPassword) {
		this.admintPassword = admintPassword;
	}

	@Override
	public String getUserType() {
		return "Admin";
	}

}
