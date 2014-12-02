package model.dataobjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

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
	
	
	protected Transaction() {}

    public Transaction(PaymentType type, User account, Long amount) {
        this.type = type;
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

	public PaymentType getType() {
		return type;
	}

	public void setType(PaymentType type) {
		this.type = type;
	}


}
