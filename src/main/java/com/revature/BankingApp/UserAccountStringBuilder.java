package com.revature.BankingApp;

public class UserAccountStringBuilder {
	
	
	static String userName = "xavier0286";
	static String fullName = "Abslan Laverde";
	static StringBuilder userAccountEntry = new StringBuilder();
	
	public static void main(String[] args) {
	
	userAccountEntry.append("insert into useraccounts (username, password, name) values ('" 
	+ userName + "', '" + PasswordVerifier.password1 +"', '" + fullName + "')");
	
//	System.out.println(userAccountEntry);
	
	}
}
