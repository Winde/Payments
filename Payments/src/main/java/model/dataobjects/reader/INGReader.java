package model.dataobjects.reader;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import model.dataobjects.IncomeEntry;
import model.dataobjects.Payment;
import model.dataobjects.User;
import model.statistics.Movements;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

@Component
public class INGReader implements StatementReader{

	private final int headerLines = 4;
	
	@Override
	public Movements getMovements(User account, InputStream data) {
		Movements movements = new Movements();
		List<Payment> payments = new ArrayList<Payment>();
		List<IncomeEntry> incomeEntries = new ArrayList<IncomeEntry>();
		movements.setIncomeEntries(incomeEntries);
		movements.setPayments(payments);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		boolean error = false;
		try {
			Workbook wBook = WorkbookFactory.create(data);
			
			Sheet sheet = wBook.getSheetAt(0);
			Row row = null;
			Cell cell = null;
			Iterator<Row> rowIterator = sheet.iterator();
			
			int i=1;
			while (rowIterator.hasNext()){
				row = rowIterator.next();				
				if (i>headerLines){
					
					Cell cell0 =  row.getCell(0);
					Cell cell1 = row.getCell(1);
					Cell cell2 = row.getCell(2);
					
					if ((cell0 == null || cell0.getCellType() == Cell.CELL_TYPE_BLANK) &&
							(cell1==null || cell1.getCellType() == Cell.CELL_TYPE_BLANK) &&
									(cell2==null || cell2.getCellType() == Cell.CELL_TYPE_BLANK)){
						break;
					}
							
					Double amount = null;
					Long longAmount = null;
					Date date = null;
					String comment = null;
					
					cell = cell2; //Amount
					
					if (cell!=null){
						switch (cell.getCellType()){
							case Cell.CELL_TYPE_NUMERIC:
								amount = new Double(cell.getNumericCellValue());		
								break;
							case Cell.CELL_TYPE_STRING:
								String stringValue = cell.getStringCellValue(); 
								if (stringValue!=null){
									stringValue = stringValue.replace(".", "");
									stringValue = stringValue.replace(",", ".");
									
									amount = Double.parseDouble(stringValue);
								}
								break;
						}
						
						if (amount!=null){
							longAmount = Math.round(new Double(amount*100));							
						}
					}
					
					cell = cell0; //Date
					if (cell!=null){
						switch (cell.getCellType()){
							case Cell.CELL_TYPE_NUMERIC:
								date = cell.getDateCellValue();		
								break;
							case Cell.CELL_TYPE_STRING:
								String stringValue = cell.getStringCellValue(); 
								date = dateFormat.parse(stringValue);
								break;							
						}
					}
					
					cell = cell1; //Description
					if (cell!=null){
						comment = cell.getStringCellValue(); 
					}
					
					
					if (date==null){						
						System.out.println("Row: " + row.getRowNum());
						System.out.println("Date is null");
						error = true;
					}else if (amount==null){				
						System.out.println("Row: " + row.getRowNum());
						System.out.println("Amount is null");
						error = true;
					} else {
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
			
		} catch (Exception ex) {
			error = true;
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		if (error) {
			return null;
		} else {
			return movements;
		}
	}

}
