package model.dataobjects;

public enum PaymentType {
	Utilities("Utilities"),
    Rent("Rent"),
    Other("Other"),
    Groceries("Groceries"),
    Eating_Out("Eating Out"),
    Internet_Phone("Internet & Phone"),
    Entertainment("Entertainment"),
    Transportation("Transportation");
    
    private String name;
    
    private PaymentType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
    
}
