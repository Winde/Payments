package web.forms;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class UserForm {

	@NotNull
	private String usernameText;
	
	@NotBlank
	private String passwordText;

	
	public String getUsernameText() {
		return usernameText;
	}

	public void setUsernameText(String usernameText) {
		this.usernameText = usernameText;
	}

	public String getPasswordText() {
		return passwordText;
	}


	public void setPasswordText(String passwordText) {
		this.passwordText = passwordText;
	}





	public void clear(){
		this.usernameText = null;
		this.passwordText = null;
	
	}
}
