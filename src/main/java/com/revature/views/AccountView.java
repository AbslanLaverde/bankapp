package com.revature.views;

import com.revature.services.UserServicesDao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.revature.daos.UserDao;
import com.revature.util.ConnectionUtil;
import com.revature.util.ScannerUtil;

public class AccountView implements View {

	HashMap<Integer, Integer> accounts = new HashMap<>();
	
	public View printOptions() {
	Boolean hasAccount = false;
	Boolean openSuccessful = false;
	
		try(Connection connection = ConnectionUtil.getConnection()){
			String sql4 = "Select username from usertobank where username = ?;";
			PreparedStatement ps4 = connection.prepareStatement(sql4);
			ps4.setString(1, UserServicesDao.usernameview);
			ResultSet rs4 = ps4.executeQuery();
			if(rs4.next()) hasAccount = true;
			}catch(SQLException e) {
		
			}
		
		if(hasAccount) {	
		
			System.out.println("-----------------------------------------------|");
			System.out.println("Welcome " + UserServicesDao.userview +"!");
			System.out.println("Here is a summary of your accounts.");
			System.out.println("-----------------------------------------------|");
		
			try(Connection connection = ConnectionUtil.getConnection()){
				String sql = "select accountnumber, balance\r\n" + 
					"from usertobank join bankaccounts\r\n" + 
					"on usertobank.accounts = accountnumber\r\n" + 
					"where username = ?;";
				PreparedStatement ps = connection.prepareStatement(sql);
			
				ps.setString(1, UserServicesDao.usernameview);
			
				ResultSet rs = ps.executeQuery();
				while(rs.next()) {
					int accountNum = rs.getInt(1);
					BigDecimal balance = rs.getBigDecimal(2);
					System.out.println("-----------------------------------------------|");
					System.out.println("Account# " + accountNum + "    |" + "Balance: " + balance);
					System.out.println("-----------------------------------------------|");
				}
				}catch (SQLException e) {
				e.printStackTrace();
				}
			}
			else {
				System.out.println("-----------------------------------------------|");
				System.out.println("Welcome " + UserServicesDao.userview +"!");
				System.out.println("You currently have no accounts");
				System.out.println("with out banks.  Please select option 1");
				System.out.println("to open your first X(factor) Bank account!");
				System.out.println("-----------------------------------------------|");

			}
			System.out.println("What would you like to do?");
			System.out.println("-----------------------------------------------|");
			System.out.println("1. Open a new account.");
			System.out.println("2. Make a deposit.");
			System.out.println("3. Make a withdrawl.");
			System.out.println("4. Transfer funds to another account.");
			System.out.println("5. Log off and return to Main Menu.");
			System.out.println("-----------------------------------------------|");

			
			int selection = ScannerUtil.getNumericChoice(5);
			
			switch(selection) {
				case 1: UserDao.openAccount();
					return new AccountView();
				
				case 2: 
					if(hasAccount){
					UserDao.makeDeposit();
					return new AccountView();
					}
					else {
					System.out.println("In order to make a deposit");
					System.out.println("please establish a new account.");
					UserDao.openAccount();
					}
					return new AccountView();
					
				case 3: 
					if(hasAccount) {
					UserDao.makeWithdrawal();
					return new AccountView();
					}
					else {
					System.out.println("In order to make a withdrawal");
					System.out.println("you must have an active account.");
					UserDao.openAccount();
					}
					return new AccountView();
					
				case 4: 
					if(hasAccount) {	
					UserDao.makeTransfer();
					return new AccountView();
					}
					else {
					System.out.println("In order to transfer money");
					System.out.println("between accounts, please");
					System.out.println("establish a new account.");
					UserDao.openAccount();
					}
					return new AccountView();
					
			default: 
					return new MainMenu();
			}
			
							
			
		
		
		
//ViewOptionsMethod		
	}
//class	
}
