package model.statistics;

import java.util.List;

import model.dataobjects.IncomeEntry;
import model.dataobjects.Payment;

public class Movements {

	private List<Payment> payments;
	private List<IncomeEntry> incomeEntries;
	public List<Payment> getPayments() {
		return payments;
	}
	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}
	public List<IncomeEntry> getIncomeEntries() {
		return incomeEntries;
	}
	public void setIncomeEntries(List<IncomeEntry> incomeEntries) {
		this.incomeEntries = incomeEntries;
	}
	
	
	
}
