package models;

import java.math.BigDecimal;

public class Admin extends User {
	private String adminEmail;
	private String admintPassword;
	private BigDecimal amount;
	
	 public String getAmount() {
	        return amount != null ? amount.toPlainString() : "0.00";
	    }

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

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
