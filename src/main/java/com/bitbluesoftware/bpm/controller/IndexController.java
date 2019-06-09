package com.bitbluesoftware.bpm.controller;

import com.bitbluesoftware.bpm.model.*;
import com.bitbluesoftware.bpm.util.AuthenticationManager;
import com.bitbluesoftware.bpm.util.DAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;

@Controller
public class IndexController {
	Logger log = LoggerFactory.getLogger(IndexController.class);
	
	@Autowired
	DAO dao;

    @Autowired
    AuthenticationManager authenticationManager;
	
	@RequestMapping("/")
	public String index(@CookieValue(value = "bpmToken", defaultValue = "") String token, Model model, HttpServletResponse response) {
		User user = null;
		if(!token.isEmpty()){
			user=dao.getToken(token);
		}
		if (user != null) {
			model.addAttribute(user);
		} else {
			try {
				response.sendRedirect(response.encodeRedirectURL("/login"));
			} catch(IOException e) {
				log.error(e.getMessage());
			}
		}
        Map<Integer, Transaction> transactions = dao.getTransactions();
        Map<Integer, Account> accounts = dao.getAccounts();
        Map<Integer, Bill> bills = dao.getBills();
        Map<Integer, Category> categories = dao.getCategories();
		model.addAttribute("message", new Date().toString());
        model.addAttribute("transactions",transactions.values());
        model.addAttribute("accounts",accounts.values());
        model.addAttribute("bills",bills.values());
        model.addAttribute("categories",categories.values());
		return "index"; //view
	}
}
