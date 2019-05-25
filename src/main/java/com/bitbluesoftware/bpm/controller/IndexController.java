package com.bitbluesoftware.bpm.controller;

import com.bitbluesoftware.bpm.model.Category;
import com.bitbluesoftware.bpm.model.Transaction;
import com.bitbluesoftware.bpm.util.DAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;
import java.util.*;
import java.util.Date;

@Controller
public class IndexController {
	Logger log = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	DAO dao;
	
	@GetMapping("/")
	public String index(Model model) {
		log.info("Index");
		model.addAttribute("message", new Date().toString());
		return "index"; //view
	}
	
	@RequestMapping("/transactions")
	public String testing(@RequestParam(name = "id", required = false, defaultValue = "-1") String idString, Model model) {
		int id = Integer.parseInt(idString);
		log.info("Transactions");
		ArrayList<Transaction> transactions = dao.getTransactions();
		Transaction transaction = null;
		if(id!=-1)
		for(Transaction trans : transactions){
			if(trans.getId()==id) {
				transaction=trans;
			}
		}
		log.info(transactions.size()+"");
		model.addAttribute("message", new Date().toString()+"; Test");
		if(id==-1) {
			model.addAttribute("transactions", transactions);
		} else {
			if(transaction!=null)
				model.addAttribute("transaction",transaction);
			else
				model.addAttribute("transaction","Transaction not found");
		}
		return "index"; //view
	}
}
