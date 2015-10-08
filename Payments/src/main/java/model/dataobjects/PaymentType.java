package model.dataobjects;

public enum PaymentType implements Comparable<PaymentType>{
	ATMCheckout("ATM Checkout"),
	Coffee("Coffee"),
	Clothes("Clothes"),
	DrugStore("Drugstore"),
	Eating_Out("Eating Out"),
	Electricity("Electricity"),
	Entertainment("Entertainment"),
	Furniture("Furniture"),
	Gas("Gas"),
	Groceries("Groceries"),
	Internet_Phone("Internet & Phone"),
	Other("Other"),			
    Rent("Rent"),        
    Transportation("Transportation"),
    Trip("Trip"),
    Utilities("Utilities"),
    Water("Water");
    
    

    private String name;
    
    private PaymentType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
