package net.pechorina.kontempl.view.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import net.pechorina.kontempl.data.User;

public class UserForm {

	@NotNull
	@Size(min = 2)
	private String name;
	
	@NotNull
	private Boolean locked;

	@NotNull
	private Boolean active;

	public UserForm() {
		super();
		this.active = true;
		this.locked = false;
	}

	public UserForm(String name) {
		super();
		this.active = true;
		this.locked = false;
		this.name = name;
	}
	
	public UserForm(User u) {
		super();
		this.active = u.isActive();
		this.locked = u.isLocked();
		this.name = u.getName();
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserForm [name=");
		builder.append(name);
		builder.append(", locked=");
		builder.append(locked);
		builder.append(", active=");
		builder.append(active);
		builder.append("]");
		return builder.toString();
	}

}
