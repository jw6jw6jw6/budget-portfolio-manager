package com.bitbluesoftware.bpm.model;

import java.util.Date;

public class Transaction {
	String name;
	double amount;
	Category category;
	int id;
	User user;
	Date date;
	Account account;
	
	public Transaction(int id, String name, double amount, Category category, User user, Date date, Account account){
		this.id = id;
		this.name = name;
		this.amount = amount;
		this.category = category;
		this.user=user;
		this.date = date;
		this.account = account;
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id=id;
	}

	public Category getCategory(){
		return category;
	}

	public void setDate(Date date){
		this.date=date;
	}
	
	public Date getDate(){
		return date;
	}
	
	public void setCategory(Category category){
		this.category=category;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public double getAmount(){
		return amount;
	}
	
	public void setAmount(double amount){
		this.amount = amount;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Account getAccount() {
		return account;
	}
	
	public String toString(){
		return "ID: "+id+"  Name: "+name+"  Amount: "+amount+"  Category: "+category+"  User: "+user;
	}
}
