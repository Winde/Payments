package web.controllers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import configuration.thymeleaf.templating.Layout;

@Controller
@Layout(value = "layouts/default")
public class LoginController {

	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String loginGET(HttpServletRequest request){
		return "views/login";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String loginPOST(HttpServletRequest request){
		return "redirect:/payments";
	}
	
	/*
	@RequestMapping(value="/logout")
	public String logout(HttpServletRequest request){		
		return "redirect:/login";		
	}
	*/
	
}
