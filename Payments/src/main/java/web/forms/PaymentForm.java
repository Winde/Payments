package web.forms;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import model.dataobjects.PaymentType;

public class PaymentForm {

	@NotNull
	@NotBlank
	private String name;	
	private PaymentType type;
	
	@NotNull
	@DecimalMin("0.01")
	private Double amount;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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


	
	
	
}
