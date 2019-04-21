package com.revature.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.User;
import com.revature.services.UserService;
import com.revature.util.ConnectionUtil;

public class UserDao {

	List<String> usernameList = new ArrayList<>();
	
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
	
	public void usernameVerifier(String userName) {
		
		try(Connection connection = ConnectionUtil.getConnection()){
			String sql = "SELECT username FROM useraccounts";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();		
			while (rs.next()) {
//				System.out.println(rs.getString(1));
				usernameList.add(rs.getString(1));
				usernameList.add(rs.getString(1).toLowerCase());
//				System.out.println(usernameList);
				if(usernameList.contains(userName)) {
					System.out.println("That username is unavailable.  Please try again!");	
					
					UserService.createUser();
				}
			}
		}
		
		catch (SQLException e) {
			e.printStackTrace();
		}
				
	}
	
	

}
