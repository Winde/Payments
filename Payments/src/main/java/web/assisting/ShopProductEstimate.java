package web.assisting;

public class ShopProductEstimate {

	private String shop;
	private String product;
	private Double price;
	
	public ShopProductEstimate(){}
	
	public ShopProductEstimate(String brand, String product, Double price){
		this.shop = brand;
		this.product = product;
		this.price = price;		
	}

	public String getShop() {
		return shop;
	}

	public void setShop(String shop) {
		this.shop = shop;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	public Double getRealPrice(){
		if (this.getPrice()==null) {
			return null;
		} else {
			return this.getPrice() / 100.0;
		}
	}
	
	public String toString(){		
		return product + " - " + shop + " - " + this.getRealPrice() + "€";		
	}
	
	
}
