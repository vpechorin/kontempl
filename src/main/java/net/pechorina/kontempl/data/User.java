package net.pechorina.kontempl.data;

import static javax.persistence.CascadeType.ALL;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Joiner;

/**
 * Entity implementation class for Entity: User
 * 
 */
@Entity
@Table(name = "user", indexes={
		@Index(name="lockedIdx", columnList="locked"),
		@Index(name="activeIdx", columnList="active")
})
public class User implements Serializable {
	
	private static final DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyy-MM-dd");
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	@Size(min = 2)
	private String name;
	
	@NotNull
	private boolean locked;
	
	@NotNull
	private boolean active;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "userrole", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
	private Set<Role> roles;
	
	@JsonIgnore
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime created;
	
	@OneToMany(mappedBy="user", fetch = FetchType.EAGER, cascade = ALL)
	@JsonIgnore
	private Set<Credential> credentials;
	
	@OneToMany(mappedBy="user", fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<AuthToken> authTokens;
	
	private static final long serialVersionUID = 1L;

	public User() {
		super();
		this.active = true;
		this.locked = false;
		this.created = new DateTime();
	}

	public User(String name) {
		super();
		this.created = new DateTime();		
		this.active = true;
		this.locked = false;
		this.name = name;
	}
	
	@Transient
	@JsonProperty("userroles")
	public String getRoleNames() {
		 Set<Role> roles = this.getRoles();
		 int rolesNum = roles.size();
		 List<String> l = new ArrayList<String>(rolesNum);
		 for(Role r: roles) {
			 l.add(r.getName());
		 }
		 
		 return Joiner.on(", ").join(l) ;
	}
	
	@Transient
	@JsonIgnore
	public Set<String> getRoleNamesSet() {
		 Set<Role> roles = this.getRoles();
		 int rolesNum = roles.size();
		 Set<String> s = new HashSet<String>(rolesNum);
		 for(Role r: roles) {
			 s.add(r.getName());
		 }
		 return s;
	}
	
	@Transient
	@JsonProperty("createdDate")
	public String getCreatedDate() {
		return this.getCreated().toString(dateFmt);
	}	
	
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public DateTime getCreated() {
		return created;
	}

	public void setCreated(DateTime created) {
		this.created = created;
	}

	public Set<Credential> getCredentials() {
		return credentials;
	}

	public void setCredentials(Set<Credential> credentials) {
		this.credentials = credentials;
	}
	
	@JsonIgnore
	public String getPassword() {
		Set<Credential> creds = this.getCredentials();
		String pwd = null;
		if (creds != null) {
			for(Credential c: creds) {
				if (c.getAuthServiceType().equalsIgnoreCase("password")) {
					pwd = c.getAuthData();
				}
			}
		}
		return pwd;
	}
	
	public String getEmail() {
		Set<Credential> creds = this.getCredentials();
		String email = null;
		if (creds != null) {
			for(Credential c: creds) {
				if (c.getAuthServiceType().equalsIgnoreCase("password")) {
					email = c.getEmail();
				}
			}
		}
		return email;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<AuthToken> getAuthTokens() {
		return authTokens;
	}

	public void setAuthTokens(Set<AuthToken> authTokens) {
		this.authTokens = authTokens;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", locked=");
		builder.append(locked);
		builder.append(", active=");
		builder.append(active);
		builder.append(", roles=");
		builder.append(roles);
		builder.append(", authTokens=");
		builder.append(authTokens);		
		builder.append(", created=");
		builder.append(created);
		builder.append("]");
		return builder.toString();
	}

}
