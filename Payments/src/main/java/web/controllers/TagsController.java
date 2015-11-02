package web.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.dataobjects.Payment;
import model.dataobjects.Tag;
import model.persistence.PaymentRepository;
import model.persistence.TagRepository;
import model.persistence.TagRepositoryImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import web.assisting.AjaxSignal;
import configuration.thymeleaf.templating.Layout;

@Controller
@Layout(value = "layouts/default")
public class TagsController {

	@Autowired
	private TagRepository tagRepository;
	
	@Autowired
	private PaymentRepository payments;


	@RequestMapping(value="/tags")
    public String tags(Model model) {
		
		List<Tag> tags = tagRepository.findAllWithUsage();
		model.addAttribute("tags", tags);
        return "views/tags";
    }
	
	@RequestMapping(value="/tag/{name}")
    public String tag(@PathVariable String name, Model model) {
		
		List<Payment> paymentEntries = null;
		Tag tag = null;
		
		
		if (name!=null){
			tag = tagRepository.findOne(name);
		}
		if (tag!=null){
			paymentEntries = payments.findByTag(tag);
		}
		
		model.addAttribute("tag", tag);
		model.addAttribute("payments", paymentEntries);
		model.addAttribute("tags",tagRepository.findAll());
		
        return "views/transactions";
    }
	
	@RequestMapping(value="/tags/{paymentId}")
    public @ResponseBody Payment getTagsForPayment(@PathVariable String paymentId, Model model) {
		
		Long id = null;
		try {
			id = Long.parseLong(paymentId);
		}catch(Exception ex){}
		
		Payment payment = null;
		
		if (id!=null){
			payment = payments.findOne(id);		
		}
		
		if (payment!=null) {
			payment.getTags();
		}
		return payment;				
	}
	
	@RequestMapping(value="/removeTag/{paymentId}/{tagName}")
    public @ResponseBody AjaxSignal removeTagForPayment(@PathVariable String paymentId,@PathVariable String tagName) {

		
		Long id = null;
		try {
			id = Long.parseLong(paymentId);
		}catch(Exception ex){}
		
		Payment payment = null;
		
		if (id!=null){
			payment = payments.findOne(id);
			
			if (payment!=null){
				Tag tag = new Tag();
				tag.setName(tagName);
				int index = -1;
				List<Tag> tags = payment.getTags();
				if (tags!=null && tags.size()>0){
					index = tags.indexOf(tag);
				}
				if (index>=0){
					tag = payment.getTags().get(index);					
					tagRepository.saveAndDecreaseUsage(tag);
					payment.getTags().remove(index);
				} else {
					AjaxSignal signal = new AjaxSignal();
					signal.establishFailure("Payment doesn't have that tag");
					return signal;
				}
				
				payments.save(payment);
				AjaxSignal signal = new AjaxSignal();
				signal.establishSuccess();
				return signal;
			} 		
		} 
		
		AjaxSignal signal = new AjaxSignal();
		signal.establishFailure("Payment does not exist");
		return signal;
		
	}
		
	
	@RequestMapping(value="/addtag/{paymentId}")
    public @ResponseBody AjaxSignal addTagsForPayment(@PathVariable String paymentId,@RequestParam("tags") String tagString) {
		
				
		Long id = null;
		try {
			id = Long.parseLong(paymentId);
		}catch(Exception ex){}
		
		Payment payment = null;
		
		if (id!=null){
			payment = payments.findOne(id);
			
			if (payment!=null){
				
				List<Tag> existingTags = tagRepository.findAll();
				
				List<Tag> tags = Tag.createTags(tagString, existingTags);
				
				List<Tag> paymentTags = payment.getTags();
				
				Iterator<Tag> iterator = tags.iterator();
				while (iterator.hasNext()){
					Tag tag = iterator.next();
					if (paymentTags.contains(tag)) {
						iterator.remove();
					}
				}
				
				if (tags==null || tags.isEmpty()) {
					AjaxSignal signal = new AjaxSignal();
					signal.establishFailure("No tags were added");
					return signal;
				}
				
				tagRepository.saveAndAddUsage(tags);
				
				
				List<Tag> newTags = new ArrayList<>();						
				newTags.addAll(paymentTags);
				newTags.addAll(tags);
				payment.setTags(newTags);				
				payments.save(payment);
				
				AjaxSignal signal = new AjaxSignal();
				signal.establishSuccess();
				signal.getPayload().put("tags", tags);
				signal.getPayload().put("paymentId", id);
				return signal;
			} 		
		} 
		
		AjaxSignal signal = new AjaxSignal();
		signal.establishFailure("Payment does not exist");
		return signal;
	}
	
}
