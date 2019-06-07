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
import java.util.Date;
import java.util.Map;

@Controller
public class ProfileController {

    Logger log = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    DAO dao;

    @Autowired
    AuthenticationManager authenticationManager;

    @RequestMapping("/profile")
    public String profile(@RequestParam(name = "edit", required = false, defaultValue = "") String edit,
                        @CookieValue(value = "bpmToken", defaultValue = "") String token, Model model, HttpServletResponse response) {
        User user = null;
        if(!token.isEmpty()){
            user=dao.getToken(token);
            if (user != null) {
                model.addAttribute(user);
            }
        }

        if(user!=null) {
            model.addAttribute("user",user);
        } else {
            model.addAttribute("error","You are not signed in");
            try {
                response.sendRedirect(response.encodeRedirectURL("/login"));
            } catch(IOException e) {
                log.error(e.getMessage());
            }
        }
        return "profile"; //view
    }

    @RequestMapping("/profileedit")
    public String profileedit(@RequestParam(name = "firstname", required = false, defaultValue = "") String firstname,
                              @RequestParam(name = "lastname", required = false, defaultValue = "") String lastname,
                              @RequestParam(name = "email", required = false, defaultValue = "") String email,
                              @RequestParam(name = "password", required = false, defaultValue = "") String password,
                              @CookieValue(value = "bpmToken", defaultValue = "") String token, Model model, HttpServletResponse response) {
        User user = null;
        if(!token.isEmpty()){
            user=dao.getToken(token);
            if (user != null) {
                model.addAttribute(user);
            }
        }

        model.addAttribute("edit",true);

        if(user!=null) {
            model.addAttribute("user",user);
            if(!firstname.isEmpty() || !lastname.isEmpty() || !email.isEmpty() || !password.isEmpty()) {
                if(!firstname.isEmpty())
                    user.setFirstName(firstname);
                if(!lastname.isEmpty())
                    user.setLastName(lastname);
                if(!email.isEmpty())
                    user.setEmail(email);
                if(!password.isEmpty())
                    user.setPassword(authenticationManager.hashPassword(password));
                dao.updateUser(user);
                dao.refreshData();
                try {
                    response.sendRedirect(response.encodeRedirectURL("/profile"));
                } catch(IOException e) {
                    log.error(e.getMessage());
                }
            }
        } else {
            model.addAttribute("error","You are not signed in");
            try {
                response.sendRedirect(response.encodeRedirectURL("/login"));
            } catch(IOException e) {
                log.error(e.getMessage());
            }
        }
        return "profile"; //view
    }
}
