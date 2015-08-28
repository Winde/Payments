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

function editButtonclick(element){	
	var row = $(element).closest('tr');
	var hidden = row.find('.hide')
	var shown = row.find('.show');
	hidden.removeClass('hide').addClass('show');
	shown.removeClass('show').addClass('hide');
}

function cleanUpCategories(element) {
	var brothers = $(element).closest('tr').find('.categoryName');	
	var selectVal = $(element).find('select option:selected').text();	
	$.each(brothers, function(index,node){
		var target = $(node);
		var nodeText = target.text();
		console.log("*" + nodeText + "* ~ *" + selectVal);
		if (selectVal!=nodeText) {
			if (target.is('.typeText')){
				target.remove();
			} else {
				target.closest('.typeText').remove();
			}
		} 
	});
	editButtonclick(element);
}