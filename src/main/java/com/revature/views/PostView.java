package com.revature.views;

import com.revature.services.UserService;
import com.revature.util.ScannerUtil;

public class PostView implements View {

	UserService userService = new UserService();

	public View printOptions() {
		System.out.println("Login to your account!");
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
