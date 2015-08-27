package model.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	
	public static Date getMonthFromString(String dateString){
		SimpleDateFormat format = new SimpleDateFormat("MM-yyyy");
		Date date = null;
		try {
			date = format.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			
		}
		return date;
		
	}
	
}
