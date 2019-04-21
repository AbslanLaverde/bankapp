package com.revature.views;

import com.revature.services.UserService;
import com.revature.util.ScannerUtil;

public class UserView implements View {

	UserService userService = new UserService();

	public View printOptions() {
		System.out.println("Thank you for choosing X(factor) United, N.A!");
		System.out.println("To start...");
		System.out.println("1. Create User");
		System.out.println("0. Back");

		int selection = ScannerUtil.getNumericChoice(1);

		switch (selection) {
		case 1:
			UserService.createUser();
			return new MainMenu();
			
		default:
			return new MainMenu();
		}
	}

}
