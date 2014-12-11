package model.dataobjects;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;

@Entity
public class Saving {


	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	
	@Column(nullable=false)
	private Date date;
	
	@Column(nullable = false)
	private Long amount;

	@PrePersist
	protected void onCreate() {
		date = new Date();
	}
	
	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}
		

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	
	public Double getRealAmount(){
		if (amount!=null) {
			return amount /100.00;
		}
		return null;
	}
}
