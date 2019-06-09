package com.bitbluesoftware.bpm.controller;

import com.bitbluesoftware.bpm.model.Account;
import com.bitbluesoftware.bpm.model.Bill;
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
public class BillController {
	Logger log = LoggerFactory.getLogger(BillController.class);
	
	@Autowired
	DAO dao;

    @Autowired
    AuthenticationManager authenticationManager;
	
	@RequestMapping("/bills")
	public String transactions(@RequestParam(name = "id", required = false, defaultValue = "-1") String idString,
                               @CookieValue(value = "bpmToken", defaultValue = "") String token, Model model, HttpServletResponse response) {
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
		int id = Integer.parseInt(idString);
		Map<Integer, Bill> bills = dao.getBills();
		Bill bill = null;
		if(id!=-1)
			bill=bills.get(id);

		if(id==-1) {
			model.addAttribute("bills", bills.values());
		} else {
			if(bill!=null)
				model.addAttribute("bill",bill);
			else
				model.addAttribute("error","Transaction not found");
		}
		return "bills"; //view
	}

    @RequestMapping("/add/bill")
    public String addBill(@CookieValue(value = "bpmToken", defaultValue = "") String token,
                            @RequestParam(value = "name", defaultValue = "") String name,
                            @RequestParam(value = "amount", defaultValue = "") String amountString,
                            @RequestParam(value = "date", defaultValue = "") String dateString,
                            @RequestParam(value = "transaction", defaultValue = "") String transactionString,
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
        Map<Integer, Transaction> transactions = dao.getTransactions();
        model.addAttribute("transactions",transactions.values());

        if(!name.isEmpty())
            if(name.isEmpty() || amountString.isEmpty() || dateString.isEmpty())
                model.addAttribute("error","All fields except Transaction are required");
        else {

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Double amount = Double.parseDouble(amountString);
                Date utilDate = format.parse(dateString);
                java.sql.Date date = new java.sql.Date(utilDate.getTime());
                Transaction transaction = null;
                if(!transactionString.isEmpty()) {
                    transaction = dao.getTransactions().get(Integer.parseInt(transactionString));
                }
                dao.insertBill(new Bill(0, name, amount, date , user, transaction));
                dao.refreshData();
            } catch (ParseException e) {
                log.error("Parse Exception");
            }

            try {
                response.sendRedirect(response.encodeRedirectURL("/bills"));
            } catch(IOException e) {
                log.error(e.getMessage());
            }
        }

        return "addBill"; //view
    }

    @RequestMapping("/edit/bill")
    public String editBill(@CookieValue(value = "bpmToken", defaultValue = "") String token,
                                  @RequestParam(name = "id", required = false, defaultValue = "-1") String idString,
                                  @RequestParam(value = "name", defaultValue = "") String name,
                                  @RequestParam(value = "amount", defaultValue = "") String amountString,
                                  @RequestParam(value = "date", defaultValue = "") String dateString,
                                  @RequestParam(value = "transaction", defaultValue = "") String transactionString,Model model, HttpServletResponse response) {
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
        if(idString.equals("-1")) {
            try {
                response.sendRedirect(response.encodeRedirectURL("/bills"));
            } catch(IOException e) {
                log.error(e.getMessage());
            }
        }

        Bill bill = dao.getBills().get(Integer.parseInt(idString));
        model.addAttribute("transactions",dao.getTransactions().values());

        if(bill!=null)
            model.addAttribute("bill",bill);

       if (name.isEmpty() && amountString.isEmpty()) {
//                model.addAttribute("error","All fields are required");
        } else{
            if (bill != null) {
                if(!name.isEmpty()) {
                    bill.setName(name);
                }

                if (!amountString.isEmpty()) {
                    Double amount = Double.parseDouble(amountString);
                    bill.setAmount(amount);
                }

                if(!transactionString.isEmpty()) {
                    Transaction transaction = dao.getTransactions().get(Integer.parseInt(transactionString));
                    bill.setTransaction(transaction);
                }

                if(!dateString.isEmpty()) {

                    try {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date utilDate = format.parse(dateString);
                        java.sql.Date date = new java.sql.Date(utilDate.getTime());
                        bill.setDate(date);
                    } catch (ParseException e) {
                        log.error("Parse Exception");
                    }
                }
                dao.updateBill(bill);
                dao.refreshData();

                try {
                    response.sendRedirect(response.encodeRedirectURL("/bills"));
                } catch(IOException e) {
                    log.error(e.getMessage());
                }
            } else {
                model.addAttribute("error", "Invalid Bill ID");
            }
        }

        return "editBill"; //view
    }

    @RequestMapping("/delete/bill")
    public String deleteBill(@CookieValue(value = "bpmToken", defaultValue = "") String token,
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
                response.sendRedirect(response.encodeRedirectURL("/bills"));
            } catch(IOException e) {
                log.error(e.getMessage());
            }
        }

        Bill bill = dao.getBills().get(Integer.parseInt(idString));

        if(bill!=null) {
            dao.deleteBill(bill);
            dao.refreshData();
        }

        try {
            response.sendRedirect(response.encodeRedirectURL("/bills"));
        } catch(IOException e) {
            log.error(e.getMessage());
        }

        return "index"; //view
    }
}
