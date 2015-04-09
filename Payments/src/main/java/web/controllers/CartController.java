package web.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import model.dataobjects.Cart;
import model.dataobjects.CartLine;
import model.dataobjects.Payment;
import model.persistence.CartLineRepository;
import model.persistence.CartRepository;
import model.persistence.PaymentRepository;
import model.persistence.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import web.forms.CartForm;
import configuration.thymeleaf.templating.Layout;

@Layout(value = "layouts/default")
@Controller
public class CartController {

	@Autowired
	private CartRepository carts;
	
	@Autowired
	private CartLineRepository cartLines;
	
	@Autowired
	private PaymentRepository payments;
	
	
	private Cart checkAndCreateCart(Payment payment){
		if (payment!=null){
			Cart cart = new Cart();
			cart.setId(payment.getId());
			cart.setPayment(payment);
			carts.save(cart);
			payment.setAssociatedDetails(cart);
			payments.save(payment);			
			return cart;
		}else {
			return null;
		}
	}
	
	@RequestMapping(value="/cart/{id}", method=RequestMethod.GET)
    public String paymentGET(@ModelAttribute CartForm cartForm,@PathVariable Long id, Model model) {
		
		Cart cart = carts.findOne(id);
		Payment payment = payments.findOne(id);
		if (cart ==null){			
			cart = checkAndCreateCart(payment);			
		}
		
		if (cart!=null){
			model.addAttribute("cart",cart);
			cartForm.setShop(cart.getShop());
		} else {
			model.addAttribute("statusCode","error");
		}
		
		model.addAttribute("id",id);
		if (payment!=null) {
			model.addAttribute("payment",payment);
		}
		
		List<String> values = cartLines.findNames();		
		model.addAttribute("nameValues",values);
		
		List<String> shops = carts.findShops();
		model.addAttribute("shops",shops);				
		
        return "views/cart";
    }
	
	@RequestMapping(value="/cart/{id}", method=RequestMethod.POST)
    public String paymentPOST(@Valid @ModelAttribute CartForm cartForm,@PathVariable Long id, Model model, BindingResult result) {
		
		Cart cart = carts.findOne(id);
		Payment payment = payments.findOne(id);
		if (cart ==null){			
			cart = checkAndCreateCart(payment);			
		} 
		
		if (cart!=null){
			model.addAttribute("cart",cart);		
		} 
		
		if (cart!=null && !result.hasErrors()){
			
			CartLine line = new CartLine();
			Long longAmount = Math.round(new Double(cartForm.getAmount()*100));
			//Long longAmount = new Double(cartForm.getAmount() * 100.0).longValue();
			line.setAmount(longAmount);
			line.setQuantity(cartForm.getQuantity());
			line.setName(cartForm.getName());
			line.setCart(cart);			
			cartLines.save(line);
			cart.addLine(line);
			cart.setShop(cartForm.getShop());
			carts.save(cart);
			
			if (payment.getAmount()!=null && payment.getAmount().equals(cart.getTotalValue())){
				payment.setFullyExplained(Boolean.TRUE);
			} else {
				payment.setFullyExplained(Boolean.FALSE);
			}
			
			payments.save(payment);
			
		} else {
			model.addAttribute("statusCode","error");			
		}

		model.addAttribute("id",id);		
		if (payment!=null) {
			model.addAttribute("payment",payment);
		}
		
		cartForm.clear();
		
		List<String> values = cartLines.findNames();		
		model.addAttribute("nameValues",values);
		
		List<String> shops = carts.findShops();
		model.addAttribute("shops",shops);
		
        return "views/cart";
    }
	
	@RequestMapping(value="/deleteCartLine/{cartId}/{lineId}")
    public String deleteCartLine(@PathVariable Long cartId,@PathVariable Long lineId, Model model) {
		
		Payment payment = payments.findOne(cartId);
		Cart cart = carts.findOne(cartId);
		CartLine cartLine = cartLines.findOne(lineId);
	
		if (payment!=null && cartLine.getCart()!=null && cartLine.getCart().equals(cart)){
			cart.getLines().remove(cartLine);			
			cartLines.delete(cartLine);
			
			if (payment.getAmount()!=null && payment.getAmount().equals(cart.getTotalValue())){
				payment.setFullyExplained(Boolean.TRUE);
			} else {
				payment.setFullyExplained(Boolean.FALSE);
			}
			payments.save(payment);
			
			return "redirect:/cart/" + cartId;
		} else {
			return "redirect:/error" ;
		}
		
		
		
		
	}
	
	
}
