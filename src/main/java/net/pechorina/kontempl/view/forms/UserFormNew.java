package net.pechorina.kontempl.view.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserFormNew {

	@NotNull
	@Size(min = 2)
	private String name;

	@NotNull
	@Size(min = 2)
	private String email;
	
	@NotNull
	private Boolean locked;

	@NotNull
	private Boolean active;

	@NotNull
	@Size(min = 6)
	private String password;
	private String passwordConfirm;
	private String role;

	public UserFormNew() {
		super();
		this.active = true;
		this.locked = false;
		this.role = "editor";
	}

	public UserFormNew(String name) {
		super();
		this.active = true;
		this.locked = false;
		this.name = name;
		this.role = "editor";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
