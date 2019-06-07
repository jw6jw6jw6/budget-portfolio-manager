package com.bitbluesoftware.bpm.controller;

import com.bitbluesoftware.bpm.model.User;
import com.bitbluesoftware.bpm.util.AuthenticationManager;
import com.bitbluesoftware.bpm.util.DAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Controller
public class AuthenticatonController {
    Logger log = LoggerFactory.getLogger(AuthenticatonController.class);

    @Autowired
    DAO dao;

    @Autowired
    AuthenticationManager authenticationManager;

    @RequestMapping("/login")
    public String login(@RequestParam(name = "username", required = false, defaultValue = "") String username,
                        @RequestParam(name = "password", required = false, defaultValue = "") String password,
                        @CookieValue(value = "bpmToken", defaultValue = "") String token, Model model, HttpServletResponse response) {
        User user = null;
        if(!token.isEmpty()){
            user=dao.getToken(token);
        }
        if (!username.isEmpty() && !password.isEmpty())
            user = authenticationManager.authenticate(username, password);

        if (user == null) {
            if (!username.isEmpty() && !password.isEmpty()) {
                model.addAttribute("error","Invalid username and password");
            } else if (!username.isEmpty() && password.isEmpty()) {
                model.addAttribute("error", "Username and Password are required");
            }
            if(!token.isEmpty()) {
                response.addCookie(new Cookie("bpmToken",""));
            }
        } else {
            model.addAttribute(user);
            if(token.isEmpty()){
                String token1 = authenticationManager.generateToken();
                dao.addToken(token1,user);
                Cookie cookie = new Cookie("bpmToken",token1);
                cookie.setMaxAge(28800);
                response.addCookie(cookie);
                user.setLastLogin(new Date());
                user.setLoginCount(user.getLoginCount()+1);
                dao.updateUser(user);
                dao.refreshData();
                try {
                    response.sendRedirect(response.encodeRedirectURL("/profile"));
                } catch(IOException e) {
                    log.error(e.getMessage());
                }
            }
        }

        return "login"; //view
    }

    @GetMapping("/logout")
    public String logout(@CookieValue(value = "bpmToken", defaultValue = "") String token, Model model, HttpServletResponse response) {
        User user = null;
        if(!token.isEmpty()){
            Cookie cookie = new Cookie("bpmToken","");
            cookie.setMaxAge(1);
            response.addCookie(cookie);
            dao.removeToken(token);
        }
        log.error(dao.getTokens().size()+"");
        model.addAttribute("message","You have been successfully logged out.");
        return "index"; //view
    }
}
