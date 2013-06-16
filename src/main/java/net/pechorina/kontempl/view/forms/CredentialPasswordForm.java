package net.pechorina.kontempl.view.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CredentialPasswordForm {

	private Integer id;
	
	@NotNull
	@Size(min = 2)
	private String email;

	@NotNull
	@Size(min = 6)
	private String password;
	private String passwordConfirm;

	public CredentialPasswordForm() {
		super();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
