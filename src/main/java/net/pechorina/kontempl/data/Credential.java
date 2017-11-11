package net.pechorina.kontempl.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Entity implementation class for Entity: Credential
 * 
 */
@Entity
@Table(name = "credential", indexes={
		@Index(name="activeIdx", columnList="active"),
		@Index(name="userIdx", columnList="userId"),
		@Index(name="usernameIdx", columnList="username"),
		@Index(name="uidIdx", columnList="uid")
})
public class Credential implements Serializable {
	
	private static final DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyy-MM-dd");
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(insertable=false, updatable=false)
	private Integer userId;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;

	@JsonIgnore
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime created;

	@JsonIgnore
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime updated;
	
	private boolean active;
	private boolean verified;

	private String authServiceType;
	
    /** define unique user id, format: serviceName:uid, i.e. facebook:100001328869081
    */
	@Column(unique = true)
	private String uid;

	private String username;
	private String email;
	private String link;
	
	@Transient
	private String password;
	
	@JsonIgnore
	private String authData;
	
	@JsonIgnore
	@Column(columnDefinition = "TEXT")
	private String optData;

	private static final long serialVersionUID = 1L;

	public Credential() {
		super();
		this.created = new DateTime();
		this.updated = new DateTime();		
		this.active = true;
		this.verified = false;
	}

	/**
	 * Constructor
	 * 
	 * @param user
	 *            - User entity
	 * @param authServiceType
	 *            - AuthService type string, i.e. "password" | "facebook" ...
	 * @param uid
	 *            - unique user identificator(for example email for password
	 *            auth)
	 * @param email
	 *            - User email
	 * @param authData
	 *            - Password if it is a passowrd auth
	 * @return
	 */
	public Credential(User user, String authServiceType, String uid,
			String email, String authData) {
		super();
		this.created = new DateTime();
		this.updated = new DateTime();
		this.active = true;
		this.verified = false;
		this.user = user;
		this.setAuthServiceType(authServiceType);
		this.uid = uid;
		this.email = email;
		this.authData = authData;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public DateTime getCreated() {
		return created;
	}

	public void setCreated(DateTime created) {
		this.created = created;
	}

	public DateTime getUpdated() {
		return updated;
	}

	public void setUpdated(DateTime updated) {
		this.updated = updated;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public String getUid() {
		return this.uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLink() {
		return this.link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getOptData() {
		return this.optData;
	}

	public void setOptData(String optData) {
		this.optData = optData;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		if (!user.getCredentials().contains(this)) {
			user.getCredentials().add(this);
		}
	}

	public String getAuthData() {
		return authData;
	}

	public void setAuthData(String authData) {
		this.authData = authData;
	}

	public String getAuthServiceType() {
		return authServiceType;
	}

	public void setAuthServiceType(String authServiceType) {
		this.authServiceType = authServiceType;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Transient
	@JsonProperty("createdDate")
	public String getCreatedDate() {
		return this.getCreated().toString(dateFmt);
	}	
	
	@Transient
	@JsonProperty("updatedDate")
	public String getUpdatedDate() {
		return this.getUpdated().toString(dateFmt);
	}	

	@Override
	public String toString() {
        return "Credential [id=" + id + ", userId=" + userId + ", created=" + created + ", updated=" + updated + ", active=" + active + ", verified=" + verified + ", authServiceType=" + authServiceType + ", uid=" + uid + ", username=" + username + ", email=" + email + ", link=" + link + ", authData=" + authData + ", optData=" + optData + "]";
	}

}
