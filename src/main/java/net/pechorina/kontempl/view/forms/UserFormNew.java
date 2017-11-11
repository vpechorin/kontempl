package net.pechorina.kontempl.view.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class UserFormNew {

	@NotNull
	@Size(min = 2)
	private String name;

	@NotNull
	@Size(min = 2)
	private String email;

	@NotNull
	@Size(min = 7)
	private String password;

	private Set<String> roles = new HashSet<>();

	public UserFormNew() {
		super();
	}

	public UserFormNew(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
        return "UserFormNew [name=" + name + ", email=" + email + ", password=" + password + ", roles=" + roles + "]";
	}

}
