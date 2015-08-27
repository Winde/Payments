var behaviours = {
		"remove": function(item,data){
			item.remove();
		},
		"remove-parent" :function(item,data){
			item.parent().remove();
		},
		"reload": function(item,data){
			location.reload();
		},
		"prototype": function(item,data) {
			var prototypeTarget = item.attr('data-prototype');
			if (prototypeTarget!=undefined) {
				var prototype = prototypes[prototypeTarget];
				if (prototype!=null && prototype!=undefined){
					prototype(item,data);
				}				
			}
		}		
}

function behaviourSuccess(current, data){
	var behaviour = current.attr('data-behaviour-success');
	behaviourFunction(current,behaviour,data);
}

function behaviourFailure(current, data){	
	var behaviour = current.attr('data-behaviour-failure');
	behaviourFunction(current,behaviour,data);
}

function behaviourError(current, data){
	var behaviour = current.attr('data-behaviour-success');	
	behaviourFunction(current,behaviour,data);
}

function behaviourFunction(current,behaviour,data){
	if (behaviour != null && behaviour !=undefined) {
		var code = 	behaviours[behaviour];
		if (code!=null && code!=undefined){
			code(current,data);
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