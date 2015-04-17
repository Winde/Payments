package web.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.validation.Valid;

import model.dataobjects.CartLineTypeAlias;
import model.persistence.CartLineRepository;
import model.persistence.CartLineTypeRepository;
import model.persistence.CartRepository;
import model.persistence.PaymentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import web.assisting.AjaxSignal;
import web.forms.CategoryForm;
import web.forms.PaymentForm;
import configuration.thymeleaf.templating.Layout;


@Layout(value = "layouts/default")
@Controller
public class CartCategoriesController {

	
	@Autowired
	private CartRepository carts;
	
	@Autowired
	private CartLineRepository cartLines;
	
	@Autowired
	private PaymentRepository payments;
	
	@Autowired
	private CartLineTypeRepository cartCategories;
	
	private void fillupModel(Model model, List<CartLineTypeAlias> categories,List<String> productNames, SortedMap<String,SortedSet<String>> mappings){
		List<CartLineTypeAlias> categoriesCopy = cartCategories.findAll();	
		List<String> productNamesCopy = cartLines.findNames();		
		if (categoriesCopy!=null){		
			categories.addAll(categoriesCopy);
		} 
		if (productNamesCopy!=null) {
			productNames.addAll(productNamesCopy);
		}
		
		Iterator<String> iterator = productNames.iterator();		
		while (iterator.hasNext()){
			String currentProductName = iterator.next();
			for (CartLineTypeAlias category : categories) {
				if (category.getProductName()!=null && category.getProductName().equals(currentProductName)){
					iterator.remove();
					break;
				}
			}
		}
		
			
		for (CartLineTypeAlias category: categories){			
			SortedSet<String> list = mappings.get(category.getAlias());	
			if (list == null){
				list = new TreeSet<>();
				mappings.put(category.getAlias(),list);
			}
			if (category.getProductName()!=null){
				list.add(category.getProductName());
			}
		}
		
		
		SortedSet<String> nonCategorized = new TreeSet<>();
		nonCategorized.addAll(productNames);
		
		
		model.addAttribute("nonCategorized", nonCategorized);
		model.addAttribute("mappings",mappings);
		
	}
	
	@RequestMapping(value="/cartadmin", method=RequestMethod.GET)
	public String cartAdmin(CategoryForm categoryForm,Model model){
		List<CartLineTypeAlias> categories = new ArrayList<>();	
		List<String> productNames = new ArrayList<>();
		SortedMap<String,SortedSet<String>> mappings = new TreeMap<>();
		
		fillupModel(model,categories,productNames,mappings);
				
		return "views/cartadmin";
	}
	
	@RequestMapping(value="/cartadmin", method=RequestMethod.POST)
	public String cartAdminPost(@Valid @ModelAttribute CategoryForm categoryForm, BindingResult result,Model model){	
		List<CartLineTypeAlias> categories = new ArrayList<>();	
		List<String> productNames = new ArrayList<>();
		SortedMap<String,SortedSet<String>> mappings = new TreeMap<>();
		
		fillupModel(model,categories,productNames,mappings);
	
		if (categoryForm.getAlias()!=null){
			categoryForm.setAlias(categoryForm.getAlias().trim());
		}
		
		
		boolean error = false;
		if (result.hasErrors() || (mappings!=null && mappings.get(categoryForm.getAlias())!=null) ){
			error = true;								
		} else {
			CartLineTypeAlias alias = new CartLineTypeAlias();
			alias.setAlias(categoryForm.getAlias());
			alias.setProductName(null);
			cartCategories.save(alias);
			mappings.put(categoryForm.getAlias(), (SortedSet) new TreeSet<>());
			categories.add(alias);
		}
		
		
		
		if (error){
			model.addAttribute("statusCode","error");
		} else {
			model.addAttribute("statusCode","success");
		}
		return "views/cartadmin";
	}
	
	@RequestMapping(value="/setCategoryMap/{category}/{product}")
	@ResponseBody
	public AjaxSignal addProductToCategory(@PathVariable String category,@PathVariable String product,Model model){	
		AjaxSignal signal = new AjaxSignal();
		String error = null;
		
		List<CartLineTypeAlias> categories = cartCategories.findAll();	
		List<String> productNames = cartLines.findNames();	
		
		if (category!=null){
			boolean existsCategory = false;
			boolean alreadyBound = false;
			for (CartLineTypeAlias categoryType: categories) {
				if (category.equals(categoryType.getAlias()) ){
					existsCategory = true;					
				}
				if (product.equals(categoryType.getProductName())){
					alreadyBound = true;
				}
			}
			if (!existsCategory){
				error = "Category doesn't exist";
			}		
			if (alreadyBound) {
				error = "Mapping already in place";
			}
		}
		
		if (productNames==null || !productNames.contains(product)){
			error = "Product doesn't exist";
		}
		
		if (error==null){
			CartLineTypeAlias alias = new CartLineTypeAlias();
			alias.setProductName(product);
			alias.setAlias(category);
			cartCategories.save(alias);
		}
		
		
		if (error!=null){
			signal.establishFailure(error);
		} else {
			signal.establishSuccess();
		}
		
		return signal;
	}
	
	@RequestMapping(value="/removeCategoryMap/{category}/{product}")
	@ResponseBody
	public AjaxSignal removeProductFromCategory(@PathVariable String category,@PathVariable String product,Model model){	
		AjaxSignal signal = new AjaxSignal();
		String error = null;
		
		List<CartLineTypeAlias> categories = cartCategories.findAll();			
		
		CartLineTypeAlias selectedCategory = null;
		
		if (category!=null && product!=null){
			
			for (CartLineTypeAlias categoryType: categories) {
				if (category.equals(categoryType.getAlias()) && product.equals(categoryType.getProductName())){
					selectedCategory = categoryType;
				}
			}
		}
		
		
		if (selectedCategory!=null){
			cartCategories.delete(selectedCategory);
		} else {
			error = "Mapping does not exist";
		}
		
		
		if (error!=null){
			signal.establishFailure(error);
		} else {
			signal.establishSuccess();
		}
		
		return signal;
	}
	
	@RequestMapping(value="/removeCategory/{category}")
	@ResponseBody
	public AjaxSignal removeCategory(@PathVariable String category,Model model){	
		AjaxSignal signal = new AjaxSignal();
		String error = null;
		
		List<CartLineTypeAlias> categories = cartCategories.findAll();			
				
		List<CartLineTypeAlias> toDelete = new ArrayList<>();
		if (category!=null){			
			for (CartLineTypeAlias categoryType: categories) {
				if (category.equals(categoryType.getAlias())){
					toDelete.add(categoryType);										
				}
			}			
		}
		
		
		if (toDelete.size()>0){
			cartCategories.delete(toDelete);
		} else {
			error = "Category does not exist";
		}
		
		
		if (error!=null){
			signal.establishFailure(error);
		} else {
			signal.establishSuccess();
		}
		
		return signal;
	}
}
