package web.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.validation.Valid;

import model.dataobjects.IncomeEntry;
import model.dataobjects.PaymentType;
import model.dataobjects.Payment;
import model.dataobjects.User;
import model.persistence.IncomeRepository;
import model.persistence.PaymentRepository;
import model.persistence.UserRepository;
import model.statistics.Statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

import web.assisting.Statistic;
import web.forms.PaymentForm;

@Controller
public class PaymentsController {
    
	@Autowired
	private PaymentRepository payments;
	
	@Autowired
	private UserRepository users;
	
	@Autowired
	private IncomeRepository incomeEntries;
	
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
			
			
			if (Boolean.TRUE.equals(paymentForm.getIncome())) {			
				IncomeEntry incomeEntry = new IncomeEntry(user,amount);
				incomeEntries.save(incomeEntry);
			}else {
				Payment transaction = new Payment(paymentForm.getType(),user,amount);				
				payments.save(transaction);
			}
			
			paymentForm.clear();
			model.addAttribute("statusCode","success");
		}
		

        return "payment";
    }
 
	@RequestMapping(value="/statistics/{month}")
	public String statistics(@PathVariable String month, Model model){
		Date date = null;
		if (month!=null) {
			date = getMonthFromString(month);
		}

		List<Payment> transactions = null;
		
		if (date==null) {
			transactions = payments.findAll();
		} else {			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			Date from = calendar.getTime();
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			Date to = calendar.getTime();
			transactions = payments.findByDateBetween(from, to);
		}
		
		
		Collection<Statistic> pieChart = Statistics.getPieChart(transactions);
		Collection<Statistic> stackedChart = Statistics.getStackedValuesPerDate(transactions);		
		
		
		model.addAttribute("pieChart",pieChart);
		model.addAttribute("stackedChart",stackedChart);
		
		
		return "statistics";
	}
	
	@RequestMapping(value="/transactions/{month}")
	public String transactions(@PathVariable String month, Model model){
		Date date = null;
		if (month!=null) {
			date = getMonthFromString(month);
		}
		
		List<Payment> transactionsNegative = null;
		List<IncomeEntry> transactionsPositive = null;
		
		if (date==null) {
			transactionsNegative = payments.findAll();
		} else {			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			Date from = calendar.getTime();
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			Date to = calendar.getTime();
			transactionsNegative = payments.findByDateBetween(from, to);
			transactionsPositive = incomeEntries.findByDateBetween(from, to);
		}
		
		
		Map<String, Map<String, Double>> table = Statistics.getTablePayments(transactionsNegative);
		
		Map<String, Double> tableIncome = Statistics.getTableIncome(transactionsPositive);
		if (tableIncome!=null && !tableIncome.isEmpty()) {
			table.put("Income", tableIncome);
		}
		
		Set<String> dates = new TreeSet<>();
		
		
		for (String type : table.keySet()) {
			Map<String, Double> current = table.get(type);
			if (current!=null){
				dates.addAll(current.keySet());
			}
		}
		
		model.addAttribute("dates",dates);
		model.addAttribute("statisticsFull",table);

		return "transactions";
	}
	
	private Date getMonthFromString(String dateString){
		SimpleDateFormat format = new SimpleDateFormat("MM-yyyy");
		Date date = null;
		try {
			date = format.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			
		}
		return date;
		
	}
	
}
