package com.bitbluesoftware.bpm.controller;

import com.bitbluesoftware.bpm.util.DAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class WelcomeController {
	@Autowired
	DAO dao;
	
	@GetMapping("/api/1/transactions")
	public String main(Model model) {
		model.addAttribute("payload", dao.getTransactions());
		return "welcome"; //view
	}
	
	// /hello?name=kotlin
	@GetMapping("/hello")
	public String mainWithParam(@RequestParam(name = "name", required = false, defaultValue = "") String name, Model model) {
		model.addAttribute("message", name);
		return "welcome"; //view
	}
    
}
