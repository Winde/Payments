function prettyAmount(amount){
	if (amount == null || amount == undefined){
		return amount;
	}else {
		return amount.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
	}
	
}