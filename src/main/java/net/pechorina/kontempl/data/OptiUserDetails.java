package net.pechorina.kontempl.data;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class OptiUserDetails implements Serializable, UserDetails {

	private User user;

	private static final long serialVersionUID = 1L;
	Set<GrantedAuthority> authorities;

	public OptiUserDetails() {
	}

	public OptiUserDetails(User u) {
		this.user = u;
	}

	public OptiUserDetails(User u, Set<String> roles) {
		this.user = u;
		this.authorities = new HashSet<>();
        authorities.addAll(roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_"
                + role.toLowerCase())).collect(Collectors.toList()));
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setAuthorities(Set<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		boolean nonLocked = true;
		if (user.isLocked())
			nonLocked = false;
		return nonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
        return "OptiUserDetails [user=" + user + ", authorities=" + authorities + "]";
	}

}
