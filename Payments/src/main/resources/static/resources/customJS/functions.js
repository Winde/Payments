function prettyAmount(amount){
	if (amount == null || amount == undefined){
		return amount;
	}else {
		return amount.toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
	}
	
}

function parseDateDDMMYYYY(dateCandidate,separator) {
	
	var splitted = dateCandidate.toString().split(separator);
	if (splitted !=null && splitted!=undefined && splitted.length == 3){
		var date = new Date(splitted[2], splitted[1]-1, splitted[0], 0, 0, 0);		
		return date;
	}
}