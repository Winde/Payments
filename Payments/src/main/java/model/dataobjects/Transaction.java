package model.dataobjects;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;

@Entity
public class Transaction {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PaymentType type;
	
	@ManyToOne(optional=false)
	private User account;
	
	@Column(nullable = false)
	private Long amount;
	
	@Column(nullable=false)
	private Date date;
	

	protected Transaction() {}

	@PrePersist
	protected void onCreate() {
		date = new Date();
	}

	
    public Transaction(PaymentType type, User account, Long amount) {
        this.type = type;
        this.account = account;
        this.amount = amount;
    }
    
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public PaymentType getType() {
		return type;
	}

	public void setType(PaymentType type) {
		this.type = type;
	}
	
	public Double getRealAmount(){
		if (amount!=null) {
			return amount /100.00;
		}
		return null;
	}
}
