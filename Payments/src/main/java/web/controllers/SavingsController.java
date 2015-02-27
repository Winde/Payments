package web.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.validation.Valid;

import model.dataobjects.IncomeEntry;
import model.dataobjects.Payment;
import model.dataobjects.Saving;
import model.persistence.IncomeRepository;
import model.persistence.PaymentRepository;
import model.persistence.SavingsRepository;
import model.statistics.Statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import web.assisting.Statistic;
import web.forms.SavingForm;
import configuration.thymeleaf.templating.Layout;

@Controller
@Layout(value = "layouts/default")
public class SavingsController {

	@Autowired
	private SavingsRepository savings;
	
	@Autowired
	private PaymentRepository payments;

	@Autowired
	private IncomeRepository incomeEntries;
	
	@RequestMapping(value="/addsaving", method=RequestMethod.GET)
    public String addSavingGET(@ModelAttribute SavingForm savingForm, Model model) {	
        return "views/addsaving";
    }
	
	@RequestMapping(value="/addsaving", method=RequestMethod.POST)
	 public String addSavingPOST(@Valid @ModelAttribute SavingForm savingForm,BindingResult result, Model model) {	
        
		Long amount = null;
		try {
			amount = new Double(savingForm.getAmount()*100).longValue();
		}catch (Exception ex){}

		if (result.hasErrors() || amount == null){
		} else {
		
			Saving saving = new Saving();
			saving.setAmount(amount);
			savings.save(saving);
		
			savingForm.clear();
			model.addAttribute("statusCode","success");
		}
		
		
		return "views/addsaving";
    }
	
	@RequestMapping(value="/savings")
    public String savings(Model model) {	
		
		List<Saving> savingList = savings.findAll();
		
		Collection<Statistic> savingResults = Statistics.getSavingChart(savingList, null);
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		
		if (savingList!=null && savingList.size()>0){
			Saving saving = savingList.get(savingList.size()-1);
			
			if (!format.format(saving.getDate()).equals(format.format(new Date()))){
				List<IncomeEntry> incomesAfter = incomeEntries.findAfter(saving.getDate());
				
				List<Payment> paymentsAfter = payments.findAfter(saving.getDate());
				
				Double savingsToday = saving.getRealAmount();
				
				for (IncomeEntry income: incomesAfter) {
					savingsToday = savingsToday + income.getRealAmount();
				}
				
				for (Payment payment: paymentsAfter) {
					savingsToday = savingsToday - payment.getRealAmount();
				}

				Statistic todayStatistic = new Statistic();		
				todayStatistic.setTitle(format.format(new Date()));
				todayStatistic.setValue(savingsToday);
				savingResults.add(todayStatistic);
			}
		}
		model.addAttribute("savings",savingResults);
		
        return "views/savings";
    }
	
	
}
