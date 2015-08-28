var prototypes = {};

prototypes["tags"] = function(element,data,baseUrl){
		
	var paymentId = null;
	if (data!=null && data!=undefined && data.payload!=null && data.payload!=undefined && data.payload.tags!=null && data.payload.paymentId!=undefined) {
		paymentId = data.payload.paymentId;
	}
	
	var tags = null;
	if (data!=null && data!=undefined && data.payload!=null && data.payload!=undefined && data.payload.tags!=null && data.payload.tags!=undefined) {
		tags = data.payload.tags;
	}
	
	if (baseUrl== undefined || baseUrl == null) {
		baseUrl = "";
	}
	var html = "";
	if (paymentId!=null && tags!=null) {
		for (var index = 0; index < tags.length; ++index) {
			var tag = tags[index].name;
			if (tag!=null && tag!=undefined){
				var tempHtml = ''+ 
						'<a class="btn btn-default ajaxButton" data-behaviour-success="remove" data-behaviour-failure="message" href="'+baseUrl+'/removeTag/'+paymentId+'/'+tag+'">'+
							'<span class="tag">'+tag+'</span>'+										
							'<span class="glyphicon glyphicon-remove red-colored"></span>'+											
						'</a>';
			}
			html = html + tempHtml;
		}
	}
	
	findExecuteInsertFunction(html,element)
};

prototypes["text-category"] = function(element,data,baseUrl){
	
	var category = null;
	if (data!=null && data!=undefined && data.payload!=null && data.payload!=undefined && data.payload.category!=null && data.payload.category!=undefined) {
		category = data.payload.category;
	}
	if (baseUrl== undefined || baseUrl == null) {
		baseUrl = "";
	}
	var html = "";
	if (category!=null) {
		html = '<span class="categoryName typeText hide">'+category+'</span>';
	}

	findExecuteInsertFunction(html,element);
}

prototypes["link-category"] = function(element,data,baseUrl){
	
	var category = null;
	if (data!=null && data!=undefined && data.payload!=null && data.payload!=undefined && data.payload.category!=null && data.payload.category!=undefined) {
		category = data.payload.category;
	}
	var payment= null;
	if (data!=null && data!=undefined && data.payload!=null && data.payload!=undefined && data.payload.payment!=null && data.payload.payment!=undefined) {
		payment = data.payload.payment;
	}
	if (baseUrl== undefined || baseUrl == null) {
		baseUrl = "";
	}
	var html = "";
	if (category!=null && payment!=null) {
		console.log(payment.fullyExplained);
		if (category == "Groceries"){
			html = ''+
				'<a class="typeText hide" href="'+baseUrl+'/cart/'+payment.id+'">'+
					'<span class="categoryName">'+category+'</span>'+					
					'<span class="glyphicon glyphicon glyphicon-pencil '+(payment.fullyExplained ? 'green-colored' : 'red-colored')+'"></span>'											
				'</a>';
		} else if(category == "ATM Checkout" && payment.amount!=undefined && payment.amount>0){
			html = ''+
				'<a class="typeText hide" href="'+baseUrl+'/impute/'+payment.id+'">'+
					'<span class="categoryName">'+category+'</span>'+											
					'<span class="glyphicon glyphicon glyphicon-pencil red-colored"></span>'+											
				'</a>';
		} else {
			html = '<span class="categoryName typeText hide">'+category+'</span>';
		}
	}

	findExecuteInsertFunction(html,element);
}

function findExecuteInsertFunction(html,element){
	var insertStrategy = element.attr('data-prototype-insert');
	if (insertStrategy!=null && insertStrategy!=undefined) {
		var insertFunction = prototypesInsert[insertStrategy];
		if (insertFunction!=null && insertFunction!=undefined) {
			insertFunction(html,element);
		}
	}	
}

var prototypesInsert = {};

prototypesInsert["replace"] = function(html, element){
	$(element).replaceWith(html);
};


prototypesInsert["insert-hide"] = function(html, element){
	$(html).insertBefore(element);
	$(element).hide();	
};

prototypesInsert["insertBefore"] = function(html, element){
	$(html).insertBefore(element);	
};
