package web.forms;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import model.dataobjects.PaymentType;

public class PaymentForm {
	
	private PaymentType type;
	
	@NotNull
	@DecimalMin("0.01")
	private Double amount;
	
	private String comments;
		
	private Boolean income = Boolean.FALSE; 
	
	
	
	
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public PaymentType getType() {
		return type;
	}

	public void setType(PaymentType type) {
		this.type = type;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Boolean getIncome() {
		return income;
	}

	public void setIncome(Boolean income) {
		this.income = income;
	}


	public void clear() {
		this.type = null;
		this.amount = null; 
	}
	
	
	
}
