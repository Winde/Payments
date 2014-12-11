package web.controllers;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import configuration.thymeleaf.templating.Layout;

@Controller
@Layout(value = "layouts/default")
public class HomeController {

	@RequestMapping(value="/")
	public String home(Model model) {	

		TreeMap<Integer,SortedSet<Date>> years = new TreeMap<>( );

		Calendar now = Calendar.getInstance();
		
		for (int i=0;i<25;i++) {
			SortedSet<Date> set = years.get(now.get(Calendar.YEAR));
			if (set ==null){
				set = new TreeSet<>();
				years.put(now.get(Calendar.YEAR),set);
			}
			set.add(now.getTime());
			
			
			now.add(Calendar.MONTH,-1);
					
		}

		model.addAttribute("years", years.descendingMap());

		return "views/home";
	}

}
