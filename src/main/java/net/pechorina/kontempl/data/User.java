package net.pechorina.kontempl.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Entity implementation class for Entity: User
 * 
 */
@Entity
@Table(name = "user", indexes={
		@Index(name="lockedIdx", columnList="locked"),
//		@Index(name="activeIdx", columnList="active")
})
@JsonIgnoreProperties(ignoreUnknown = true)
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
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="user_role")
	private Set<String> roles = new HashSet<String>();
	
	@JsonIgnore
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime created;
	
	@JsonIgnore
	@OneToMany(mappedBy="user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
	private Set<Credential> credentials;
	
	@JsonIgnore
	@OneToMany(mappedBy="user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval=true)
	private Set<AuthToken> authTokens;
	
	private static final long serialVersionUID = 1L;

	public User() {
		super();
		this.active = true;
		this.locked = false;
		this.created = new DateTime();
		this.roles = new HashSet<>();
		this.credentials = new HashSet<>();
		this.authTokens = new HashSet<>();
	}

	public User(String name) {
		super();
		this.created = new DateTime();		
		this.active = true;
		this.locked = false;
		this.name = name;
		this.roles = new HashSet<>();
		this.credentials = new HashSet<>();
		this.authTokens = new HashSet<>();		
	}
	
	@Transient
	@JsonProperty("createdDate")
	public String getCreatedDate() {
		if (this.getCreated() != null)
			return this.getCreated().toString(dateFmt);
		return null;
	}
	
	@Transient
	@JsonProperty("roleMap")
	public Map<String, Boolean> roleMap() {
		Map<String, Boolean> m = new HashMap<String, Boolean>();
		if (this.roles != null) {
			for(String r: this.roles) {
				m.put(r, true);
			}
		}
		return m;
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
		this.credentials.clear();
		this.credentials.addAll( credentials );
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
	
	@Transient
	@JsonIgnore
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

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public Set<AuthToken> getAuthTokens() {
		return authTokens;
	}

	public void setAuthTokens(Set<AuthToken> authTokens) {
		this.authTokens.clear();
		this.authTokens.addAll( authTokens );
	}
	
	public void addCredential(Credential c) {
		this.getCredentials().add(c);
		if (c.getUser() != this) {
			c.setUser(this);
		}
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
		builder.append(", created=");
		builder.append(created);
		builder.append("]");
		return builder.toString();
	}

}
