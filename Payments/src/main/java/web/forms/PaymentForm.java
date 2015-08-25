package web.forms;

import java.util.Calendar;
import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import model.dataobjects.PaymentType;

public class PaymentForm {
		
	private PaymentType type;
	
	@NotNull
	@DecimalMin("0.01")
	private Double amount;
	
	private String comments;
		
	private Boolean income = Boolean.FALSE; 
	
	private String tags;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date = null;
	

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public PaymentType getType() {
		return type;
	}

	public void setType(PaymentType type) {
		this.type = type;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Boolean getIncome() {
		return income;
	}

	public void setIncome(Boolean income) {
		this.income = income;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {	
		Calendar input = Calendar.getInstance();        
		input.setTime(date);
        Calendar today = Calendar.getInstance();        
        if (isSameDay(input, today)){
        	this.date = new Date();
        } else {
        	this.date = date;
        }
	}

	public void clear() {
		this.type = null;
		this.amount = null; 
		this.comments = null;
		this.tags = null;
		this.date = new Date();
	}
	
    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            return false;
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
	
    public Long getAmountLong(){
    	Long amount = null;
		try {
			amount = Math.round(new Double(this.getAmount()*100));			
		}catch (Exception ex){}
    	return amount;
    }

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
	
    
}
