package com.revature.views;

import com.revature.util.ScannerUtil;

public class MainMenu implements View {

	public View printOptions() {
		System.out.println("Welcome to X(factor)United, N.A."+
	"\n"+"Please choose from the following options:");
		System.out.println("1. Login to your Online Account!");
		System.out.println("2. Sign Up for an Online Account!");
		System.out.println("0. Quit.");
	
		int selection = ScannerUtil.getNumericChoice(2);
		
		switch(selection) {
		case 1: return new PostView();
		case 2: return new UserView();
		default: return null;
		}
		
	}

}
