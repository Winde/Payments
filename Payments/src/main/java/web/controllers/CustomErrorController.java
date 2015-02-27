package web.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import configuration.thymeleaf.templating.Layout;

@Controller
@Layout(value = "layouts/default")
public class CustomErrorController implements ErrorController{

	private static final String PATH = "/error";

	
	//@ResponseStatus(value = HttpStatus.NOT_FOUND)
	//@ExceptionHandler(Exception.class)
	@RequestMapping(PATH)
	public String error(HttpServletRequest request){
		return "views/error";
	}
	
	 @Override
	 public String getErrorPath() {
	        return PATH;
	 }
	
}
