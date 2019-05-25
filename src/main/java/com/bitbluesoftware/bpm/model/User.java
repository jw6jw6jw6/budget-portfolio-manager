package com.bitbluesoftware.bpm.model;

import java.util.Date;

public class User {
	
	String username;
	String firstName;
	String lastName;
	String email;
	String password;
	Date accountCreated;
	Date lastLogin;
	int loginCount;
	
	public User(String username, String firstName, String lastName, String email, String password, Date accountCreated, Date lastLogin, int loginCount) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.accountCreated = accountCreated;
		this.lastLogin = lastLogin;
		this.loginCount = loginCount;
	}
	
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Date getAccountCreated() {
		return accountCreated;
	}
	
	public void setAccountCreated(Date accountCreated) {
		this.accountCreated = accountCreated;
	}
	
	public Date getLastLogin() {
		return lastLogin;
	}
	
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	
	public int getLoginCount() {
		return loginCount;
	}
	
	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}
}
