package web.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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

import com.fasterxml.jackson.databind.ObjectMapper;

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
			model.addAttribute("statusCode","success");
		}
		

        return "payment";
    }
 
	@RequestMapping(value="/transactions")
	public String transactions(Model model){
		List<Transaction> transactions = payments.findAll();
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY");
		
		/*
		Map<String,Map<PaymentType,Double>> result = new TreeMap<>();
		
		for (Transaction transaction: transactions) {
			Date date = transaction.getDate();
			PaymentType type = transaction.getType();
			if (date!=null && type!=null){
				String key = format.format(date);
				Map<PaymentType, Double> mapForDate = result.get(key);
				if (mapForDate==null) {
					mapForDate = new TreeMap<>();
					for (PaymentType iteration : PaymentType.values()) {
						mapForDate.put(iteration, new Double(0.0));
					}
					result.put(key,mapForDate);
				}
				Double amount = mapForDate.get(type);
				if (amount ==null) { amount = new Double(0.0); }
				amount = amount + transaction.getRealAmount();
				mapForDate.put(type, amount);				
			}						
		}
		*/
		
		
		Map<PaymentType,Map<String,Double>> result = new TreeMap<>();
		Set<String> dates = new HashSet<>();
		
		for (Transaction transaction: transactions) {
			Date date = transaction.getDate();
			PaymentType type = transaction.getType();
			if (date!=null && type!=null){
				String key = format.format(date);
				Map<String, Double> mapForType = result.get(type);
				if (mapForType==null) {
					mapForType = new TreeMap<>();					
					result.put(type,mapForType);
				}
				Double amount = mapForType.get(key);
				if (amount ==null) { amount = new Double(0.0); }
				amount = amount + transaction.getRealAmount();								
				mapForType.put(key, amount);				
				for (PaymentType iteration: result.keySet() ){
					Double amountForCombination = result.get(iteration).get(key);
					if (amountForCombination==null) {
						 result.get(iteration).put(key,new Double(0.0));
					}					
				}
				
				dates.add(key);
				
			}						
		}
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(System.out, result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		model.addAttribute("dates",dates);
		model.addAttribute("statistics",result);
		
		return "transactions";
	}
	
}
