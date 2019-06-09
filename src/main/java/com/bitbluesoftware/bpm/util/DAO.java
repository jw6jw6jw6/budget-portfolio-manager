package com.bitbluesoftware.bpm.util;

import com.bitbluesoftware.bpm.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DAO {
	Map<Integer, Transaction> transactions;
	Map<Integer, Category> categories;
	Map<Integer, User> users;
	Map<Integer, Account> accounts;
	Map<Integer, Bill> bills;
	Map<Integer, Role> roles;
	Map<Integer, BalanceHistory> balanceHistory;
	Map<String, User> tokens = new HashMap<>();
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

	public Map<Integer, User> getUsers(){
		return users;
	}
	
	public Map<Integer, Category> getCategories(){
		return categories;
	}
	
	public Map<Integer, Transaction> getTransactions(){
		return transactions;
	}

	public Map<Integer,Account> getAccounts(){
		return accounts;
	}

	public Map<Integer,Bill> getBills(){
		return bills;
	}

	public Map<String, User> getTokens(){
		return tokens;
	}

	public User getToken(String token) {
		return tokens.get(token);
	}

	public void addToken(String token, User user){
		tokens.put(token,user);
	}

	public void removeToken(String token){
		tokens.remove(token);
	}

	public void insertHistoryRecord(Transaction transaction) {
		if(transaction!=null) {
			Account account = transaction.getAccount();
			if(account!=null) {
				try {

					PreparedStatement statement = conn.prepareStatement("INSERT INTO balance_history (original_balance,new_balance,account,transaction) VALUES (?,?,?,?)");
					statement.setDouble(3, account.getBalance());
					statement.setDouble(3, transaction.getAmount()+account.getBalance());
					statement.setInt(5, account.getId());
					statement.setInt(6, transaction.getId());
					statement.executeUpdate();
				} catch (SQLException ex) {
					// handle any errors
					log.error("Insert BalanceHistory (New Transaction) Error");
					log.error("SQLException: " + ex.getMessage());
					log.error("SQLState: " + ex.getSQLState());
					log.error("VendorError: " + ex.getErrorCode());
				}
			}
		}
	}

	public void insertHistoryRecord(Transaction newTransaction, Transaction oldTransaction) {
		if(newTransaction!=null && oldTransaction!=null) {
			BalanceHistory balanceHistoryItem;
			for(BalanceHistory bh : balanceHistory.values())
				if(bh.getTransaction().getId()==newTransaction.getId())
					balanceHistoryItem=bh;

			if(balanceHistory!=null) {
				try {
					PreparedStatement statement = conn.prepareStatement("UPDATE  SET name = ? WHERE id = ?" );
					statement.setDouble(3, newTransaction.getAccount().getBalance());
					statement.setDouble(3, oldTransaction.getAmount()+newTransaction.getAccount().getBalance());
					statement.setInt(5, newTransaction.getAccount().getId());
					statement.setInt(6, newTransaction.getId());
					statement.executeUpdate();
				} catch (SQLException ex) {
					// handle any errors
					log.error("Insert BalanceHistory (New Transaction) Error");
					log.error("SQLException: " + ex.getMessage());
					log.error("SQLState: " + ex.getSQLState());
					log.error("VendorError: " + ex.getErrorCode());
				}
			}
		}
	}
	
	private Map<Integer, Transaction> processTransactions(){
		transactions = new HashMap<>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from transactions;");
			while(rs.next()) {
				transactions.put(rs.getInt("id"), new Transaction(rs.getInt("id"),rs.getString("name"),rs.getDouble("amount"),categories.get(rs.getInt("category")),users.get(rs.getInt("user")),rs.getDate("date"),accounts.get(rs.getInt("account"))));
			}
			rs.close();
			stmt.close();
		} catch (SQLException ex) {
			// handle any errors
			log.error("Transactions Error");
			log.error("SQLException: " + ex.getMessage());
			log.error("SQLState: " + ex.getSQLState());
			log.error("VendorError: " + ex.getErrorCode());
		}
		return transactions;
	}

	public void deleteTransactionsFromAccount(Account account) {
		try {
			PreparedStatement statement = conn.prepareStatement("DELETE from transactions WHERE account = ?" );
			statement.setInt(1, account.getId());
			statement.executeUpdate();
		} catch (SQLException ex) {
			// handle any errors
			log.error("Delete Associated Transactions Error");
			log.error("SQLException: " + ex.getMessage());
			log.error("SQLState: " + ex.getSQLState());
			log.error("VendorError: " + ex.getErrorCode());
		}
	}

	public void insertCategory(Category category) {
		try {
			PreparedStatement statement = conn.prepareStatement("INSERT INTO categories (name) VALUES (?)" );
			statement.setString(1, category.getName());
			statement.executeUpdate();
		} catch (SQLException ex) {
			// handle any errors
			log.error("Insert Category Error");
			log.error("SQLException: " + ex.getMessage());
			log.error("SQLState: " + ex.getSQLState());
			log.error("VendorError: " + ex.getErrorCode());
		}
	}

	public void updateCategory(Category category) {
		try {
			PreparedStatement statement = conn.prepareStatement("UPDATE categories SET name = ? WHERE id = ?" );
			statement.setString(1, category.getName());
			statement.setInt(2, category.getId());
			statement.executeUpdate();
		} catch (SQLException ex) {
			// handle any errors
			log.error("Category Update Error");
			log.error("SQLException: " + ex.getMessage());
			log.error("SQLState: " + ex.getSQLState());
			log.error("VendorError: " + ex.getErrorCode());
		}
	}

	public void deleteCategory(Category category) {
		try {
			PreparedStatement statement = conn.prepareStatement("DELETE from categories WHERE id = ?" );
			statement.setInt(1, category.getId());
			statement.executeUpdate();
		} catch (SQLException ex) {
			// handle any errors
			log.error("Category Delete Error");
			log.error("SQLException: " + ex.getMessage());
			log.error("SQLState: " + ex.getSQLState());
			log.error("VendorError: " + ex.getErrorCode());
		}
	}

	public void insertAccount(Account account) {
		try {
			PreparedStatement statement = conn.prepareStatement("INSERT INTO accounts (name,balance,bank,type,user) VALUES (?,?,?,?,?)" );
			statement.setString(1, account.getName());
			statement.setDouble(2, account.getBalance());
			statement.setString(3, account.getBank());
			statement.setString(4, account.getType());
			statement.setInt(5, account.getUser().getId());
			statement.executeUpdate();
		} catch (SQLException ex) {
			// handle any errors
			log.error("Insert Account Error");
			log.error("SQLException: " + ex.getMessage());
			log.error("SQLState: " + ex.getSQLState());
			log.error("VendorError: " + ex.getErrorCode());
		}
	}

	public void insertTransaction(Transaction transaction) {
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO transactions (name,category,amount,date,user,account) VALUES (?,?,?,?,?,?)");
            statement.setString(1, transaction.getName());
            statement.setInt(2, transaction.getCategory().getId());
            statement.setDouble(3, transaction.getAmount());
            statement.setDate(4, convertToSqlDate(transaction.getDate()));
            statement.setInt(5, transaction.getUser().getId());
            statement.setInt(6,transaction.getAccount().getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            // handle any errors
            log.error("Insert Transactions Error");
            log.error("SQLException: " + ex.getMessage());
            log.error("SQLState: " + ex.getSQLState());
            log.error("VendorError: " + ex.getErrorCode());
        }
    }

	public void insertBill(Bill bill) {
		try {
			PreparedStatement statement = conn.prepareStatement("INSERT INTO bills (name,amount,date,transaction,user) VALUES (?,?,?,?,?)" );
			statement.setString(1, bill.getName());
			statement.setDouble(2, bill.getAmount());
			statement.setDate(3, convertToSqlDate(bill.getDate()));
			if(bill.getTransaction()!=null)
				statement.setInt(4, bill.getTransaction().getId());
			else
				statement.setInt(4,0);
			statement.setInt(5, bill.getUser().getId());
			statement.executeUpdate();
		} catch (SQLException ex) {
			// handle any errors
			log.error("Insert Bill Error");
			log.error("SQLException: " + ex.getMessage());
			log.error("SQLState: " + ex.getSQLState());
			log.error("VendorError: " + ex.getErrorCode());
		}
	}
	
	public void insertUser(User user) {
		try {
			PreparedStatement statement = conn.prepareStatement("INSERT INTO users (username,firstname,lastname,password,lastLogin,logincount,email,accountcreated) VALUES (?,?,?,?,?,?,?,?)" );
			statement.setString(1, user.getUsername());
			statement.setString(2, user.getFirstName());
			statement.setString(3, user.getLastName());
			statement.setString(4, user.getPassword());
			statement.setDate(5, convertToSqlDate(user.getLastLogin()));
			statement.setInt(6, user.getLoginCount());
			statement.setString(7, user.getEmail());
			statement.setDate(8, convertToSqlDate(user.getAccountCreated()));
			statement.executeUpdate();
		} catch (SQLException ex) {
			// handle any errors
			log.error("Insert User Error");
			log.error("SQLException: " + ex.getMessage());
			log.error("SQLState: " + ex.getSQLState());
			log.error("VendorError: " + ex.getErrorCode());
		}
	}

	public void updateBill(Bill bill) {
		try {
			PreparedStatement statement = conn.prepareStatement("UPDATE bills SET name = ?, amount = ?, date = ?, transaction = ?, user = ? WHERE id = ?" );
			statement.setString(1, bill.getName());
			statement.setDouble(2, bill.getAmount());
			statement.setDate(3, convertToSqlDate(bill.getDate()));
			if(bill.getTransaction()!=null)
				statement.setInt(4, bill.getTransaction().getId());
			else
				statement.setInt(4,0);
			statement.setInt(5, bill.getUser().getId());
			statement.setInt(6, bill.getId());
			statement.executeUpdate();
		} catch (SQLException ex) {
			// handle any errors
			log.error("Bill Update Error");
			log.error("SQLException: " + ex.getMessage());
			log.error("SQLState: " + ex.getSQLState());
			log.error("VendorError: " + ex.getErrorCode());
		}
	}

	public void deleteBill(Bill bill) {
		try {
			PreparedStatement statement = conn.prepareStatement("DELETE from bills WHERE id = ?" );
			statement.setInt(1, bill.getId());
			statement.executeUpdate();
		} catch (SQLException ex) {
			// handle any errors
			log.error("Bill Delete Error");
			log.error("SQLException: " + ex.getMessage());
			log.error("SQLState: " + ex.getSQLState());
			log.error("VendorError: " + ex.getErrorCode());
		}
	}

	public void updateUser(User user) {
		try {
			PreparedStatement statement = conn.prepareStatement("UPDATE users SET firstname = ?, lastname = ?, email = ?, password = ?, accountcreated = ?, lastlogin = ?, logincount = ? WHERE id = ?" );
			statement.setString(1, user.getFirstName());
			statement.setString(2, user.getLastName());
			statement.setString(3, user.getEmail());
			statement.setString(4, user.getPassword());
			java.sql.Date accountCreated = new java.sql.Date(user.getAccountCreated().getTime());
			statement.setDate(5, accountCreated);
			java.sql.Date lastLogin = new java.sql.Date(user.getLastLogin().getTime());
			statement.setDate(6, lastLogin);
			statement.setInt(7,user.getLoginCount());
			statement.setInt(8, user.getId());
			statement.executeUpdate();
		} catch (SQLException ex) {
			// handle any errors
			log.error("Update User Error");
			log.error("SQLException: " + ex.getMessage());
			log.error("SQLState: " + ex.getSQLState());
			log.error("VendorError: " + ex.getErrorCode());
		}
	}

	public void deleteTransaction(Transaction transaction) {
		try {
			PreparedStatement statement = conn.prepareStatement("DELETE from transactions WHERE id = ?" );
			statement.setInt(1, transaction.getId());
			statement.executeUpdate();
		} catch (SQLException ex) {
			// handle any errors
			log.error("Transaction Delete Error");
			log.error("SQLException: " + ex.getMessage());
			log.error("SQLState: " + ex.getSQLState());
			log.error("VendorError: " + ex.getErrorCode());
		}
	}

	public void deleteAccount(Account account) {
		try {
			PreparedStatement statement = conn.prepareStatement("DELETE from accounts WHERE id = ?" );
			statement.setInt(1, account.getId());
			statement.executeUpdate();
		} catch (SQLException ex) {
			// handle any errors
			log.error("Account Delete Error");
			log.error("SQLException: " + ex.getMessage());
			log.error("SQLState: " + ex.getSQLState());
			log.error("VendorError: " + ex.getErrorCode());
		}
	}

	public void updateAccount(Account account) {
		try {
			PreparedStatement statement = conn.prepareStatement("UPDATE accounts SET name = ?, bank = ?, balance = ?, type = ? WHERE id = ?" );
			statement.setString(1, account.getName());
			statement.setString(2, account.getBank());
			statement.setDouble(3, account.getBalance());
			statement.setString(4, account.getType());
			statement.setInt(5, account.getId());
			statement.executeUpdate();
		} catch (SQLException ex) {
			// handle any errors
			log.error("Account Update Error");
			log.error("SQLException: " + ex.getMessage());
			log.error("SQLState: " + ex.getSQLState());
			log.error("VendorError: " + ex.getErrorCode());
		}
	}

	public void updateTransaction(Transaction transaction) {
		try {
			PreparedStatement statement = conn.prepareStatement("UPDATE transactions SET name = ?, amount = ?, date = ?, category = ?, account = ? WHERE id = ?" );
			statement.setString(1, transaction.getName());
			statement.setDouble(2, transaction.getAmount());
			statement.setDate(3, convertToSqlDate(transaction.getDate()));
			statement.setInt(4, transaction.getCategory().getId());
			statement.setInt(5, transaction.getAccount().getId());
			statement.setInt(6, transaction.getId());
			statement.executeUpdate();
		} catch (SQLException ex) {
			// handle any errors
			log.error("Transaction Update Error");
			log.error("SQLException: " + ex.getMessage());
			log.error("SQLState: " + ex.getSQLState());
			log.error("VendorError: " + ex.getErrorCode());
		}
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
			log.error("Categories Error");
			log.error("SQLException: " + ex.getMessage());
			log.error("SQLState: " + ex.getSQLState());
			log.error("VendorError: " + ex.getErrorCode());
		}
		return categories;
	}

	private Map<Integer,Category> processUsers() {
		users = new HashMap<>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from users;");
			while(rs.next()) {
				users.put(rs.getInt("id"),new User(rs.getInt("id"),rs.getString("username"),rs.getString("firstname"),rs.getString("lastname"),rs.getString("email"),rs.getString("password"),rs.getDate("accountcreated"),rs.getDate("lastlogin"),rs.getInt("logincount")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException ex) {
			// handle any errors
			log.error("Users Error");
			log.error("SQLException: " + ex.getMessage());
			log.error("SQLState: " + ex.getSQLState());
			log.error("VendorError: " + ex.getErrorCode());
		}
		return categories;
	}

	private Map<Integer,Account> processAccounts() {
		accounts = new HashMap<>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from accounts;");
			while(rs.next()) {
				accounts.put(rs.getInt("id"),new Account(rs.getInt("id"),rs.getString("name"),rs.getString("bank"),rs.getDouble("balance"),users.get(rs.getInt("user")), rs.getString("type")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException ex) {
			// handle any errors
			log.error("Accounts Error");
			log.error("SQLException: " + ex.getMessage());
			log.error("SQLState: " + ex.getSQLState());
			log.error("VendorError: " + ex.getErrorCode());
		}
		return accounts;
	}

	private Map<Integer,Bill> processBills() {
		bills = new HashMap<>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from bills;");
			while(rs.next()) {
					bills.put(rs.getInt("id"),new Bill(rs.getInt("id"),rs.getString("name"),rs.getDouble("amount"),rs.getDate("date"),users.get(rs.getInt("user")),transactions.get(rs.getInt("transaction"))));
			}
			rs.close();
			stmt.close();
		} catch (SQLException ex ) {
			// handle any errors
			log.error("Bills Error");
			log.error("SQLException: " + ex.getMessage());
			log.error("SQLState: " + ex.getSQLState());
			log.error("VendorError: " + ex.getErrorCode());
		} catch (NullPointerException e) {
			log.error("Bills error: Transactions: "+transactions);
		}
		return bills;
	}

	private Map<Integer,Bill> processRoles() {
		roles = new HashMap<>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from roles;");
			while(rs.next()) {
				roles.put(rs.getInt("id"),new Role(rs.getInt("id"),rs.getString("name")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException ex) {
			// handle any errors
			log.error("Roles Error");
			log.error("SQLException: " + ex.getMessage());
			log.error("SQLState: " + ex.getSQLState());
			log.error("VendorError: " + ex.getErrorCode());
		}
		return bills;
	}

	private void processFinancialData() {
		FinancialInterface fi = new FinancialInterface();
		fi.init();
	}
	
	private java.util.Date convertToUtilDate(Date date){
		return new java.util.Date(date.getTime());
	}
	
	private java.sql.Date convertToSqlDate(java.util.Date date){
		return new java.sql.Date(date.getTime());
	}

	private void init() {
		log.info("Init DAO");
		ConfigLoader configLoader = new ConfigLoader();
		Map<String,String> configs = configLoader.readConfig();
		try {
			if(conn!=null)
				conn.close();
			conn = DriverManager.getConnection("jdbc:mysql://"+configs.get("databaseUrl")+":"+configs.get("databasePort")+"/"+configs.get("databaseName")+"?" + "serverTimezone=CST&user="+configs.get("databaseUsername")+"&password="+configs.get("databasePassword"));
		} catch (SQLException ex) {
			// handle any errors
			log.error("SQLException: " + ex.getMessage());
			log.error("SQLState: " + ex.getSQLState());
			log.error("VendorError: " + ex.getErrorCode());
		}
		processRoles();
		processUsers();
		processAccounts();
		processCategories();
		processTransactions();
		processBills();

	}

	public void refreshData(){
		init();
	}
}
