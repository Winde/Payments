package model.dataobjects.reader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import au.com.bytecode.opencsv.CSVReader;
import model.dataobjects.IncomeEntry;
import model.dataobjects.Payment;
import model.dataobjects.User;
import model.statistics.Movements;

@Component
public class EVOReader implements StatementReader{
	
	public  Movements getMovements(User account, InputStream data) {
		
		Movements movements = new Movements();
		List<Payment> payments = new ArrayList<Payment>();
		List<IncomeEntry> incomeEntries = new ArrayList<IncomeEntry>();
		movements.setIncomeEntries(incomeEntries);
		movements.setPayments(payments);
		
		InputStreamReader inputReader = new InputStreamReader(data); 
		CSVReader reader = new CSVReader(inputReader,';');		
	
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat dateFormatClassic = new SimpleDateFormat("dd/MM/yyyy");
		
		String [] nextLine;
		boolean error = false;
		try {
			int i=0;
			while ((nextLine = reader.readNext())!=null){
				if (i>0) {
					if (nextLine.length>=6) {
						
						String amountString = nextLine[3];
						String comment = nextLine[2];
						String dateString = nextLine[1];
						
						if ((amountString == null  || "".equals(amountString.trim())) && 
								(comment == null  || "".equals(comment.trim())) &&
										(dateString == null  || "".equals(dateString.trim()))
							){
							break;
						}
							
						
						amountString = amountString.replace(".", "");
						amountString = amountString.replace(",", ".");
						Double amount = Double.parseDouble(amountString);
						Date date = null;
						try {
							date = dateFormat.parse(dateString);
						} catch (Exception ex) {}
						if (date==null){
							try {
								date = dateFormatClassic.parse(dateString);
							} catch (Exception ex) {
								ex.printStackTrace();
							}							
						}
						
						Long longAmount = Math.round(new Double(amount*100));						
						
						if (amount>0) {
							IncomeEntry incomeEntry = new IncomeEntry();
							incomeEntry.setAccount(account);
							incomeEntry.setDate(date);
							incomeEntry.setAmount(longAmount);
							incomeEntry.setComments(comment);
							incomeEntries.add(incomeEntry);
							
						} else {
							Payment payment = new Payment();
							longAmount = longAmount * -1;
							payment.setAmount(longAmount);
							payment.setComments(comment);
							payment.setDate(date);			
							payment.setAccount(account);
							payments.add(payment);
						}
					}
					
				}
				i=i+1;					
			}
		} catch (Exception ex){
			ex.printStackTrace();
			error = true;
		} finally {
			try {
				inputReader.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (error) {
			return null;
		} else {
			return movements;
		}
		
		
	}
	
}
