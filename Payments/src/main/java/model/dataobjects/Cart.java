package model.dataobjects;

import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;



@Entity
public class Cart {

	
	
	@Id    
    private Long id;
	
	@ManyToOne(optional=false)
	private Payment payment;
	
	//@OneToMany(fetch=FetchType.LAZY,cascade=CascadeType.ALL,mappedBy="Cart")
	@OneToMany(mappedBy="cart", cascade=CascadeType.ALL)
	@OrderBy("id ASC")
	private SortedSet<CartLine> lines;

	@Column
	private String shop;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public Set<CartLine> getLines() {
		return lines;
	}

	public void setLines(SortedSet<CartLine> lines) {
		this.lines = lines;
	}

	public void addLine(CartLine line) {
		if (lines == null){ 
			lines = new TreeSet<>();
		}
		lines.add(line);		
	}
	
	public long getTotalValue(){
		if (lines == null || lines.size()<=0) {
			return 0;
		} else {
			long value = 0;
			Iterator<CartLine> iterator = lines.iterator();
			while (iterator.hasNext()){
				CartLine line = iterator.next();
				value = value + line.getTotal();
			}
			return value;
		}
	}
	
	public Double getRealValue(){
		return new Double(this.getTotalValue()) / 100.0; 
	}

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}
	
	
	
}
