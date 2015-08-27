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
	$(html).insertBefore(element);	
}