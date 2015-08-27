package web.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import model.dataobjects.IncomeEntry;
import model.dataobjects.Payment;
import model.dataobjects.PaymentType;
import model.dataobjects.Tag;
import model.dataobjects.User;
import model.dataobjects.reader.EVOReader;
import model.dataobjects.reader.INGReader;
import model.dataobjects.reader.StatementReader;
import model.persistence.CartLineRepository;
import model.persistence.IncomeRepository;
import model.persistence.PaymentRepository;
import model.persistence.TagRepository;
import model.statistics.Movements;
import model.statistics.Statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import web.assisting.Statistic;
import web.forms.PaymentForm;
import configuration.thymeleaf.templating.Layout;

@Controller
@Layout(value = "layouts/default")
public class PaymentsController {
    
	@Autowired
	private PaymentRepository payments;

	@Autowired
	private IncomeRepository incomeEntries;
	
	@Autowired
	private CartLineRepository cartLines;
	
	@Autowired
	private TagRepository tagRepository;
	
	
	@RequestMapping(value="/payments", method=RequestMethod.GET)
    public String paymentGET(@ModelAttribute PaymentForm paymentForm, Model model) {
		paymentForm.setDate(new Date());
		
		List<Tag> tags = tagRepository.findAll();
		model.addAttribute("tags", tags);
        return "views/payment";
    }
	
	
	@RequestMapping(value="/payments", method=RequestMethod.POST)
    public String paymentPOST(@Valid @ModelAttribute PaymentForm paymentForm, BindingResult result, Model model) {
		boolean error = false;
		Long amount = paymentForm.getAmountLong();
		List<Tag> tags = tagRepository.findAll();
		
		if (result.hasErrors() || amount == null){
			if (result!=null && result.getAllErrors()!=null && result.getAllErrors().size()>0){
				System.out.println(result.getAllErrors().get(0).getCode());
				System.out.println(result.getAllErrors().get(0).getDefaultMessage());
				System.out.println(result.getAllErrors().get(0).getObjectName());
			}
			 error = true;
		} else {
			List<Tag> paymentTags = Tag.createTags(paymentForm.getTags(), tags);
			for (Tag tag: tags) {
				tag.setUsage(tag.getUsage()+1);
			}
			tagRepository.save(tags);
			
			User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			if (Boolean.TRUE.equals(paymentForm.getIncome())) {			
				IncomeEntry incomeEntry = new IncomeEntry(user,amount);				
				incomeEntry.setDate(paymentForm.getDate());				
				incomeEntries.save(incomeEntry);
			}else {
				if (paymentForm.getType()==null){
					error = true;
				} else {
					Payment transaction = new Payment(paymentForm.getType(),user,amount);
					transaction.setComments(paymentForm.getComments());
					transaction.setDate(paymentForm.getDate());
					transaction.setTags(paymentTags);
					payments.save(transaction);
				}
			}
			
			paymentForm.clear();
			
		}
		
		
		model.addAttribute("tags", tags);
		
		if (error){
			model.addAttribute("statusCode","error");
		} else {
			model.addAttribute("statusCode","success");
		}
        return "views/payment";
    }
	
	@RequestMapping(value="/uploadBank", method=RequestMethod.POST)
    public String uploadBank(@RequestParam("file") MultipartFile file,@RequestParam("bank") String bank, Model model) {

		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		StatementReader reader = null;
		if ("EVO".equals(bank)){
			reader = new EVOReader();
		} else if ("ING".equals(bank)){
			reader = new INGReader();
		}			
		if (reader!=null){
			Movements movements = null;
			try {
				movements = reader.getMovements(user, file.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			if (movements!=null && movements.getIncomeEntries()!=null) {
				incomeEntries.save(movements.getIncomeEntries());
			}
			if (movements!=null && movements.getPayments()!=null) {
				payments.save(movements.getPayments());
			}
			if (movements!=null){		
				model.addAttribute("statusCode","success");			
			} else {
				System.out.println("Exception: Movements is null");
				model.addAttribute("statusCode","error");
			}
		} else {
			System.out.println("Exception: Reader is null");
			model.addAttribute("statusCode","error");
		}
		return "redirect:/transactions";
    }
	
	
	@RequestMapping(value="/deleteIncome/{id}")
    public String deleteIncome(@PathVariable String id, Model model, HttpServletRequest request) {
		
		Long idNum = Long.parseLong(id);

		try {
			incomeEntries.delete(idNum);
		}catch (Exception ex){
			ex.printStackTrace();
			model.addAttribute("statusCode","error");
		}
		
		String referer = request.getHeader("Referer");
		return "redirect:"+ referer + "#transactions";

	}
	
	@RequestMapping(value="/deleteTransaction/{id}")
    public String deleteTransaction(@PathVariable String id, Model model, HttpServletRequest request) {
		
		Long idNum = Long.parseLong(id);
		try {
			payments.delete(idNum);
		}catch (Exception ex){
			ex.printStackTrace();
			model.addAttribute("statusCode","error");
		}
		
		String referer = request.getHeader("Referer");
		return "redirect:"+ referer + "#transactions";

	}
	
	@RequestMapping(value="/typeTransaction")
    public String typeTransaction(@RequestParam("id") String id,@RequestParam("type") String type, Model model, HttpServletRequest request) {
		
		Long idNum = Long.parseLong(id);
		Payment payment = payments.findOne(idNum);
		
		payment.setType(PaymentType.valueOf(type));
		
		try {
			payments.save(payment);
		}catch (Exception ex){
			ex.printStackTrace();
			model.addAttribute("statusCode","error");
		}
		
		
		String referer = request.getHeader("Referer");
		return "redirect:"+ referer + "#transactions";

	}
	
	@RequestMapping(value="/impute/{id}",  method=RequestMethod.GET)
    public String imputeGET(@PathVariable String id,Model model,@ModelAttribute PaymentForm paymentForm) {
		
		Long idNum = Long.parseLong(id);
		Payment payment = payments.findOne(idNum);
		paymentForm.setDate(new Date());
		
		if (payment==null || !PaymentType.ATMCheckout.equals(payment.getType())) {
			return "redirect:/error" ;
		} else {			
			model.addAttribute("payment",payment);			
		}
				
		 return "views/impute";

	}
	
	@RequestMapping(value="/impute/{id}",  method=RequestMethod.POST)
    public String imputePOST(@PathVariable String id, Model model, @Valid @ModelAttribute PaymentForm paymentForm, BindingResult result) {
		
		Long idNum = Long.parseLong(id);
		Payment payment = payments.findOne(idNum);
		
		boolean error = false;
		if (payment==null || !PaymentType.ATMCheckout.equals(payment.getType())) {
			error = true;
			return "redirect:/error" ;
		} else {			
			model.addAttribute("payment",payment);	
			

			Long amount = paymentForm.getAmountLong();
			
			if (result.hasErrors() || amount == null || amount > payment.getAmount()){
				if (result!=null && result.getAllErrors()!=null && result.getAllErrors().size()>0){
					System.out.println(result.getAllErrors().get(0).getCode());
					System.out.println(result.getAllErrors().get(0).getDefaultMessage());
					System.out.println(result.getAllErrors().get(0).getObjectName());
				}
				 error = true;
				
			} else {
						
				
				User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				
				
				if (paymentForm.getType()==null){
					error = true;
				} else if (amount == payment.getAmount()){
					Payment transaction = new Payment(paymentForm.getType(),user,amount);
					transaction.setComments(paymentForm.getComments());
					transaction.setDate(paymentForm.getDate());
										
					payments.save(transaction);					
					payments.delete(payment);
				} else {
					Payment transaction = new Payment(paymentForm.getType(),user,amount);
					transaction.setComments(paymentForm.getComments());
					transaction.setDate(paymentForm.getDate());
					
					payment.setAmount(payment.getAmount()-amount);
					
					
					payments.save(transaction);					
					payments.save(payment);
				}
				
				
				paymentForm.clear();
				
			}
											
		}
		
		if (error) {
			 model.addAttribute("statusCode","error");
		} else {
			model.addAttribute("statusCode","success");
		}
				
		 return "views/impute/"+id;

	}
	
	
	@RequestMapping(value="/statistics")
	public String statisticsPerMonth(Model model){
		

		List<Payment> transactions = null;
		List<IncomeEntry> incomes = null;	
		transactions = payments.findAll();
		incomes = incomeEntries.findAll();
				
		Collection<Statistic> pieChart = Statistics.getPieChart(transactions);
		Collection<Statistic> barChart = Statistics.getValuesPerMonth(transactions);
		Collection<Statistic> barChart2 = Statistics.getSavedPerMonth(transactions,incomes);
		
		Map<String, Collection<Statistic>> multipleChart = Statistics.getValuesPerTypePerMonth(transactions);		
		
		
		
		List<Statistic> cartChart = cartLines.findStatistic();
		List<Statistic> cartChart2 = cartLines.findStatisticByCategory();
		
		Double totalValueAll = 0.0;
		for (Statistic statistic : cartChart){
			totalValueAll = totalValueAll+ statistic.getValue();
		}
		
		Double totalValueCategorized = 0.0;
		for (Statistic statistic : cartChart2){
			totalValueCategorized = totalValueCategorized + statistic.getValue();
		}
		
		Double totalValueGroceries = 0.0;
		for (Payment transaction : transactions){
			if (PaymentType.Groceries.equals(transaction.getType())){
				totalValueGroceries = totalValueGroceries + transaction.getRealAmount();
			}
		}
		
		Double otherValue = totalValueAll - totalValueCategorized;
		Statistic otherValueStatistic = new Statistic();
		otherValueStatistic.setTitle("Other");
		otherValueStatistic.setValue(otherValue);
		if (otherValue>0.0){
			cartChart2.add(otherValueStatistic);
		}
		
		
		Double nonCategorizedValue = totalValueGroceries - totalValueAll;		
		Statistic pendingTicketValueStatistic = new Statistic();
		pendingTicketValueStatistic.setTitle("Pending Ticket");
		pendingTicketValueStatistic.setValue(nonCategorizedValue);
		if (cartChart2!=null && nonCategorizedValue>0.0) {
			//Removed due to adding too much noise
			//cartChart2.add(pendingTicketValueStatistic);
		}
		if (cartChart!=null && nonCategorizedValue>0.0) {
			//Removed due to adding too much noise
			//cartChart.add(pendingTicketValueStatistic);
		}
		
		model.addAttribute("pieChart",pieChart);
		//model.addAttribute("stackedChart",stackedChart);
		model.addAttribute("barChart",barChart);
		model.addAttribute("barChart2",barChart2);
		model.addAttribute("multipleChart",multipleChart);
		model.addAttribute("cartChart",cartChart);
		model.addAttribute("cartChart2",cartChart2);
		
		
		return "views/statistics";
	}
	
 
	@RequestMapping(value="/statistics/{month}")
	public String statistics(@PathVariable String month, Model model){
		Date date = null;
		if (month!=null) {
			date = getMonthFromString(month);
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
		
		List<Payment> transactions = null;
		
		List<Statistic> cartChart = null;
		List<Statistic> cartChart2 = null;
		if (date==null) {
			transactions = payments.findAll();
		} else {			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			Date from = calendar.getTime();
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			Date to = calendar.getTime();
			transactions = payments.findLazyByDateBetween(from, to);
			cartChart = cartLines.findStatistic(from,to);
			cartChart2 = cartLines.findStatisticByCategory(from,to);
		}
		
		
		
		Double totalValueAll = 0.0;
		if (cartChart!=null){
			for (Statistic statistic : cartChart){
				totalValueAll = totalValueAll+ statistic.getValue();
			}
		}
		
		
		Double totalValueCategorized = 0.0;
		if (cartChart2!=null){
			for (Statistic statistic : cartChart2){
				totalValueCategorized = totalValueCategorized + statistic.getValue();
			}
		}
		
		Double totalValueGroceries = 0.0;
		for (Payment transaction : transactions){
			if (PaymentType.Groceries.equals(transaction.getType())){
				totalValueGroceries = totalValueGroceries + transaction.getRealAmount();
			}
		}
		
		Double otherValue = totalValueAll - totalValueCategorized;
		Statistic otherValueStatistic = new Statistic();
		otherValueStatistic.setTitle("Other");
		otherValueStatistic.setValue(otherValue);
		if (cartChart2!=null && otherValue > 0.0){
			cartChart2.add(otherValueStatistic);
		}
		
		Double nonCategorizedValue = totalValueGroceries - totalValueAll;		
		Statistic pendingTicketValueStatistic = new Statistic();
		pendingTicketValueStatistic.setTitle("Pending Ticket");
		pendingTicketValueStatistic.setValue(nonCategorizedValue);
		if (cartChart2!=null && nonCategorizedValue>0.0) {
			cartChart2.add(pendingTicketValueStatistic);
		}
		
		if (cartChart!=null && nonCategorizedValue>0.0) {
			cartChart.add(pendingTicketValueStatistic);
		}
		
		
		Collection<Statistic> pieChart = Statistics.getPieChart(transactions);
		Collection<Statistic> stackedChart = Statistics.getStackedValuesPerDate(transactions);				
		
		model.addAttribute("pieChart",pieChart);
		model.addAttribute("stackedChart",stackedChart);
		model.addAttribute("month",month);
		model.addAttribute("cartChart",cartChart);
		model.addAttribute("cartChart2",cartChart2);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		Date previousMonth = calendar.getTime();
		calendar.add(Calendar.MONTH, 2);
		Date nextMonth = calendar.getTime();
		
		model.addAttribute("previousMonth",dateFormat.format(previousMonth));
		model.addAttribute("nextMonth",dateFormat.format(nextMonth));
		
		return "views/statistics";
	}
	
	@RequestMapping(value={"/transactions/","/transactions"})
	public String transactions(Model model){
		return transactions(null,model);
	}
	
	@RequestMapping(value={"/transactions/{month}"})
	public String transactions(@PathVariable String month, Model model){
		Date date = null;
		if (month!=null) {
			date = getMonthFromString(month);
		} 
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");

		Collection<Payment> transactionsNegative = null;
		Collection<IncomeEntry> transactionsPositive = null;
		
		if (date==null) {
			transactionsNegative = payments.findAll();
			transactionsPositive = incomeEntries.findAll();
		} else {			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			Date from = calendar.getTime();
			calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			Date to = calendar.getTime();
			
			transactionsNegative = payments.findEagerByDateBetween(from, to);
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
		
		model.addAttribute("payments",transactionsNegative);
		model.addAttribute("income",transactionsPositive);
		
		model.addAttribute("month",month);
		if (date!=null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.MONTH, -1);
			Date previousMonth = calendar.getTime();
			calendar.add(Calendar.MONTH, 2);
			Date nextMonth = calendar.getTime();
			
			model.addAttribute("previousMonth",dateFormat.format(previousMonth));
			model.addAttribute("nextMonth",dateFormat.format(nextMonth));
		}
		
		
		model.addAttribute("tags",tagRepository.findAll());
		return "views/transactions";
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
