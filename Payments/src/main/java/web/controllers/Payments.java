package web.controllers;

import java.util.List;

import model.dataobjects.PaymentType;
import model.dataobjects.Transaction;
import model.dataobjects.User;
import model.persistence.PaymentRepository;
import model.persistence.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import web.forms.PaymentForm;

@Controller
public class Payments {
    
	@Autowired
	private PaymentRepository payments;
	
	@Autowired
	private UserRepository users;
	
	@RequestMapping(value="/payments", method=RequestMethod.GET)
    public String paymentGET(@ModelAttribute PaymentForm paymentForm, Model model) {	
		List<Transaction> transactions = payments.findAll();
		model.addAttribute("transactions", transactions);
        return "payment";
    }
	
	@RequestMapping(value="/payments", method=RequestMethod.POST)
    public String paymentPOST(@ModelAttribute PaymentForm paymentForm, Model model) {
		String name = paymentForm.getName();
		User user = null;
		if (name!=null) {
			List<User> listUsers = users.findByName(name);
			if (listUsers!=null && listUsers.size()>0) {
				user = listUsers.get(0);
				
			}
		}
		
		if (user == null) {
			user = new User();
			user.setName(paymentForm.getName());				
			users.save(user);
		}
		
		Transaction transaction = new Transaction(PaymentType.Eating_Out,user,(long) 3);				
		payments.save(transaction);
		
		List<Transaction> transactions = payments.findAll();
		model.addAttribute("transactions", transactions);
		
        return "payment";
    }
    
}
