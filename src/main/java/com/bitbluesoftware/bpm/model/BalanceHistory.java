package com.bitbluesoftware.bpm.model;

import java.util.concurrent.BlockingDeque;

public class BalanceHistory {
    double original_balance;
    double new_balance;
    Account account;
    Transaction transaction;

    public BalanceHistory(double original_balance, double new_balance, Account account, Transaction transaction) {
        this.original_balance = original_balance;
        this.new_balance = new_balance;
        this.account=account;
        this.transaction=transaction;
    }

    public double getOriginal_balance() {
        return original_balance;
    }

    public void setOriginal_balance(double original_balance) {
        this.original_balance = original_balance;
    }

    public double getNew_balance() {
        return new_balance;
    }

    public void setNew_balance(double new_balance) {
        this.new_balance = new_balance;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
