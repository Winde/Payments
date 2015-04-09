package model.dataobjects;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;

@Entity
public class Payment{

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	
	@Column
	@Enumerated(EnumType.STRING)
	private PaymentType type;
	
	@ManyToOne(optional=false)
	private User account;
	
	@Column(nullable = false)
	private Long amount;
	
	@Column(nullable=false)
	private Date date;
	
	@Column
	private String comments;
	
	@Column(nullable=false)
	private String currency = "EUR";
	
	@OneToOne(fetch=FetchType.LAZY)
	private Cart associatedDetails;
	
	@Column
	private Boolean fullyExplained;
	
	
	public Payment() {}

	@PrePersist
	protected void onCreate() {
		if (date==null){
			date = new Date();
		}
	}


	
    public Payment(PaymentType type, User account, Long amount) {
        this.type = type;
        this.account = account;
        this.amount = amount;        
    }
    
	
	public Long getId() {
		return id;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public Cart getAssociatedDetails() {
		return associatedDetails;
	}

	public void setAssociatedDetails(Cart associatedDetails) {
		this.associatedDetails = associatedDetails;
	}

	public Boolean getFullyExplained() {
		return fullyExplained;
	}

	public void setFullyExplained(Boolean fullyExplained) {
		this.fullyExplained = fullyExplained;
	}

	public String toString() {
		return this.getDate() + "\t" + this.getRealAmount();
	}
	
}
