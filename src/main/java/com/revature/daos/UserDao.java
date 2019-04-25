package com.revature.daos;

import java.sql.Connection;
import com.revature.util.ScannerUtil;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
		
		System.out.println("-----------------------------------------|");
		System.out.println("Please choose the type of account");
		System.out.println("you would like to open:");
		System.out.println("-----------------------------------------|");
		System.out.println("1. Individual Account");
		System.out.println("2. Joint Account");
		System.out.println("0. Return to your Accounts.");
		System.out.println("-----------------------------------------|");
		
		int selection = ScannerUtil.getNumericChoice(2);
	
		switch(selection) {
		
			case 1: //individual account opening
			
				int accountNumber = 0;
				double initialDeposit = 0;
				
				System.out.println("Here at X(factor)United, we're proud to"); 
				System.out.println("offer high interest rates on all of our accounts!");
				System.out.println("To immediately take advantage of these rates and"); 
				System.out.println("start earning, we recommend an initial deposit of $100.");
				System.out.println("Please enter the amount of your inital deposit to open your account:");
				
				initialDeposit += Double.parseDouble(ScannerUtil.getLine());
				
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
				
				System.out.println("To open a joint account, both account owners"); 
				System.out.println("must have access to our online service.");
				System.out.println("If the Secondary Account Holder does not");
				System.out.println("does not have access to our Online service,");
				System.out.println("return to the Account Summary page and select");
				System.out.println("0 to Sign Out and create a new Online Account");
				System.out.println("for the Secondary Account Holder.  Otherwise, enter 1 to begin");
				System.out.println("account opening for a joint account!");
				
				System.out.println("-----------------------------------------|");
				System.out.println("1. Create Joint Account");
				System.out.println("0. Return to Account Summary Page.");
				System.out.println("-----------------------------------------|");
				
				int selection2 = ScannerUtil.getNumericChoice(1);
			
				switch(selection2) {
				
					case 1: //joint account creation
						
						System.out.println("You are currently logged in as " + UserServicesDao.usernameview +".");
						System.out.println("This user will be designated as the Primary Account Holder.");
						
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
						double initialDepositJ = 0;
						
						System.out.println("Here at X(factor)United, we're proud to"); 
						System.out.println("offer high interest rates on all of our accounts!");
						System.out.println("To immediately take advantage of these rates and"); 
						System.out.println("start earning, we recommend an initial deposit of $100.");
						System.out.println("Please enter the amount of your inital deposit to open your account:");
						
						initialDepositJ += Double.parseDouble(ScannerUtil.getLine());
						
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
		System.out.println("Make deposit methods.");
	}
	
	public static void makeWithdrawal() {
		System.out.println("Make a withdrawal method.");
	}
	
	public static void makeTransfer() {
		System.out.println("Make a transfer method.");
	}
	

}
