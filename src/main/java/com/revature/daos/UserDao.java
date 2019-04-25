package com.revature.daos;

import java.math.BigDecimal;
import java.sql.Connection;
import com.revature.util.ScannerUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.revature.views.*;
import com.revature.beans.User;
import com.revature.services.UserServicesDao;
import com.revature.util.ConnectionUtil;

public class UserDao {

	List<String> usernameList = new ArrayList<>();
	static List<String> secondaryList = new ArrayList<>();
	
	public User saveUser(User user) {
		
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "INSERT INTO useraccounts (username, \"password\", \"name\") VALUES (?, ?, ?)";
			PreparedStatement ps = connection.prepareStatement(sql);

			ps.setString(1, user.getUserName());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getFullName());
			ps.executeUpdate();
			}
		 catch (SQLException e) {
			 e.printStackTrace();
		}
		
		return null;
	}
	
	public static void openAccount() {
		
		System.out.println("-----------------------------------------------|");
		System.out.println("Please choose the type of account");
		System.out.println("you would like to open:");
		System.out.println("-----------------------------------------------|");
		System.out.println("1. Individual Account");
		System.out.println("2. Joint Account");
		System.out.println("0. Return to your Accounts.");
		System.out.println("-----------------------------------------------|");
		
		int selection = ScannerUtil.getNumericChoice(2);
	
		switch(selection) {
		
			case 1: //individual account opening
			
				int accountNumber = 0;
				Double initialDeposit = 0.00;
				Boolean depositCorrect = false;
				
				System.out.println("-----------------------------------------------|");
				System.out.println("Here at X(factor)United, we're proud to"); 
				System.out.println("offer high interest rates on all of our accounts!");
				System.out.println("To immediately take advantage of these rates and"); 
				System.out.println("start earning, we recommend an initial deposit");
				System.out.println("of $100.");  
				System.out.println("-----------------------------------------------|");
				System.out.println("Please enter the amount of your inital");
				System.out.println("deposit to open your account:");
				System.out.println("-----------------------------------------------|");

				Double initialDepositEntry = 0.00;
				
				
				while(!depositCorrect) {
					try {
						initialDepositEntry =Double.parseDouble(ScannerUtil.getLine());
						
						if(initialDepositEntry<=0) {
							System.out.println("-----------------------------------------------|");
							System.out.println("Invalid deposit amount.");
							System.out.println("-----------------------------------------------|");
							System.out.println("Please enter a deposit amount greater than 0:");
							System.out.println("-----------------------------------------------|");
							initialDepositEntry = 0.00;

						}
						else {	
							initialDeposit += initialDepositEntry;
							depositCorrect = true;
						}
					}catch(NumberFormatException e) {
						System.out.println("-----------------------------------------------|");
						System.out.println("Invalid entry.");
						System.out.println("-----------------------------------------------|");
						System.out.println("Please enter the amount of your initial deposit:");
						System.out.println("-----------------------------------------------|");

						}
					}
				
				try(Connection connection = ConnectionUtil.getConnection()){
					String sql = "insert into bankaccounts (balance) values ("+initialDeposit+") returning accountnumber;";
					PreparedStatement ps = connection.prepareStatement(sql);
					ResultSet rs = ps.executeQuery();
					while(rs.next()) {
						accountNumber += rs.getInt(1);
					}
					String sql2 = "insert into usertobank (username, accounts) values ('" + UserServicesDao.usernameview + "', " + accountNumber + ");";
					PreparedStatement ps2 = connection.prepareStatement(sql2);
					ResultSet rs2 = ps2.executeQuery();
					
					
				}catch (SQLException e) {
					
				}
			
				System.out.println("Your account has been opened!");
				System.out.println("You will now be taken back to your account summary.");
				break;
				
				
				
			case 2: //joint account opening confimation
				
				System.out.println("-----------------------------------------------|");
				System.out.println("To open a joint account, both account owners"); 
				System.out.println("must have access to our online service.");
				System.out.println("If the Secondary Account Holder does");
				System.out.println("not have access to our Online service,");
				System.out.println("return to the Account Summary page and select");
				System.out.println("0 to Sign Out and create a new Online Account");
				System.out.println("for the Secondary Account Holder.  Otherwise,");
				System.out.println("enter 1 to begin account opening!");
				System.out.println("-----------------------------------------------|");
				
				System.out.println("-----------------------------------------|");
				System.out.println("1. Create Joint Account");
				System.out.println("0. Return to Account Summary Page.");
				System.out.println("-----------------------------------------|");
				
				int selection2 = ScannerUtil.getNumericChoice(1);
			
				switch(selection2) {
				
					case 1: //joint account creation
						
						System.out.println("-----------------------------------------------|");
						System.out.println("You are currently logged in as: "); 
						System.out.println(UserServicesDao.usernameview +".");
						System.out.println("This user will be designated as the"); 
						System.out.println("Primary Account Holder.");
						System.out.println("-----------------------------------------------|");

						
						int loopBreaker = 0;
						Boolean userExists = false;
						String secondaryOwner = "";
						
						while((!userExists)&&(loopBreaker==0)) {
							System.out.println("-----------------------------------------|");
							System.out.println("Please enter the username of the Secondary Account Holder:");
							System.out.println("-----------------------------------------|");
							String secondaryOwnerEntry = ScannerUtil.getLine();

							try(Connection connection = ConnectionUtil.getConnection()){
								String sql = "SELECT username FROM useraccounts";
								PreparedStatement ps = connection.prepareStatement(sql);
								ResultSet rs = ps.executeQuery();
								while (rs.next()) {
									secondaryList.add(rs.getString(1));
									secondaryList.add(rs.getString(1).toLowerCase());
									}
								}catch(SQLException e) {
									e.printStackTrace();
									}
							if(!secondaryOwnerEntry.equals(UserServicesDao.usernameview)) {
									
									if(!(secondaryList.contains(secondaryOwnerEntry))) {
										System.out.println("-----------------------------------------|");
										System.out.println("That username is not associated to a User Account.");
										System.out.println("If you need to return to the Main Menu and sign up");
										System.out.println("for a user account, please enter 0.");
										System.out.println("Otherwise, enter 1 to try again:");
										System.out.println("-----------------------------------------|");
									
										int selection3 = ScannerUtil.getNumericChoice(1);
									
										switch(selection3) {
											case 1:
												break;
											case 0: //return to main menu
												loopBreaker = 1;;
												break;
												}

									}
									else 
									{
										secondaryOwner += secondaryOwnerEntry;
										userExists = true;
									}
								}
								else 
								{
									System.out.println("The Primary Account Holder's username");
									System.out.println("is not a valid entry.");
									
								}
							
						}
						//out of while loop
						//This is where the method will take place to create the joint account.
						int accountNumberJ = 0;
						Double initialDepositJ = 0.00;
						Boolean depositCorrectJ = false;
						
						System.out.println("-----------------------------------------------|");
						System.out.println("Here at X(factor)United, we're proud to"); 
						System.out.println("offer high interest rates on all of our accounts!");
						System.out.println("To immediately take advantage of these rates and"); 
						System.out.println("start earning, we recommend an initial deposit");
						System.out.println("of $100.");  
						System.out.println("-----------------------------------------------|");
						System.out.println("Please enter the amount of your inital");
						System.out.println("deposit to open your account:");
						System.out.println("-----------------------------------------------|");

						Double initialDepositEntryJ = 0.00;
						
						
						while(!depositCorrectJ) {
							try {
								initialDepositEntryJ =Double.parseDouble(ScannerUtil.getLine());
								
								if(initialDepositEntryJ<=0) {
									System.out.println("-----------------------------------------------|");
									System.out.println("Invalid deposit amount.");
									System.out.println("-----------------------------------------------|");
									System.out.println("Please enter a deposit amount greater than 0:");
									System.out.println("-----------------------------------------------|");
									initialDepositEntryJ = 0.00;

								}
								else {	
									initialDepositJ += initialDepositEntryJ;
									depositCorrectJ = true;
								}
							}catch(NumberFormatException e) {
								System.out.println("-----------------------------------------------|");
								System.out.println("Invalid entry.");
								System.out.println("-----------------------------------------------|");
								System.out.println("Please enter the amount of your initial deposit:");
								System.out.println("-----------------------------------------------|");

								}
							}
						try(Connection connection = ConnectionUtil.getConnection()){
							String sql = "insert into bankaccounts (balance) values ("+initialDepositJ+") returning accountnumber;";
							PreparedStatement ps = connection.prepareStatement(sql);
							ResultSet rs = ps.executeQuery();
							while(rs.next()) {
								accountNumberJ += rs.getInt(1);
								}
							String sql2 = "insert into usertobank (username, accounts) values ('" + UserServicesDao.usernameview + "', " + accountNumberJ + "), ('" + secondaryOwner + "', " + accountNumberJ + ");";
							PreparedStatement ps2 = connection.prepareStatement(sql2);
							ps2.executeQuery();
							

							
						}catch (SQLException e) {
							
						}
//						
						System.out.println("Your account has been opened!");
						System.out.println("You will now be taken back to your account summary.");
						
						break;
						
						
						
						
					case 0: //return to main menu from joint account opening confirmation
						return;
				}
				
				
			case 0: //return to account view from the account type selection screen
				return; 
				
		}
		
		
			
		
	}
	
	public static void makeDeposit() {
		
		int accountNumberSel = 0;
		Set<Integer> availAccounts = new HashSet<>();
		Boolean accountExists = false;
		Boolean depositEntered = false;
		Double depositAmount = 0.00;
		
		System.out.println("-----------------------------------------------|");
		System.out.println("Here is a list of your active accounts:");
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
				availAccounts.add(rs.getInt(1));
				int accountNum = rs.getInt(1);
				BigDecimal balance = rs.getBigDecimal(2);
				System.out.println("-----------------------------------------------|");
				System.out.println("Account# " + accountNum + "    |" + "Balance: " + balance);
				System.out.println("-----------------------------------------------|");
			}
		}catch (SQLException e) {
			e.printStackTrace();
			}
		
		while(!accountExists) {
			System.out.println("-----------------------------------------------|");
			System.out.println("Enter the account number in which");
			System.out.println("you would like to make a deposit:");
			System.out.println("-----------------------------------------------|");

		
			int accountNumberSelEntry = 0;
		
			try {
				accountNumberSelEntry =Integer.parseInt(ScannerUtil.getLine());
				if(!availAccounts.contains(accountNumberSelEntry)) {
					System.out.println("Invalid Account Number");
				}
				else {
					accountNumberSel += accountNumberSelEntry;
					accountExists = true;
				}
			}catch(NumberFormatException e) {
				System.out.println("-----------------------------------------------|");
				System.out.println("Invalid entry.");
				System.out.println("-----------------------------------------------|");

				}
		}

		while(!depositEntered) {
			System.out.println("-----------------------------------------------|");
			System.out.println("Enter deposit amount: ");
			System.out.println("-----------------------------------------------|");

		
			try {
				
				depositAmount += Double.parseDouble(ScannerUtil.getLine());
				
				if(depositAmount<=0) {
					System.out.println("-----------------------------------------------|");
					System.out.println("Enter a deposit amount greater than 0.");
					System.out.println("-----------------------------------------------|");

					depositAmount = 0.00;
				}
				
				else{depositEntered = true;}
				
			}catch(NumberFormatException e) {
				System.out.println("Invalid entry.");
			}
	
		}
		
		try(Connection connection = ConnectionUtil.getConnection()){
			String sql = "update bankaccounts set balance = balance + ? where accountnumber = ?;";
			PreparedStatement ps = connection.prepareStatement(sql);
		
			ps.setDouble(1, depositAmount);
			ps.setInt(2, accountNumberSel);
			ps.executeUpdate();
			System.out.println("-----------------------------------------------|");
			System.out.println("Deposit successful!");
			System.out.println("-----------------------------------------------|");

			
		}catch (SQLException e) {
			e.printStackTrace();
			}
		
		
		
		
	}
	
	public static void makeWithdrawal() {
		int accountNumberSel = 0;
		Set<Integer> availAccounts = new HashSet<>();
		Boolean accountExists = false;
		Boolean withdrawalEntered = false;
		Double withdrawalAmount = 0.00;
		Map<Integer, Double> accBal = new HashMap<>();
		
		System.out.println("-----------------------------------------------|");
		System.out.println("Here is a list of your active accounts:");
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
				accBal.put(rs.getInt(1), rs.getDouble(2));
				availAccounts.add(rs.getInt(1));
				int accountNum = rs.getInt(1);
				BigDecimal balance = rs.getBigDecimal(2);
				System.out.println("-----------------------------------------------|");
				System.out.println("Account# " + accountNum + "    |" + "Balance: " + balance);
				System.out.println("-----------------------------------------------|");
			}
		}catch (SQLException e) {
			e.printStackTrace();
			}
		
		while(!accountExists) {
			System.out.println("-----------------------------------------------|");
			System.out.println("Enter the account number from which");
			System.out.println("you would like to make a withdrawal:");
			System.out.println("-----------------------------------------------|");

		
			int accountNumberSelEntry = 0;
		
			try {
				accountNumberSelEntry =Integer.parseInt(ScannerUtil.getLine());
				if(!availAccounts.contains(accountNumberSelEntry)) {
					System.out.println("-----------------------------------------------|");
					System.out.println("Invalid Account Number");
					System.out.println("-----------------------------------------------|");

				}
				else {
					accountNumberSel += accountNumberSelEntry;
					accountExists = true;
				}
			}catch(NumberFormatException e) {
				System.out.println("-----------------------------------------------|");
				System.out.println("Invalid entry.");
				System.out.println("-----------------------------------------------|");

				}
		}

		while(!withdrawalEntered) {
			System.out.println("-----------------------------------------------|");
			System.out.println("Enter withdrawal amount: ");
			System.out.println("-----------------------------------------------|");

		
			try {
				
				withdrawalAmount += Double.parseDouble(ScannerUtil.getLine());
				
				if(withdrawalAmount<=0) {
					System.out.println("-----------------------------------------------|");
					System.out.println("Enter a withdrawal amount greater than 0.");
					System.out.println("-----------------------------------------------|");

					withdrawalAmount = 0.00;
				}
				
				else{
					if(accBal.get(accountNumberSel)<withdrawalAmount) {
						System.out.println("-----------------------------------------------|");
						System.out.println("Cannot withdraw an amount greater than your");
						System.out.println("account balance.");
						System.out.println("-----------------------------------------------|");
						
						withdrawalAmount = 0.00;
						
					}else {
					
						withdrawalEntered = true;
					}
				}
			}catch(NumberFormatException e) {
				System.out.println("-----------------------------------------------|");
				System.out.println("Invalid entry.");
				System.out.println("-----------------------------------------------|");

			}
	
		}
		
		try(Connection connection = ConnectionUtil.getConnection()){
			String sql = "update bankaccounts set balance = balance - ? where accountnumber = ?;";
			PreparedStatement ps = connection.prepareStatement(sql);
		
			ps.setDouble(1, withdrawalAmount);
			ps.setInt(2, accountNumberSel);
			ps.executeUpdate();
			System.out.println("-----------------------------------------------|");
			System.out.println("Withdrawal successful!");
			System.out.println("-----------------------------------------------|");

			
		}catch (SQLException e) {
			e.printStackTrace();
			}

	}
	
	public static void makeTransfer() {
		System.out.println("Make a transfer method.");
	}
	

}
