package com.bitbluesoftware.bpm.controller;

import com.bitbluesoftware.bpm.model.Bill;
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
public class CategoryController {
	Logger log = LoggerFactory.getLogger(CategoryController.class);
	
	@Autowired
	DAO dao;

    @Autowired
    AuthenticationManager authenticationManager;
	
	@RequestMapping("/categories")
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
		Map<Integer, Category> categories = dao.getCategories();
        Category category = null;
		if(id!=-1)
            category=categories.get(id);

		log.error(categories.size()+"");

		if(id==-1) {
			model.addAttribute("categories", categories.values());
		} else {
			if(category!=null)
				model.addAttribute("category",category);
			else
				model.addAttribute("error","Transaction not found");
		}
		return "categories"; //view
	}

    @RequestMapping("/add/category")
    public String addBill(@CookieValue(value = "bpmToken", defaultValue = "") String token,
                            @RequestParam(value = "name", defaultValue = "") String name,
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

        if(!name.isEmpty()) {
            dao.insertCategory(new Category(0, name));
            dao.refreshData();
            try {
                response.sendRedirect(response.encodeRedirectURL("/categories"));
            } catch(IOException e) {
                log.error(e.getMessage());
            }
        }

        return "addCategory"; //view
    }

    @RequestMapping("/edit/category")
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
                response.sendRedirect(response.encodeRedirectURL("/categories"));
            } catch(IOException e) {
                log.error(e.getMessage());
            }
        }

        Category category = dao.getCategories().get(Integer.parseInt(idString));

        if(category!=null)
            model.addAttribute("category",category);

       if (name.isEmpty() && amountString.isEmpty()) {
//                model.addAttribute("error","All fields are required");
        } else{
            if (category != null) {
                if(!name.isEmpty()) {
                    category.setName(name);
                }

                dao.updateCategory(category);
                dao.refreshData();

                try {
                    response.sendRedirect(response.encodeRedirectURL("/categories"));
                } catch(IOException e) {
                    log.error(e.getMessage());
                }
            } else {
                model.addAttribute("error", "Invalid Category ID");
            }
        }

        return "editCategory"; //view
    }

    @RequestMapping("/delete/category")
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
                response.sendRedirect(response.encodeRedirectURL("/categories"));
            } catch(IOException e) {
                log.error(e.getMessage());
            }
        }

        Category category = dao.getCategories().get(Integer.parseInt(idString));

        if(category!=null) {
            dao.deleteCategory(category);
            dao.refreshData();
        }

        try {
            response.sendRedirect(response.encodeRedirectURL("/categories"));
        } catch(IOException e) {
            log.error(e.getMessage());
        }

        return "index"; //view
    }
}
