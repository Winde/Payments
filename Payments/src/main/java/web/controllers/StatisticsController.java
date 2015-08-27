package web.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import model.dataobjects.IncomeEntry;
import model.dataobjects.Payment;
import model.dataobjects.PaymentType;
import model.dataobjects.Tag;
import model.persistence.CartLineRepository;
import model.persistence.IncomeRepository;
import model.persistence.PaymentRepository;
import model.persistence.TagRepository;
import model.statistics.Statistics;
import model.util.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import web.assisting.Statistic;
import configuration.thymeleaf.templating.Layout;

@Controller
@Layout(value = "layouts/default")
public class StatisticsController {

	@Autowired
	private PaymentRepository payments;

	@Autowired
	private IncomeRepository incomeEntries;
	
	@Autowired
	private CartLineRepository cartLines;
	
	@Autowired
	private TagRepository tagRepository;
	
	@RequestMapping(value="/statisticsForTag/{tagName}")
	public String statisticsForTag(@PathVariable String tagName, Model model){
				
			Tag tag = new Tag();
			tag.setName(tagName);
		
			List<Payment> transactions = null;			
			transactions = payments.findByTag(tag);			
					
			//Collection<Statistic> pieChart = Statistics.getPieChart(transactions);
			Collection<Statistic> barChart = Statistics.getValuesPerMonth(transactions);
			Collection<Statistic> barChart2 = null;
			Collection<Statistic> stackedChart = Statistics.getStackedValuesPerDate(transactions);				
			
			
			//Map<String, Collection<Statistic>> multipleChart = Statistics.getValuesPerTypePerMonth(transactions);					
			
			List<Statistic> cartChart = null; //cartLines.findStatistic(); //TODO REDO
			List<Statistic> cartChart2 = null; //cartLines.findStatisticByCategory(); //TODO REDO
						
			model.addAttribute("tag",tag);
			//model.addAttribute("pieChart",pieChart);
			model.addAttribute("stackedChart",stackedChart);
			model.addAttribute("barChart",barChart);
			model.addAttribute("barChart2",barChart2);
			//model.addAttribute("multipleChart",multipleChart);
			model.addAttribute("cartChart",cartChart);
			model.addAttribute("cartChart2",cartChart2);
			model.addAttribute("stackedChart",stackedChart);
			
			
			return "views/statistics";
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
			date = Utils.getMonthFromString(month);
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
	
	
}
