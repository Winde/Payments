package model.dataobjects;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

@Entity
public class IncomeEntry{

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	
	@ManyToOne(optional=false)
	private User account;
	
	@Column(nullable = false)
	private Long amount;
	
	@Column(nullable=false)
	private Date date;
	
	@Column(nullable=false)
	private String currency = "EUR";

	
	protected IncomeEntry() {}

	@PrePersist
	protected void onCreate() {
		date = new Date();
	}

	
    public IncomeEntry(User account, Long amount) {
        this.account = account;
        this.amount = amount;        
    }
	
	public User getAccount() {
		return account;
	}

	public void setAccount(User account) {
		this.account = account;
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

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public Double getRealAmount(){
		if (amount!=null) {
			return amount /100.00;
		}
		return null;
	}
}
