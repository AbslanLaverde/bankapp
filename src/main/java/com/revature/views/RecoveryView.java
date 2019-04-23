package com.revature.views;

import com.revature.services.UserServicesDao;
import com.revature.util.ScannerUtil;

public class RecoveryView implements View {

	UserServicesDao userService = new UserServicesDao();
	
	public View printOptions() {
		System.out.println("-----------------------------------------|");
		System.out.println("Please choose from the following options:");
		System.out.println("-----------------------------------------|");
		System.out.println("1. Forgot username.");
		System.out.println("2. Forgot password.");
		System.out.println("0. Back");
		System.out.println("-----------------------------------------|");
		
		
		
		int selection = ScannerUtil.getNumericChoice(2);
		
		switch(selection) {
		case 1:
			UserServicesDao.retrieveUsername();
			return new LoginView();
		case 2: 
			UserServicesDao.retrievePassword();
			return new LoginView();
		default: return null;
		}
	}

	
}
