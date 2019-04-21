package com.revature.BankingApp;

public class PasswordVerifier {

	static String password1 = "tr1f0rce";
	static String password2 = "";
	static String invalidVerification = "Passwords do not match.  Please re-enter password:";
	
	String verifyPassword(String password1, String password2) {
		if(!(password1.equals(password2))) 
		{
			return invalidVerification;
			//Will have to restart the password entry process
		}

		return password1;
	}
	
}
