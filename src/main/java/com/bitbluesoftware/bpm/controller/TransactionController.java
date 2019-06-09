package com.bitbluesoftware.bpm.controller;

import com.bitbluesoftware.bpm.model.*;
import com.bitbluesoftware.bpm.util.AuthenticationManager;
import com.bitbluesoftware.bpm.util.DAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Controller
public class TransactionController {
	Logger log = LoggerFactory.getLogger(TransactionController.class);
	
	@Autowired
	DAO dao;

    @Autowired
    AuthenticationManager authenticationManager;
	
	@RequestMapping("/transactions")
	public String transactions(@RequestParam(name = "id", required = false, defaultValue = "-1") String idString,
                               @CookieValue(value = "bpmToken", defaultValue = "") String token, Model model, HttpServletResponse response) {
        User user = null;
        if(!token.isEmpty()){
            user=dao.getToken(token);
            if (user != null) {
                model.addAttribute(user);
            } else {
                try {
                    response.sendRedirect(response.encodeRedirectURL("/login"));
                } catch(IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
		int id = Integer.parseInt(idString);
		Map<Integer, Transaction> transactions = dao.getTransactions();
		Transaction transaction = null;
		if(id!=-1)
			transaction=transactions.get(id);
		log.info(transactions.size()+"");
		model.addAttribute("message", new Date().toString()+"; Test");
		if(id==-1) {
			model.addAttribute("transactions", transactions.values());
		} else {
			if(transaction!=null)
				model.addAttribute("transaction",transaction);
			else
				model.addAttribute("error","Transaction not found");
		}
		return "transactions"; //view
	}
	
	private boolean checkDateIsValid(Date date) {
	    if(new Date().before(date)) {
	        return false;
        } else {
	        return true;
        }
    }

    @RequestMapping("/add/transaction")
    public String addTransactions(@CookieValue(value = "bpmToken", defaultValue = "") String token,
                                  @RequestParam(value = "name", defaultValue = "") String name,
                                  @RequestParam(value = "date", defaultValue = "") String dateString,
                                  @RequestParam(value = "category", defaultValue = "") String category,
                                  @RequestParam(value = "type", defaultValue = "") String type,
                                  @RequestParam(value = "account", defaultValue = "") String accountString,
                                  @RequestParam(value = "amount", defaultValue = "") String amountString,Model model, HttpServletResponse response) {
        User user = null;
        if(!token.isEmpty()){
            user=dao.getToken(token);
            if (user != null) {
                model.addAttribute(user);
            }
        }
        if(user==null) {
            try {
                response.sendRedirect(response.encodeRedirectURL("/login"));
            } catch(IOException e) {
                log.error(e.getMessage());
            }
        }
        model.addAttribute("categories",dao.getCategories().values());
        model.addAttribute("accounts",dao.getAccounts().values());
        log.error("Name: "+name+"\tAmount: "+amountString+"\tDate: "+dateString+"\tCategory: "+category);
        log.error(category+"\t"+(!category.isEmpty())+"\t"+((name.isEmpty() || dateString.isEmpty() || amountString.isEmpty())));
        if(!category.isEmpty())
            if(name.isEmpty() || dateString.isEmpty() || amountString.isEmpty())
                model.addAttribute("error","All fields are required");
        else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date1 = format.parse(dateString);
                
                if(!checkDateIsValid(date1)) {
                    model.addAttribute("error","Date cannot be in the future");
                    return "addTransaction";
                }
                java.sql.Date date = new java.sql.Date(date1.getTime());
                log.error("Check Date: "+(checkDateIsValid(date1))+" / "+dateString+" "+date1.toString()+"\t"+date.toString());
                Category category1 = dao.getCategories().get(Integer.parseInt(category));
                Double amount = Double.parseDouble(amountString);
                int accountId = Integer.parseInt(accountString);
                Account account = dao.getAccounts().get(accountId);
                if(type.equalsIgnoreCase("Debit"))
                    amount=amount*-1;
                if(category1==null)
                    log.error("NULL category");
                dao.insertTransaction(new Transaction(0, name, amount, category1 , user, date,account));
                dao.refreshData();
                try {
                    response.sendRedirect(response.encodeRedirectURL("/transactions"));
                } catch(IOException e) {
                    log.error(e.getMessage());
                }
            } catch(ParseException e) {
                log.error("Parse Exception");
            }
        }

        return "addTransaction"; //view
    }

    @RequestMapping("/edit/transaction")
    public String editTransaction(@CookieValue(value = "bpmToken", defaultValue = "") String token,
                                  @RequestParam(name = "id", required = false, defaultValue = "-1") String idString,
                                  @RequestParam(value = "name", defaultValue = "") String name,
                                  @RequestParam(value = "date", defaultValue = "") String dateString,
                                  @RequestParam(value = "category", defaultValue = "") String categoryString,
                                  @RequestParam(value = "type", defaultValue = "") String type,
                                  @RequestParam(value = "account", defaultValue = "") String accountString,
                                  @RequestParam(value = "amount", defaultValue = "") String amountString,Model model, HttpServletResponse response) {
        User user = null;
        if(!token.isEmpty()){
            user=dao.getToken(token);
            if (user != null) {
                model.addAttribute(user);
            }
        }
        if(user==null) {
            try {
                response.sendRedirect(response.encodeRedirectURL("/login"));
            } catch(IOException e) {
                log.error(e.getMessage());
            }
        }
        if(idString.equalsIgnoreCase("-1")) {
            try {
                response.sendRedirect(response.encodeRedirectURL("/transactions"));
            } catch(IOException e) {
                log.error(e.getMessage());
            }
        }

        model.addAttribute("categories",dao.getCategories().values());
        model.addAttribute("accounts",dao.getAccounts().values());
        Transaction transaction = dao.getTransactions().get(Integer.parseInt(idString));

        if(transaction!=null)
            model.addAttribute("transaction",transaction);

        if (name.isEmpty() && dateString.isEmpty() && amountString.isEmpty() && type.isEmpty() && categoryString.isEmpty() && accountString.isEmpty()) {
//                model.addAttribute("error","All fields are required");
        } else{
            if (transaction != null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if(!name.isEmpty()) {
                        transaction.setName(name);
                    }

                    if (!dateString.isEmpty()) {
                        Date date1 = format.parse(dateString);
                        if(!checkDateIsValid(date1)) {
                            model.addAttribute("error","Date cannot be in the future");
                            return "editTransaction";
                        }
                        java.sql.Date date = new java.sql.Date(date1.getTime());
                        transaction.setDate(date);
                    }
                    if (!categoryString.isEmpty()) {
                        Category category = dao.getCategories().get(Integer.parseInt(categoryString));
                        transaction.setCategory(category);
                    }

                    if (!amountString.isEmpty()) {
                        Double amount = Double.parseDouble(amountString);
                        transaction.setAmount(amount);
                    }

                    if (!accountString.isEmpty()) {
                        int accountId = Integer.parseInt(accountString);
                        Account account = dao.getAccounts().get(accountId);
                        transaction.setAccount(account);
                    }

                    dao.updateTransaction(transaction);
                    dao.refreshData();
                    try {
                        response.sendRedirect(response.encodeRedirectURL("/transactions"));
                    } catch(IOException e) {
                        log.error(e.getMessage());
                    }
                } catch (ParseException e) {
                    log.error("Parse Exception");
                }
            } else {
                model.addAttribute("error", "Invalid Transaction ID");
            }
        }


        return "editTransaction"; //view
    }

    @RequestMapping("/delete/transaction")
    public String deleteTransaction(@CookieValue(value = "bpmToken", defaultValue = "") String token,
                                    @RequestParam(name = "id", required = false, defaultValue = "-1") String idString,
                                    Model model, HttpServletResponse response) {
        User user = null;
        if(!token.isEmpty()){
            user=dao.getToken(token);
            if (user != null) {
                model.addAttribute(user);
            }
        }
        if(user==null) {
            try {
                response.sendRedirect(response.encodeRedirectURL("/login"));
            } catch(IOException e) {
                log.error(e.getMessage());
            }
        }
        if(idString.equalsIgnoreCase("-1")) {
            try {
                response.sendRedirect(response.encodeRedirectURL("/transactions"));
            } catch(IOException e) {
                log.error(e.getMessage());
            }
        }

        Transaction transaction = dao.getTransactions().get(Integer.parseInt(idString));

        if(transaction!=null) {
            dao.deleteTransaction(transaction);
            dao.refreshData();
        }

        try {
            response.sendRedirect(response.encodeRedirectURL("/transactions"));
        } catch(IOException e) {
            log.error(e.getMessage());
        }

        return "index"; //view
    }
}
