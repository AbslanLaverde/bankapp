package com.revature.views;

import com.revature.util.ScannerUtil;

public class MainMenu implements View {

	public View printOptions() {
		System.out.println("-----------------------------------------------|");
		System.out.println("Welcome to X(factor)United, N.A."+
				"\n"+"Please choose from the following options:");
		System.out.println("-----------------------------------------------|");
		System.out.println("1. Login to your Online Account!");
		System.out.println("2. Sign Up for an Online Account!");
		System.out.println("0. Quit.");
		System.out.println("-----------------------------------------------|");
	
				
		int selection = ScannerUtil.getNumericChoice(2);
		
				
		switch(selection) {
		case 1: return new LoginView();
		case 2: return new SignUpView();
		default: return null;
		}
		
	}

}
