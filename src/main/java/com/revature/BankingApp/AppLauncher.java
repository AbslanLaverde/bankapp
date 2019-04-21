package com.revature.BankingApp;

import java.sql.Connection;
import java.sql.SQLException;

import com.revature.util.ConnectionUtil;
import com.revature.views.MainMenu;
import com.revature.views.View;

public class AppLauncher {

	public static void main(String[] args) {
		
		
		View view = new MainMenu();
		
		while(view != null) {
			view = view.printOptions();
		}
		
		System.out.println("Thank you for banking with us!");
		
		
	}
}
