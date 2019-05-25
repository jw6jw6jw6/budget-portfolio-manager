package com.bitbluesoftware.bpm.model;

import java.util.Date;

public class Bill {
	String name;
	double amount;
	Date date;
	User user;
	Transaction transaction;
	
	public Bill(String name, double amount, Date date, User user) {
		this.name=name;
		this.amount=amount;
		this.date=date;
		this.user=user;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Transaction getTransaction() {
		return transaction;
	}
	
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
}
