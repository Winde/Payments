package web.assisting;

public class Statistic {

	private String title;
	private Double value;
	
	public Statistic(){}
	
	public Statistic(String title, Long value) {
		this.title = title;
		this.value = new Double(value) / 100.0;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	
	public String toString(){
		return "{title:'"+title+"',value:'"+value+"'}";
	}
	
	
}