package com.revature.views;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import com.revature.services.UserService;
import com.revature.util.ConnectionUtil;
import com.revature.util.ScannerUtil;

public class AccountView implements View {

	HashMap<Integer, Integer> accounts = new HashMap<>();
	
	public View printOptions() {
		System.out.println("\n");
		System.out.println("-----------------------------------------|");
		System.out.println("Welcome " + UserService.userview +"!");
		System.out.println("Here is a summary of your accounts.");
		
		try(Connection connection = ConnectionUtil.getConnection()){
			String sql = "select accountnumber, balance\r\n" + 
					"from usertobank join bankaccounts\r\n" + 
					"on usertobank.accounts = accountnumber\r\n" + 
					"where username = ?;";
			PreparedStatement ps = connection.prepareStatement(sql);
			
			ps.setString(1, UserService.usernameview);
			
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int accountNum = rs.getInt(1);
				BigDecimal balance = rs.getBigDecimal(2);
				System.out.println("-----------------------------------------|");
				System.out.println("Account# " + accountNum + "|" + "Balance: " + balance);
				System.out.println("-----------------------------------------|");
			}

			System.out.println("What would you like to do?");
			System.out.println("-----------------------------------------|");
			System.out.println("1. Open a new account.");
			System.out.println("2. Make a deposit.");
			System.out.println("3. Make a withdrawl.");
			System.out.println("4. Transfer funds to another account.");
			System.out.println("5. Return to Main Menu.");
			System.out.println("-----------------------------------------|");

			
			int selection = ScannerUtil.getNumericChoice(5);
			
			switch(selection) {
			case 1: UserService.openAccount();
				return new AccountView();
			case 2: UserService.makeDeposit();
				return new AccountView();
			case 3: UserService.makeWithdrawal();
				return new AccountView();
			case 4: UserService.makeTransfer();
			
			default: return new MainMenu();
			
			}
		}catch (SQLException e) {
			e.printStackTrace();

		}
		
		return null;
	}

	
}
