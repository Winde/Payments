package web.controllers;

import javax.validation.Valid;

import model.dataobjects.User;
import model.persistence.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import web.forms.PaymentForm;
import web.forms.UserForm;
import configuration.thymeleaf.templating.Layout;

@Secured("ROLE_ADMIN")
@Layout(value = "layouts/default")
@Controller
public class AdminController {

	
	@Autowired
	private UserRepository users;
	
	@RequestMapping(value="/admin", method=RequestMethod.GET)
	public String admin(Model model, @ModelAttribute UserForm userForm) {	
		userForm.clear();
		return "views/admin";
	}
	
	@RequestMapping(value="/admin", method=RequestMethod.POST)
	public String newUser(Model model, @Valid @ModelAttribute UserForm userForm, BindingResult result) {
		if (result.hasErrors() ){
			model.addAttribute("statusCode","error");
		} else {
			User user = new User();
			user.setName(userForm.getUsernameText());
			user.setPassword(userForm.getPasswordText());
			
			users.save(user);
			model.addAttribute("statusCode","success");
		}
		userForm.clear();
		
		
		return "views/admin";
	}
	
}
