var behaviours = {
		"remove": function(item){
			item.remove();
		},
		"remove-parent" :function(item){
			item.parent().remove();
		},
		"reload": function(item){
			location.reload();
		} 		
}

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

function behaviourSuccess(current, data){
	var behaviour = current.attr('data-behaviour-success');
	behaviourFunction(current,behaviour);
}

function behaviourFailure(current, data){	
	var behaviour = current.attr('data-behaviour-failure');
	behaviourFunction(current,behaviour);
}

function behaviourError(current, data){
	var behaviour = current.attr('data-behaviour-success');	
	behaviourFunction(current,behaviour);
}

function behaviourFunction(current,behaviour){
	if (behaviour != null && behaviour !=undefined) {
		var code = 	behaviours[behaviour];
		if (code!=null && code!=undefined){
			code(current);
		}
	}
}

function ajaxSuccess(current, data){
	if (data!=null && data!=undefined && data.result!=undefined){
		
		if (data.result == "success"){						
			behaviourSuccess(current, data);			
		}
		
		if (data.result == "error"){
			behaviourFailure(current, data);
		}
	}
}

function ajaxError(current, data){
	behaviourError(current, data);
}

$(document).ready(function(){
	
	$(document).on('click','.ajaxButton', function(event){
		event.preventDefault();
		event.stopPropagation();
		var current = $(this);
		
		var href = current.attr('href');
		if (href!=null && href!=undefined){
			$.ajax({
				url: href,
				type: "GET",
				cache: false,
				success: function(data){
					ajaxSuccess(current, data);
				},
				error: function(data){
					ajaxError(current, data);
				}
			});
		}
		
		return false;
	});
	
	$(document).on('submit','.ajaxForm', function(event){
		event.preventDefault();
		event.stopPropagation();
		var current = $(this);
		
		var action = current.attr('action');
		if (action!=null && action!=undefined){
			$.ajax({
				url: action,
				type: "POST",
				cache: false,
				data: current.serialize(),
				success: function(data){
					ajaxSuccess(current, data);
				},
				error: function(data){
					ajaxError(current, data);
				}
			
			});
		}	
	});
})