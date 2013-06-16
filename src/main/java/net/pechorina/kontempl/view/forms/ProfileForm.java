package net.pechorina.kontempl.view.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProfileForm {
	
	@NotNull
	@Size(min = 2)
	private String name;
	
	@NotNull
	@Size(min = 2)
	private String email;

	@NotNull
	@Size(min = 6)
	private String password;
	private String passwordConfirm;

	public ProfileForm() {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
