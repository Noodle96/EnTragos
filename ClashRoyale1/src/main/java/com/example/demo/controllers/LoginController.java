package com.example.demo.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
	@GetMapping("/login")
	public String login(@RequestParam(value="error", required=false) String error,  Model model, Principal principal,
			RedirectAttributes flash) {
		//para evitar que haga doble inicio de sesion
		if(principal !=  null) {
			flash.addFlashAttribute("info", "Ya ha iniciado sesion anteriormete");
			return "redirect:/";
		}
		if(error != null) {
			model.addAttribute("error", "Error al login: Nombre o password Incorrect, Try Again!!");
			
		}
		return "login";
	}
}
