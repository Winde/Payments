package web.forms;

import javax.validation.constraints.NotNull;

public class ImputeForm {

	@NotNull	
	private Double amount;

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	
	
}
