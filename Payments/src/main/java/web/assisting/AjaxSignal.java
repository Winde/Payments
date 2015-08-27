package web.assisting;

import java.util.HashMap;
import java.util.Map;

public class AjaxSignal {

	private String result = null;	
	private String error = null;
	private Map<String,Object> payload = new HashMap<>();
	
	public void establishSuccess(){
		this.result = "success";
	}

	public void establishFailure(){
		this.result = "error";
	}
	
	public void establishFailure(String error){
		this.result = "error";
		this.error = error;
	}

	public String getResult() {
		return result;
	}

	public String getError() {
		return error;
	}

	public Map<String, Object> getPayload() {
		return payload;
	}

	public void setPayload(Map<String, Object> payload) {
		this.payload = payload;
	}
	

}
