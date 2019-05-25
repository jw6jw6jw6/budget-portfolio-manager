package com.bitbluesoftware.bpm.util;

import com.bitbluesoftware.bpm.model.Category;
import com.bitbluesoftware.bpm.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DAO {
	ArrayList<Transaction> transactions;
	Map<Integer, Category> categories;
	Connection conn;
	Logger log = LoggerFactory.getLogger(DAO.class);
	
	public DAO(){
		init();
	}
	
	@Bean
	@Primary
	public DAO getDAO(){
		return this;
	}
	
	public Map<Integer, Category> getCategories(){
		return categories;
	}
	
	public ArrayList<Transaction> getTransactions(){
		return transactions;
	}
	
	private ArrayList<Transaction> processTransactions(){
		transactions = new ArrayList<>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from transactions;");
			while(rs.next()) {
				transactions.add(new Transaction(rs.getInt("id"),rs.getString("name"),rs.getDouble("amount"),categories.get(rs.getInt("category"))));
			}
			rs.close();
			stmt.close();
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return transactions;
	}
	
	private Map<Integer,Category> processCategories() {
		categories = new HashMap<>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from categories;");
			while(rs.next()) {
				categories.put(rs.getInt("id"),new Category(rs.getInt("id"),rs.getString("name")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException ex) {
			// handle any errors
			log.error("SQLException: " + ex.getMessage());
			log.error("SQLState: " + ex.getSQLState());
			log.error("VendorError: " + ex.getErrorCode());
		}
		return categories;
	}
	
	public void init() {
		log.info("Init DAO");
		try {
			conn = DriverManager.getConnection("jdbc:mysql://192.168.10.244/budget?" + "serverTimezone=UTC&user=budget&password=password");
		} catch (SQLException ex) {
			// handle any errors
			log.error("SQLException: " + ex.getMessage());
			log.error("SQLState: " + ex.getSQLState());
			log.error("VendorError: " + ex.getErrorCode());
		}
		
		processCategories();
		processTransactions();
	}
}
