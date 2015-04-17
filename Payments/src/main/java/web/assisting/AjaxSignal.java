package web.assisting;

public class AjaxSignal {

	private String result = null;
	
	private String error = null;
	
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
	
	
	
	
}
