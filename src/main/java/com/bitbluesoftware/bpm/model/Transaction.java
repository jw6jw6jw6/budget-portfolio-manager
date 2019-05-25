package com.bitbluesoftware.bpm.model;

public class Transaction {
	String name;
	double amount;
	Category category;
	int id;
	User user;
	
	public Transaction(int id, String name, double amount, Category category){
		this.id = id;
		this.name = name;
		this.amount = amount;
		this.category = category;
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
	
	public String toString(){
		return "ID: "+id+"  Name: "+name+"  Amount: "+amount+"  Category: "+category;
	}
}
