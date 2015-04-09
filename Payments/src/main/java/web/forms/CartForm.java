package web.forms;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class CartForm {


	@NotNull
	private Double amount;
	
	@NotNull
	@NotBlank
	private String name;	
	
	@NotNull
	private Long quantity;
	
	@NotNull
	private String shop;
	
	public CartForm(){
		quantity = new Long(1);
	}
	
	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	public void clear(){
		this.amount = null;
		this.name=null;
		this.quantity=new Long(1);
	}
}
