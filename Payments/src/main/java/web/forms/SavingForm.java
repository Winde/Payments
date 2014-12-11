package web.forms;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class SavingForm {


	@NotNull
	@DecimalMin("0.01")
	private Double amount;

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public void clear() {
		this.amount = null;
	}
	
}
