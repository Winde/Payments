package model.dataobjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class CartLine implements Comparable<CartLine>{

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	
	@ManyToOne(fetch=FetchType.LAZY)	
	@JoinColumn(name = "cart_id")	
	private Cart cart;

	@Column(nullable = false)
	private Long amount;
	
	@Column(nullable = false)
	private String name;
	
	@Column
	private Long quantity;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Double getRealAmount() {
		if (this.getAmount()==null) {
			return null;
		} else {
			return new Double(this.getAmount()) / 100.0;
		}
	}
	
	public Long getTotal(){
		if (this.getAmount()==null) {
			return null;
		} else {
			Long qty = this.getQuantity();
			if (qty == null) {
				qty = new Long(1);
			}
			
			return this.getAmount() * qty;
		}
	}
	
	public Double getRealTotal() {
		if (this.getAmount()==null) {
			return null;
		} else {
			Long qty = this.getQuantity();
			if (qty == null) {
				qty = new Long(1);
			}
			
			return (new Double(this.getAmount()) / 100.0) * qty;
		}
	}

	@Override
	public int compareTo(CartLine o) {
		if (this.getId()==null) {
			return -1;
		} else {
			return this.getId().compareTo(o.getId());				
		}
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}	
	
	
	
}
