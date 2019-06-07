package com.bitbluesoftware.bpm.controller;

import com.bitbluesoftware.bpm.model.Account;
import com.bitbluesoftware.bpm.model.Category;
import com.bitbluesoftware.bpm.model.Transaction;
import com.bitbluesoftware.bpm.model.User;
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
public class AccountController {
	Logger log = LoggerFactory.getLogger(AccountController.class);
	
	@Autowired
	DAO dao;

    @Autowired
    AuthenticationManager authenticationManager;
	
	@RequestMapping("/accounts")
	public String transactions(@RequestParam(name = "id", required = false, defaultValue = "-1") String idString,
                               @CookieValue(value = "bpmToken", defaultValue = "") String token, Model model, HttpServletResponse response) {
        User user = null;
        if(!token.isEmpty()){
            user=dao.getToken(token);
            log.error(dao.getTokens().toString());
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
		Map<Integer, Account> accounts = dao.getAccounts();
		Account account = null;
		if(id!=-1)
			account=accounts.get(id);

		if(id==-1) {
			model.addAttribute("accounts", accounts.values());
		} else {
			if(account!=null)
				model.addAttribute("account",account);
			else
				model.addAttribute("error","Transaction not found");
		}
		return "accounts"; //view
	}

    @RequestMapping("/add/account")
    public String addTransactions(@CookieValue(value = "bpmToken", defaultValue = "") String token,
                                  @RequestParam(value = "name", defaultValue = "") String name,
                                  @RequestParam(value = "bank", defaultValue = "") String bank,
                                  @RequestParam(value = "balance", defaultValue = "") String balanceString,
                                  @RequestParam(value = "type", defaultValue = "") String type,Model model, HttpServletResponse response) {
        User user = null;
        if(!token.isEmpty()){
            user=dao.getToken(token);
            log.error(dao.getTokens().toString());
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

        if(!type.isEmpty())
            if(name.isEmpty() || bank.isEmpty() || type.isEmpty())
                model.addAttribute("error","All fields are required");
        else {
            Double balance = Double.parseDouble(balanceString);

            dao.insertAccount(new Account(0, name, bank, balance , user, type));
            dao.refreshData();
            try {
                response.sendRedirect(response.encodeRedirectURL("/accounts"));
            } catch(IOException e) {
                log.error(e.getMessage());
            }
        }

        return "addAccount"; //view
    }

    @RequestMapping("/edit/account")
    public String editTransaction(@CookieValue(value = "bpmToken", defaultValue = "") String token,
                                  @RequestParam(name = "id", required = false, defaultValue = "-1") String idString,
                                  @RequestParam(value = "name", defaultValue = "") String name,
                                  @RequestParam(value = "bank", defaultValue = "") String bank,
                                  @RequestParam(value = "type", defaultValue = "") String type,
                                  @RequestParam(value = "balance", defaultValue = "") String balanceString,Model model, HttpServletResponse response) {
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
                response.sendRedirect(response.encodeRedirectURL("/accounts"));
            } catch(IOException e) {
                log.error(e.getMessage());
            }
        }

        Account account = dao.getAccounts().get(Integer.parseInt(idString));

        if(account!=null)
            model.addAttribute("account",account);

        if (name.isEmpty() && balanceString.isEmpty()) {
//                model.addAttribute("error","All fields are required");
        } else{
            if (account != null) {
                if(!name.isEmpty()) {
                    account.setName(name);
                }

                if (!balanceString.isEmpty()) {
                    Double amount = Double.parseDouble(balanceString);
                    account.setBalance(amount);
                }

                if(!bank.isEmpty()) {
                    account.setBank(bank);
                }

                if(!type.isEmpty()) {
                    account.setType(type);
                }

                dao.updateAccount(account);
                dao.refreshData();

                try {
                    response.sendRedirect(response.encodeRedirectURL("/accounts"));
                } catch(IOException e) {
                    log.error(e.getMessage());
                }
            } else {
                model.addAttribute("error", "Invalid Transaction ID");
            }
        }


        return "editAccount"; //view
    }

    @RequestMapping("/delete/account")
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
                response.sendRedirect(response.encodeRedirectURL("/accounts"));
            } catch(IOException e) {
                log.error(e.getMessage());
            }
        }

        Account account = dao.getAccounts().get(Integer.parseInt(idString));

        if(account!=null) {
            dao.deleteTransactionsFromAccount(account);
            dao.deleteAccount(account);
            dao.refreshData();
        }

        try {
            response.sendRedirect(response.encodeRedirectURL("/accounts"));
        } catch(IOException e) {
            log.error(e.getMessage());
        }

        return "index"; //view
    }
}
