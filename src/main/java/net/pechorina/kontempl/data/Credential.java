package net.pechorina.kontempl.data;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
//import javax.persistence.UniqueConstraint;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.Index;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Entity implementation class for Entity: Credential
 * 
 */
@Entity
@Table(name = "credential")
public class Credential implements Serializable {

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
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated;
	
	@Index(name="activeIndex")
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
	
	@JsonIgnore
	private String authData;

	@Column(columnDefinition = "TEXT")
	private String optData;

	private static final long serialVersionUID = 1L;

	public Credential() {
		super();
		this.created = new Date();
		this.updated = new Date();		
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
		this.created = new Date();
		this.updated = new Date();
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

	/**
	 * @return the updated
	 */
	public Date getUpdated() {
		return updated;
	}

	/**
	 * @param updated
	 *            the updated to set
	 */
	public void setUpdated(Date updated) {
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
	
	@Transient
	@JsonProperty("createdDate")
	public String getCreatedDate() {
		DateTime dt = new DateTime(this.getCreated());
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		String strOutputDateTime = fmt.print(dt);

		 return strOutputDateTime;
	}	
	
	@Transient
	@JsonProperty("updatedDate")
	public String getUpdatedDate() {
		DateTime dt = new DateTime(this.getUpdated());
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		String strOutputDateTime = fmt.print(dt);

		 return strOutputDateTime;
	}	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Credential [id=");
		builder.append(id);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", created=");
		builder.append(created);
		builder.append(", updated=");
		builder.append(updated);
		builder.append(", active=");
		builder.append(active);
		builder.append(", verified=");
		builder.append(verified);
		builder.append(", authServiceType=");
		builder.append(authServiceType);
		builder.append(", uid=");
		builder.append(uid);
		builder.append(", username=");
		builder.append(username);
		builder.append(", email=");
		builder.append(email);
		builder.append(", link=");
		builder.append(link);
		builder.append(", authData=");
		builder.append(authData);
		builder.append(", optData=");
		builder.append(optData);
		builder.append("]");
		return builder.toString();
	}

}
