package com.bitbluesoftware.bpm.model;

import java.sql.Date;

public class Bill {
	int id;
	String name;
	double amount;
	Date date;
	User user;
	Transaction transaction;
	
	public Bill(int id, String name, double amount, Date date, User user, Transaction transaction) {
		this.id=id;
		this.name=name;
		this.amount=amount;
		this.date=date;
		this.user=user;
		this.transaction=transaction;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String toString(){
		return "Name: "+name+"  Amount: "+amount+" Date: "+date+"  User: "+user+"  Paid: "+(transaction!=null);
	}
}
