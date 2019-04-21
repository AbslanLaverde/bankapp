package com.revature.views;

import com.revature.services.UserService;
import com.revature.util.ScannerUtil;

public class LoginView implements View {

	UserService userService = new UserService();

	public View printOptions() {
		System.out.println("-----------------------------------------|");
		System.out.println("Login to your account!");
		System.out.println("To start...");
		System.out.println("-----------------------------------------|");
		System.out.println("1. Enter your Login information!");
		System.out.println("2. Forgot Username or Password?");
		System.out.println("0. Back");
		System.out.println("-----------------------------------------|");

		int selection = ScannerUtil.getNumericChoice(2);

		switch (selection) {
		case 1:
			UserService.loginUser();
			return new AccountView();
		case 2 :
			return new RecoveryView();
		default:
			return new MainMenu();
		}
	}


}
