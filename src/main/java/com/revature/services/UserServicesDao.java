package com.revature.services;

import com.revature.views.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.revature.beans.User;
import com.revature.daos.UserDao;
import com.revature.util.ConnectionUtil;
import com.revature.util.ScannerUtil;
import com.revature.views.LoginView;
import com.revature.views.MainMenu;
import com.revature.views.View;

public class UserServicesDao {
	
	static UserDao userDao = new UserDao();
	static List<String> usernameList = new ArrayList<>();
	static List<String> fullnameList = new ArrayList<>();
	static List<String> passwordList = new ArrayList<>();
	static Map<String, String> usernamePassword = new HashMap<>();
	static Map<String, String> namePassword = new HashMap<>();
	static Map<String, String> usernameName = new HashMap<>();
	public static String userview = "";
	public static String usernameview = "";
	
	/**
	 * Handles creation workflow for User
	 * bean
	 */
	public static void createUser() {
//		name
		Boolean nameExists = false;
		String fullName = "";
		
		System.out.println("-----------------------------------------|");
		System.out.println("Please enter your first and last name: " );
		System.out.println("-----------------------------------------|");
		String fullNameEntry = ScannerUtil.getLine();
		
		while(!nameExists) {
			try(Connection connection = ConnectionUtil.getConnection()){
				String sql = "Select name FROM useraccounts";
				PreparedStatement ps = connection.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					fullnameList.add(rs.getString(1));
				}
				if(fullnameList.contains(fullNameEntry)) {
					
					System.out.println("-----------------------------------------|");
					System.out.println("That name is already associated with a");
					System.out.println("user profile for an Online Account.");
					System.out.println("You will now be directed to the Main");
					System.out.println("Menu.  Please login with your username");
					System.out.println("and password.");
					System.out.println("-----------------------------------------|");
					
					View view = new MainMenu();
					
					while(view != null) {
						view = view.printOptions();
					}
					
				}
				else {
					fullName += fullNameEntry;
					nameExists = true;
				}
			
			
			
			
			}catch(SQLException e) {
				
			}
			
		}
		
		
		
		
		
//		Username
		Boolean userExists = false;
		String userName = "";
		
		while(!userExists) {
			System.out.println("-----------------------------------------|");
			System.out.println("Please enter a username to associate with your accounts: ");
			System.out.println("-----------------------------------------|");
			String userNameEntry = ScannerUtil.getLine();

			try(Connection connection = ConnectionUtil.getConnection()){
				String sql = "SELECT username FROM useraccounts";
				PreparedStatement ps = connection.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					usernameList.add(rs.getString(1));
					usernameList.add(rs.getString(1).toLowerCase());
				}
				
				if(usernameList.contains(userNameEntry)) {
					System.out.println("-----------------------------------------|");
					System.out.println("That username is unavailable.  Please try again!");	
					System.out.println("-----------------------------------------|");

				}
				else 
				{
					userName += userNameEntry;
					userExists = true;
				}
				
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
//		Password
		Boolean passMatch = false;
		String password = "";

		while(!passMatch) {
			System.out.println("-----------------------------------------|");
			System.out.println("Please enter a password: ");
			System.out.println("-----------------------------------------|");
			String passwordEntry = ScannerUtil.getLine();

			System.out.println("-----------------------------------------|");
			System.out.println("Please re-enter your password: ");
			System.out.println("-----------------------------------------|");
			String passwordReEntry = ScannerUtil.getLine();

			if(passwordEntry.equals(passwordReEntry)) {
				
				password += passwordEntry;
				passMatch = true;

			}
		
			else {
				System.out.println("-----------------------------------------|");
				System.out.println("The passwords you entered do not match.");
				System.out.println("Please try again!");
				System.out.println("-----------------------------------------|");

				}
		}
		
		
		User user = new User(fullName, password, userName);
		
		user = userDao.saveUser(user);
		
		System.out.println("-----------------------------------------|");
		System.out.println("You have successfully created an Online Account!");
		System.out.println("You will now be sent back to the Main Menu.");
		System.out.println("Please log in using your new account credentials.");
		System.out.println("-----------------------------------------|");
	}
	
	public static void loginUser() {
		Boolean userPassMatch = false;
		
		while(!userPassMatch) {
		
			try(Connection connection = ConnectionUtil.getConnection()){
				String sql = "SELECT username, password FROM useraccounts";
				PreparedStatement ps = connection.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					usernamePassword.put(rs.getString(1), rs.getString(2));
					usernameList.add(rs.getString(1));
					}
				String sql2 = "Select username, name FROM useraccounts";
				PreparedStatement ps2 = connection.prepareStatement(sql2);
				ResultSet rs2 = ps2.executeQuery();
				while (rs2.next()) {
					usernameName.put(rs2.getString(1), rs2.getString(2));
				}
				System.out.println("-----------------------------------------|");
				System.out.println("Please enter your username:");
				System.out.println("-----------------------------------------|");

				String userNameEntry = ScannerUtil.getLine();
			
				if(usernameList.contains(userNameEntry)) {
					
					
					System.out.println("-----------------------------------------|");
					System.out.println("Please enter your password:");
					System.out.println("-----------------------------------------|");

					String passwordEntry = ScannerUtil.getLine();
				
					if((usernamePassword.get(userNameEntry).equals(passwordEntry))) {
						userPassMatch = true;
						userview = usernameName.get(userNameEntry);
						usernameview = userNameEntry;
					
						System.out.println("-----------------------------------------|");
						System.out.println("Login successful!  You will now be"); 
						System.out.println("taken to your Accounts.");
						System.out.println("-----------------------------------------|");
					}
				
					else {
						System.out.println("-----------------------------------------|");
						System.out.println("Username and password do not match.");
						System.out.println("Please try again.");
						System.out.println("-----------------------------------------|");
					}
				}
				else 
				{
					System.out.println("That Username is not associated with a ");
					System.out.println("user profile.");
					
					System.out.println("-----------------------------------------|");
					System.out.println("1. Enter 1 to try again.");
					System.out.println("0. Return to Main Menu and ");
					System.out.println("Sign Up for an Online Account.");
					System.out.println("-----------------------------------------|");
					
					int selection = ScannerUtil.getNumericChoice(1);
					
					switch(selection) {
					
						case 1:
							break;
						
						default :
							View view = new MainMenu();
							while(view != null) {
								view = view.printOptions();
						}
							;
					}
					
					
				}
			
			}catch(SQLException e){
				e.printStackTrace();
				}
			
			
			
		}
	
	}
	
	
	
	
	
	public static void retrieveUsername() {
		Boolean namePassMatch = false;
		
		while(!namePassMatch) {
		
			try(Connection connection = ConnectionUtil.getConnection()){
				String sql = "SELECT username, password FROM useraccounts";
				PreparedStatement ps = connection.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					usernamePassword.put(rs.getString(2), rs.getString(1));
//					System.out.println(usernamePassword);
					}
				String sql2 = "Select password, name FROM useraccounts";
				PreparedStatement ps2 = connection.prepareStatement(sql2);
				ResultSet rs2 = ps2.executeQuery();
				while (rs2.next()) {
					namePassword.put(rs2.getString(2), rs2.getString(1));
					fullnameList.add(rs2.getString(2));
				}
						
				System.out.println("-----------------------------------------|");
				System.out.println("Please enter your name:");
				System.out.println("-----------------------------------------|");

				String nameEntry = ScannerUtil.getLine();
			
				if(fullnameList.contains(nameEntry)) {
				
					System.out.println("-----------------------------------------|");
					System.out.println("Please enter your password:");
					System.out.println("-----------------------------------------|");

					String passwordEntry = ScannerUtil.getLine();
				
					if((namePassword.get(nameEntry).equals(passwordEntry))) {
						namePassMatch = true;
					
						System.out.println("-----------------------------------------|");
						System.out.println("Your username is:");
						System.out.println(usernamePassword.get(passwordEntry));
						System.out.println("You are being redirected to the Login screen.");
						System.out.println("Please choose the option to enter your login information");
						System.out.println("and use the username that has been provided.");;
						System.out.println("-----------------------------------------|");
					}
				
					else {
						System.out.println("-----------------------------------------|");
						System.out.println("That name and password are not associated to a username.");
						System.out.println("Please try again.");
						System.out.println("-----------------------------------------|");

					}
				}
				else {
				
					System.out.println("That name is not associated with a ");
					System.out.println("user profile.");
					
					System.out.println("-----------------------------------------|");
					System.out.println("1. Enter 1 to try again.");
					System.out.println("0. Return to Main Menu and ");
					System.out.println("Sign Up for an Online Account.");
					System.out.println("-----------------------------------------|");
					
					int selection = ScannerUtil.getNumericChoice(1);
					
					switch(selection) {
					
						case 1:
							break;
						
						default :
							View view = new MainMenu();
							while(view != null) {
								view = view.printOptions();
						}
					}			
					
					
				}
			}catch(SQLException e){
				e.printStackTrace();
				}
		}

		
	}

	
	public static void retrievePassword() {
		Boolean nameUserMatch = false;
		
		while(!nameUserMatch) {
		
			try(Connection connection = ConnectionUtil.getConnection()){
				String sql = "SELECT username, password FROM useraccounts";
				PreparedStatement ps = connection.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					usernamePassword.put(rs.getString(1), rs.getString(2));
					
					}
				String sql2 = "Select username, name FROM useraccounts";
				PreparedStatement ps2 = connection.prepareStatement(sql2);
				ResultSet rs2 = ps2.executeQuery();
				while (rs2.next()) {
					usernameName.put(rs2.getString(2), rs2.getString(1));
					fullnameList.add(rs2.getString(2));
					
				}

				System.out.println("-----------------------------------------|");
				System.out.println("Please enter your name:");
				System.out.println("-----------------------------------------|");

				String nameEntry = ScannerUtil.getLine();				
				
				if(fullnameList.contains(nameEntry)) {
				

					System.out.println("-----------------------------------------|");
					System.out.println("Please enter your username:");
					System.out.println("-----------------------------------------|");

					String usernameEntry = ScannerUtil.getLine();

					
					
					//
					if((usernameName.get(nameEntry).equals(usernameEntry))) {
						
						System.out.println("-----------------------------------------|");
						System.out.println("Your password is:");
						System.out.println(usernamePassword.get(usernameEntry));
						System.out.println("You are being redirected to the Login screen.");
						System.out.println("Please choose the option to enter your login information");
						System.out.println("and use the username that has been provided.");;
						System.out.println("-----------------------------------------|");
						
						nameUserMatch = true;
					}
				
					else {
						
						System.out.println("-----------------------------------------|");
						System.out.println("That name and username are not associated to a password.");
						System.out.println("Please try again.");
						System.out.println("-----------------------------------------|");
						
					}
					
				}
				else {
					
					System.out.println("That name is not associated with a ");
					System.out.println("user profile.");
					
					System.out.println("-----------------------------------------|");
					System.out.println("1. Enter 1 to try again.");
					System.out.println("0. Return to Main Menu and ");
					System.out.println("Sign Up for an Online Account.");
					System.out.println("-----------------------------------------|");
					
					int selection = ScannerUtil.getNumericChoice(1);
					
					switch(selection) {
					
						case 1:
							break;
						
						default :
							View view = new MainMenu();
							while(view != null) {
								view = view.printOptions();
							}
						}
					}
			
			
			}catch(SQLException e){
				e.printStackTrace();
				}
		}
	}
	
	
	
	
	
	
}
