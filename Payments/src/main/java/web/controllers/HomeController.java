package web.controllers;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@RequestMapping(value="/")
    public String home(Model model) {	
		
		TreeSet<Date> months = new TreeSet<>( );
		
		Calendar now = Calendar.getInstance();
		
		months.add(now.getTime());
		
		for (int i=0;i<24;i++) {
			now.add(Calendar.MONTH,-1);
			months.add(now.getTime());			
		}
				
		model.addAttribute("months", months.descendingSet());
		
        return "home";
    }
	
}
