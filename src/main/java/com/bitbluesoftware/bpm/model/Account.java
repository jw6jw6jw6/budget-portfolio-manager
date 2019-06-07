package com.bitbluesoftware.bpm.model;

public class Account {
	int id;
	String name;
	String bank;
	double balance;
	User user;
	String type;
	
	public Account(int id, String name, String bank, double balance, User user, String type) {
		this.id=id;
		this.name=name;
		this.bank=bank;
		this.balance=balance;
		this.user=user;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String toString(){
		return "ID: "+id+"  Name: "+name+"  Bank: "+bank+"  Balance: "+balance+"  User: "+user+"  Type: "+type;
	}
}