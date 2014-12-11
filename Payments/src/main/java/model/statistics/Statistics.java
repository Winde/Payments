package model.statistics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import model.dataobjects.IncomeEntry;
import model.dataobjects.PaymentType;
import model.dataobjects.Payment;
import model.dataobjects.Saving;
import web.assisting.Statistic;

public class Statistics {

	public static Collection<Statistic> getPieChart(Collection<Payment> transactions) {

		Map<PaymentType,Statistic> statisticsPerType = new TreeMap<>();

		for (Payment transaction: transactions) {
			Date date = transaction.getDate();
			PaymentType type = transaction.getType();
			if (date!=null && type!=null){

				Statistic statistic = statisticsPerType.get(type);

				if (statistic ==null) {
					statistic = new Statistic();
					statistic.setTitle(type.getName());
					statistic.setValue(0.0);
				}
				Double amountPerType = statistic.getValue();
				statistic.setValue(amountPerType + transaction.getRealAmount());
				statisticsPerType.put(type, statistic);

			}						
		}

		return statisticsPerType.values();
	}

	public static Collection<Statistic> getStackedValuesPerDate(Collection<Payment> transactions) {

		SortedMap<String,Statistic> result = new TreeMap<>();

		SimpleDateFormat formatKey = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatTitle = new SimpleDateFormat("dd/MM/yyyy");

		for (Payment transaction: transactions) {

			String key = null;
			try {				
				for (String string: result.keySet()){
					
					Date date = null;
					date = formatKey.parse(string);				
					if (date.before(transaction.getDate())){
						if (key==null || date.after(formatKey.parse(key))){
							key = string;
						}
					}
				}				
			} catch (ParseException e) {}		
			
			Statistic statistic = result.get(formatKey.format(transaction.getDate()));

			if (statistic !=null){
				statistic.setValue(statistic.getValue() + transaction.getRealAmount());
			} else {

				statistic = new Statistic();
				statistic.setTitle(formatTitle.format(transaction.getDate()));
				statistic.setValue(0.0);

				if (key==null){				
					statistic.setValue(statistic.getValue() + transaction.getRealAmount());							
				} else {				
					statistic.setValue(statistic.getValue() + transaction.getRealAmount()+result.get(key).getValue());
				}
			}
			result.put(formatKey.format(transaction.getDate()), statistic);
		}
		//TODO check ordering
		return result.values();
	}

	public static Map<String,Map<String,Double>>  getTablePayments(Collection<Payment> transactions) {

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		Map<String,Map<String,Double>> statisticsFull = new TreeMap<>();

		for (Payment transaction: transactions) {
			Date date = transaction.getDate();
			PaymentType type = transaction.getType();
			if (date!=null && type!=null){

				String key = format.format(date);
				Map<String, Double> mapForType = statisticsFull.get(type.getName());
				if (mapForType==null) {
					mapForType = new TreeMap<>();					
					statisticsFull.put(type.getName(),mapForType);
				}
				Double amount = mapForType.get(key);
				if (amount ==null) { amount = new Double(0.0); }
				amount = amount + transaction.getRealAmount();								
				mapForType.put(key, amount);				
				for (String iteration: statisticsFull.keySet() ){
					Double amountForCombination = statisticsFull.get(iteration).get(key);
					if (amountForCombination==null) {
						statisticsFull.get(iteration).put(key,new Double(0.0));
					}					
				}

			}						
		}

		return statisticsFull;
	}

	public static Map<String,Double> getTableIncome(Collection<IncomeEntry> transactions){

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		Map<String,Double> result =new TreeMap<>();

		for (IncomeEntry transaction: transactions) {
			Date date = transaction.getDate();
			if (date!=null){

				String key = format.format(date);

				Double amount = result.get(key);
				if (amount ==null) { amount = new Double(0.0); }
				amount = amount + transaction.getRealAmount();								
				result.put(key, amount);						
			}						
		}
		return result;

	}

	public static Collection<Statistic> getSavingChart(Collection<Saving> savings, Collection<Payment> payment){
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

		List<Statistic> savingResults = new ArrayList<>();

		for (Saving saving : savings) {
			Statistic statistic = new Statistic();
			statistic.setTitle(format.format(saving.getDate()));
			statistic.setValue(saving.getRealAmount());
			savingResults.add(statistic);
		}

		return savingResults;
	}

}
