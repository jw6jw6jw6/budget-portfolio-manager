package com.bitbluesoftware.bpm.model;

public class Account {
	String name;
	String bank;
	double balance;
	User user;
	
	public Account(String name, String bank, double balance, User user) {
		this.name=name;
		this.bank=bank;
		this.balance=balance;
		this.user=user;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getBank() {
		return bank;
	}
	
	public void setBank(String bank) {
		this.bank = bank;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
}
