package com.revature.services;

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

public class UserService {
	
	static UserDao userDao = new UserDao();
	static List<String> usernameList = new ArrayList<>();
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
		
//		name
		System.out.println("-----------------------------------------|");
		System.out.println("Please enter your first and last name: " );
		System.out.println("-----------------------------------------|");
		String fullName = ScannerUtil.getLine();
		
		
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
			
				System.out.println("-----------------------------------------|");
				System.out.println("Please enter your password:");
				System.out.println("-----------------------------------------|");

				String passwordEntry = ScannerUtil.getLine();
				
				if((usernamePassword.get(userNameEntry).equals(passwordEntry))) {
					userPassMatch = true;
					userview = usernameName.get(userNameEntry);
					usernameview = userNameEntry;
					
					System.out.println("-----------------------------------------|");
					System.out.println("Login successful!  You will now be taken to your Accounts.");
					System.out.println("-----------------------------------------|");
				}
				
				else {
					System.out.println("-----------------------------------------|");
					System.out.println("Username and password do not match.");
					System.out.println("Please try again.");
					System.out.println("-----------------------------------------|");

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
					
				}
						
				System.out.println("-----------------------------------------|");
				System.out.println("Please enter your name:");
				System.out.println("-----------------------------------------|");

				String nameEntry = ScannerUtil.getLine();
			
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
//					System.out.println(usernamePassword);
					}
				String sql2 = "Select username, name FROM useraccounts";
				PreparedStatement ps2 = connection.prepareStatement(sql2);
				ResultSet rs2 = ps2.executeQuery();
				while (rs2.next()) {
					usernameName.put(rs2.getString(2), rs2.getString(1));
					
				}
						
				System.out.println("-----------------------------------------|");
				System.out.println("Please enter your name:");
				System.out.println("-----------------------------------------|");

				String nameEntry = ScannerUtil.getLine();
			
				System.out.println("-----------------------------------------|");
				System.out.println("Please enter your username:");
				System.out.println("-----------------------------------------|");

				String usernameEntry = ScannerUtil.getLine();
				
				if((usernameName.get(nameEntry).equals(usernameEntry))) {
					nameUserMatch = true;
					
					System.out.println("-----------------------------------------|");
					System.out.println("Your password is:");
					System.out.println(usernamePassword.get(usernameEntry));
					System.out.println("You are being redirected to the Login screen.");
					System.out.println("Please choose the option to enter your login information");
					System.out.println("and use the username that has been provided.");;
					System.out.println("-----------------------------------------|");
				}
				
				else {
					System.out.println("-----------------------------------------|");
					System.out.println("That name and username are not associated to a password.");
					System.out.println("Please try again.");
					System.out.println("-----------------------------------------|");

				}
			
			
			}catch(SQLException e){
				e.printStackTrace();
				}
		}
	}
	
	
	public static void openAccount() {
		System.out.println("Open account methods.");
	}
	
	public static void makeDeposit() {
		System.out.println("Make deposit methods.");
	}
	
	public static void makeWithdrawal() {
		System.out.println("Make a withdrawal method.");
	}
	
	public static void makeTransfer() {
		System.out.println("Make a transfer method.");
	}
	
	
	
}
