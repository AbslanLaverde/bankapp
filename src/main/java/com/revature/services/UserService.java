package com.revature.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.User;
import com.revature.daos.UserDao;
import com.revature.util.ConnectionUtil;
import com.revature.util.ScannerUtil;

public class UserService {
	
	static UserDao userDao = new UserDao();
	static List<String> usernameList = new ArrayList<>();

	
	/**
	 * Handles creation workflow for User
	 * bean
	 */
	public static void createUser() {
//		Username
		Boolean userExists = false;
		String userName = "";
		
		while(!userExists) {
			System.out.println("Please enter a username to associate with your accounts: ");
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
					System.out.println("That username is unavailable.  Please try again!");	
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
			System.out.println("Please enter a password: ");
			String passwordEntry = ScannerUtil.getLine();
			System.out.println("Please re-enter your password: ");
			String passwordReEntry = ScannerUtil.getLine();

			if(passwordEntry.equals(passwordReEntry)) {
				
				password += passwordEntry;
				passMatch = true;

			}
		
			else {
				System.out.println("The passwords you entered do not match.");
				System.out.println("Please try again!");
				}
		}
		
//		name
		System.out.println("Please enter your full name: " );
		String fullName = ScannerUtil.getLine();
		
		// Validate all this data
		
		User user = new User(fullName, password, userName);
		
		user = userDao.saveUser(user);
		System.out.println("You have successfully created an Online Account!");
		System.out.println("You will now be sent back to the Main Menu.");
		System.out.println("Please log in using your new account credentials.");
	}
	
	public void loginUser() {
		
		
	}
	
	
}
