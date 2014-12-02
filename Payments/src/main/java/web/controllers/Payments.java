package web.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import model.dataobjects.PaymentType;
import model.dataobjects.Transaction;
import model.dataobjects.User;
import model.persistence.PaymentRepository;
import model.persistence.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        return "payment";
    }
	
	@RequestMapping(value="/payments", method=RequestMethod.POST)
    public String paymentPOST(@Valid @ModelAttribute PaymentForm paymentForm, BindingResult result, Model model) {
		
		Long amount = null;
		try {
			amount = new Double(paymentForm.getAmount()*100).longValue();
		}catch (Exception ex){}

		if (result.hasErrors() || amount == null){
			if (result.hasErrors()){
				System.out.println("error");
			}
		} else {
		
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
			
			
			
			Transaction transaction = new Transaction(paymentForm.getType(),user,amount);				
			payments.save(transaction);
		}
		

        return "payment";
    }
 
	@RequestMapping(value="/transactions")
	public String transactions(Model model){
		List<Transaction> transactions = payments.findAll();
		
		Map<PaymentType,List<Transaction>> transactionMap = new HashMap<>();
		Map<PaymentType,Double> transactionStatistics = new HashMap<>();
		if (transactions!=null){
			for (Transaction transaction: transactions) {
				PaymentType type = transaction.getType();
				List<Transaction> listTransactions = transactionMap.get(type);
				Double value = transactionStatistics.get(type);
				if (listTransactions==null){
					listTransactions = new ArrayList<>();					
					transactionMap.put(type,listTransactions);
				}		
				if (value ==null) {
					value = new Double(0.0);
				}
			
				listTransactions.add(transaction);
				value = new Double(value + (transaction.getAmount()/100));
				transactionStatistics.put(type,value);
				
			}	
		}
		
		model.addAttribute("statistics", transactionStatistics);
		model.addAttribute("transactions", transactionMap);
		
		return "transactions";
	}
	
}
