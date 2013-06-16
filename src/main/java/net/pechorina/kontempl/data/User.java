package net.pechorina.kontempl.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.Index;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.common.base.Joiner;
import static javax.persistence.CascadeType.ALL;

/**
 * Entity implementation class for Entity: User
 * 
 */
@Entity
@Table(name = "user")
public class User implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	@Size(min = 2)
	private String name;
	
	@Index(name="lockedIdx")
	@NotNull
	private boolean locked;
	
	@Index(name="activeIdx")
	@NotNull
	private boolean active;
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "userrole", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
	private Set<Role> roles;
	
	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	
	@OneToMany(mappedBy="user", fetch = FetchType.EAGER, cascade = ALL)
	@JsonIgnore
	private Set<Credential> credentials;
	
	private static final long serialVersionUID = 1L;

	public User() {
		super();
		this.active = true;
		this.locked = false;
		this.created = new Date();
	}

	public User(String name) {
		super();
		this.created = new Date();		
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
		DateTime dt = new DateTime(this.getCreated());
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		String strOutputDateTime = fmt.print(dt);

		 return strOutputDateTime;
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

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created
	 *            the created to set
	 */
	public void setCreated(Date created) {
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
