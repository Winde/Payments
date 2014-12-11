package web.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import model.dataobjects.IncomeEntry;
import model.dataobjects.Payment;
import model.dataobjects.Saving;
import model.dataobjects.User;
import model.persistence.PaymentRepository;
import model.persistence.SavingsRepository;
import model.statistics.Statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import configuration.thymeleaf.templating.Layout;
import web.assisting.Statistic;
import web.forms.SavingForm;

@Controller
@Layout(value = "layouts/default")
public class SavingsController {

	@Autowired
	private SavingsRepository savings;
	
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
		
		model.addAttribute("savings",savingResults);
		
        return "views/savings";
    }
}
