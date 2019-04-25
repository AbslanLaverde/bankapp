package com.revature.services;

import com.revature.views.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.revature.beans.User;
import com.revature.daos.UserDao;
import com.revature.util.ConnectionUtil;
import com.revature.util.ScannerUtil;
import com.revature.views.LoginView;
import com.revature.views.MainMenu;
import com.revature.views.View;

public class UserServicesDao {
	
	static UserDao userDao = new UserDao();
	static Set<String> usernameSet = new HashSet<>();
	static Set<String> fullnameSet = new HashSet<>();
	static Set<String> passwordSet = new HashSet<>();
	static Map<String, String> usernamePassword = new HashMap<>();
	static Map<String, String> namePassword = new HashMap<>();
	static Map<String, String> usernameName = new HashMap<>();
	public static String userview = "";
	public static String usernameview = "";
	public static Boolean loginSuccessful = false;
	
	/**
	 * Handles creation workflow for User
	 * bean
	 */
	public static void createUser() {
//		name
		Boolean nameExists = false;
		String fullName = "";
		int failedAttempt = 0;
		
		System.out.println("-----------------------------------------------|");
		System.out.println("Please enter your first and last name: " );
		System.out.println("-----------------------------------------------|");
		String fullNameEntry = ScannerUtil.getLine();
		
		while((!nameExists) && (failedAttempt==0)) {
			try(Connection connection = ConnectionUtil.getConnection()){
				String sql3 = "Select name FROM useraccounts";
				PreparedStatement ps = connection.prepareStatement(sql3);
				ResultSet rs3 = ps.executeQuery();
				while (rs3.next()) {
					fullnameSet.add(rs3.getString(1));
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
				if(fullnameSet.contains(fullNameEntry)) {
					
					System.out.println("-----------------------------------------------|");
					System.out.println("That name is already associated with a");
					System.out.println("user profile for an Online Account.");
					System.out.println("You will now be directed to the Main");
					System.out.println("Menu.  Please login with your username");
					System.out.println("and password.");
					System.out.println("-----------------------------------------------|");
					
					loginSuccessful = false;
					failedAttempt = 1;
					
					
					
				}
				else {
					fullName += fullNameEntry;
					nameExists = true;
				}
		}


//		Username
		Boolean userExists = false;
		String userName = "";
		
		while((!userExists)&&(nameExists==true)) {
			System.out.println("-----------------------------------------------|");
			System.out.println("Please enter a username to associate"); 
			System.out.println("with your accounts: ");
			System.out.println("**Usernames are case-sensitive**");
			System.out.println("-----------------------------------------------|");
			String userNameEntry = ScannerUtil.getLine();

			try(Connection connection = ConnectionUtil.getConnection()){
				String sql = "SELECT username FROM useraccounts";
				PreparedStatement ps = connection.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					usernameSet.add(rs.getString(1));
					usernameSet.add(rs.getString(1).toLowerCase());
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
				
				if(usernameSet.contains(userNameEntry)) {
					System.out.println("-----------------------------------------------|");
					System.out.println("That username is unavailable.  Please try again!");	
					System.out.println("-----------------------------------------------|");

				}
				else 
				{
					userName += userNameEntry;
					userExists = true;
				}
				
			
		}
		
//		Password
		Boolean passMatch = false;
		String password = "";

		while((!passMatch) && (userExists==true)) {
			System.out.println("-----------------------------------------------|");
			System.out.println("Please enter a password: ");
			System.out.println("-----------------------------------------------|");
			String passwordEntry = ScannerUtil.getLine();

			System.out.println("-----------------------------------------------|");
			System.out.println("Please re-enter your password: ");
			System.out.println("-----------------------------------------------|");
			String passwordReEntry = ScannerUtil.getLine();

			if(passwordEntry.equals(passwordReEntry)) {
				
				password += passwordEntry;
				passMatch = true;
				
				User user = new User(fullName, password, userName);
				
				user = userDao.saveUser(user);
				
				System.out.println("-----------------------------------------------|");
				System.out.println("You have successfully created an Online Account!");
				System.out.println("You will now be sent back to the Main Menu.");
				System.out.println("Please log in using your new account credentials.");
				System.out.println("-----------------------------------------------|");
				
			}
		
			else {
				System.out.println("-----------------------------------------------|");
				System.out.println("The passwords you entered do not match.");
				System.out.println("Please try again!");
				System.out.println("-----------------------------------------------|");

				}
			
		}
		
		
		
	}
	
	
	
	public static void loginUser() {
		Boolean userPassMatch = false;
		int failedAttempt = 0;
		
		while((!userPassMatch)&&(failedAttempt < 3)) {
		
			try(Connection connection = ConnectionUtil.getConnection()){
				String sql = "SELECT username, password FROM useraccounts";
				PreparedStatement ps = connection.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					usernamePassword.put(rs.getString(1), rs.getString(2));
					usernameSet.add(rs.getString(1));
					}
				String sql2 = "Select username, name FROM useraccounts";
				PreparedStatement ps2 = connection.prepareStatement(sql2);
				ResultSet rs2 = ps2.executeQuery();
				while (rs2.next()) {
					usernameName.put(rs2.getString(1), rs2.getString(2));
				}
			}catch(SQLException e){
				e.printStackTrace();
				}
			
				System.out.println("-----------------------------------------------|");
				System.out.println("Please enter your username:");
				System.out.println("-----------------------------------------------|");

				String userNameEntry = ScannerUtil.getLine();
			
				if(usernameSet.contains(userNameEntry)) {
					
					
					System.out.println("-----------------------------------------------|");
					System.out.println("Please enter your password:");
					System.out.println("-----------------------------------------------|");

					String passwordEntry = ScannerUtil.getLine();
				
					if((usernamePassword.get(userNameEntry).equals(passwordEntry))) {
						userPassMatch = true;
						userview = usernameName.get(userNameEntry);
						usernameview = userNameEntry;
					
//						System.out.println("-----------------------------------------------|");
//						System.out.println("Login successful!  You will now be"); 
//						System.out.println("taken to your Accounts.");
						System.out.println("-----------------------------------------------|");
						System.out.println("Login successful!");
						System.out.println("-----------------------------------------------|");

						loginSuccessful = true;
						
					}
				
					else {
						failedAttempt++;
						if(failedAttempt == 3) {
							System.out.println("-----------------------------------------------|");
							System.out.println("You have entered three failed attempts.");
							System.out.println("For security purposes, you are being");
							System.out.println("directed back to the Main Menu.");
							System.out.println("If you've forgotten your username or password");
							System.out.println("select the appropriate option to retrieve");
							System.out.println("your login credentials.");
							System.out.println("-----------------------------------------------|");

							
							loginSuccessful = false;
							
						}else 
						{
							System.out.println("-----------------------------------------------|");
							System.out.println("Username and password do not match.");
							System.out.println("Please try again.");
							System.out.println("-----------------------------------------------|");
							
							
						}
					} 
				}
				else 
				{
					System.out.println("That Username is not associated with a ");
					System.out.println("user profile.");
					
					System.out.println("-----------------------------------------------|");
					System.out.println("1. Enter 1 to try again.");
					System.out.println("0. Return to Main Menu and ");
					System.out.println("Sign Up for an Online Account.");
					System.out.println("-----------------------------------------------|");
					
					int selection = ScannerUtil.getNumericChoice(1);
					
					switch(selection) {
					
						case 1:
							break;
						
						default :
							loginSuccessful = false;
							failedAttempt = 5;
							break;
					}
					
					
				}
		}
		
	}
	
	
	
	
	
	public static void retrieveUsername() {
		Boolean namePassMatch = false;
		int failedAttempt = 0;
		
		while((!namePassMatch) && (failedAttempt<3)) {
		
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
					fullnameSet.add(rs2.getString(2));
//					System.out.println(namePassword);
//					System.out.println(fullnameSet);
					}
			}catch(SQLException e){
				e.printStackTrace();
				}
						
				
				System.out.println("-----------------------------------------------|");
				System.out.println("Please enter your name:");
				System.out.println("-----------------------------------------------|");

				String nameEntry = ScannerUtil.getLine();
			
				if(fullnameSet.contains(nameEntry)) {
				
					System.out.println("-----------------------------------------------|");
					System.out.println("Please enter your password:");
					System.out.println("-----------------------------------------------|");

					String passwordEntry = ScannerUtil.getLine();
				
					if((namePassword.get(nameEntry).equals(passwordEntry))) {
						namePassMatch = true;
					
						System.out.println("-----------------------------------------------|");
						System.out.println("Your username is:");
						System.out.println(usernamePassword.get(passwordEntry));
						System.out.println("You are being redirected to the Login screen.");
						System.out.println("Please choose the option to enter your login");
						System.out.println("information and use the username provided.");;
						System.out.println("-----------------------------------------------|");
						loginSuccessful = true;
					}
				
					else {
						failedAttempt++;
						if(failedAttempt == 3) {
							System.out.println("-----------------------------------------------|");
							System.out.println("You have entered three failed attempts.");
							System.out.println("For security purposes, you are being");
							System.out.println("directed back to the Main Menu.");
							System.out.println("Please call our customer service number");
							System.out.println("1(800) 932-2867");
							System.out.println("if you require additional assistance");
							System.out.println("accessing your account.  Thank you.");
							System.out.println("-----------------------------------------------|");

							
							loginSuccessful = false;
							
						}else 
						{
							System.out.println("-----------------------------------------------|");
							System.out.println("That name and password are not associated to");
							System.out.println("a username in our databse.");
							System.out.println("Please try again.");
							System.out.println("-----------------------------------------------|");
							
							
						}


					}
				}
				else {
				
					System.out.println("That name is not associated with a ");
					System.out.println("user profile.");
					
					System.out.println("-----------------------------------------------|");
					System.out.println("1. Enter 1 to try again.");
					System.out.println("0. Return to Main Menu and ");
					System.out.println("Sign Up for an Online Account.");
					System.out.println("-----------------------------------------------|");
					
					int selection = ScannerUtil.getNumericChoice(1);
					
					switch(selection) {
					
						case 1:
							break;
						
						default :
							loginSuccessful = false;
							failedAttempt = 5;
							break;
					}			
					
					
				}
			
		}
		
	}

	
	public static void retrievePassword() {
		Boolean nameUserMatch = false;
		int failedAttempt = 0;
		
		while((!nameUserMatch)&&(failedAttempt<3)) {
		
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
					fullnameSet.add(rs2.getString(2));
				}
			}catch(SQLException e){
				e.printStackTrace();
				}	

				System.out.println("-----------------------------------------------|");
				System.out.println("Please enter your name:");
				System.out.println("-----------------------------------------------|");

				String nameEntry = ScannerUtil.getLine();				
				
				if(fullnameSet.contains(nameEntry)) {
				

					System.out.println("-----------------------------------------------|");
					System.out.println("Please enter your username:");
					System.out.println("-----------------------------------------------|");

					String usernameEntry = ScannerUtil.getLine();

					
					
					//
					if((usernameName.get(nameEntry).equals(usernameEntry))) {
						
						System.out.println("-----------------------------------------------|");
						System.out.println("Your password is:");
						System.out.println(usernamePassword.get(usernameEntry));
						System.out.println("You are being redirected to the Login screen.");
						System.out.println("Please choose the option to enter your login");
						System.out.println("information and use the password provided.");;
						System.out.println("-----------------------------------------------|");
						
						loginSuccessful = true;
					}
				
					else {
						
						failedAttempt++;
						if(failedAttempt == 3) {
							System.out.println("-----------------------------------------------|");
							System.out.println("You have entered three failed attempts.");
							System.out.println("For security purposes, you are being");
							System.out.println("directed back to the Main Menu.");
							System.out.println("Please call our customer service number");
							System.out.println("1(800) 932-2867");
							System.out.println("if you require additional assistance");
							System.out.println("accessing your account.  Thank you.");
							System.out.println("-----------------------------------------------|");

							
							loginSuccessful = false;
							
						}else 
						{
							System.out.println("-----------------------------------------------|");
							System.out.println("That name and username are not associated to");
							System.out.println("a password in our records.");
							System.out.println("Please try again.");
							System.out.println("-----------------------------------------------|");
							
							
						}

					}
					
				}
				else {
					
					System.out.println("That name is not associated with a ");
					System.out.println("user profile.");
					
					System.out.println("-----------------------------------------------|");
					System.out.println("1. Enter 1 to try again.");
					System.out.println("0. Return to Main Menu and ");
					System.out.println("Sign Up for an Online Account.");
					System.out.println("-----------------------------------------------|");
					
					int selection = ScannerUtil.getNumericChoice(1);
					
					switch(selection) {
					
						case 1:
							break;
						
						default :
							loginSuccessful = false;
							failedAttempt = 5;
							break;
						}
					}
			
			
			
				}
		}
	
	
	
	
	
	
}
