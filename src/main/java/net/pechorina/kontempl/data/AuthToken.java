package net.pechorina.kontempl.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "authtoken", indexes={
	@Index(name="updatedIdx", columnList="updated")
})
public class AuthToken implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyy-MM-dd");
	
	@Id
	private String uuid;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "userId")
	private User user;
	
	@JsonIgnore
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime created;
	
	@JsonIgnore
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime updated;
	
	private String ip;
	private String ua;

	public AuthToken() {
		super();
		this.uuid = UUID.randomUUID().toString();
		
		DateTime now = new DateTime();
		this.created = now;
		this.updated = now;
	}

	public AuthToken(User user, String ip, String ua) {
		super();
		this.uuid = UUID.randomUUID().toString();
		
		DateTime now = new DateTime();
		this.user = user;
		this.created = now;
		this.updated = now;
		this.ip = ip;
		this.ua = ua;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		if (!user.getAuthTokens().contains(this)) {
			user.getAuthTokens().add(this);
		}
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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUa() {
		return ua;
	}

	public void setUa(String ua) {
		this.ua = ua;
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
	
	@Transient
	@JsonProperty("updatedSec")
	public long getUpdatedSec() {
		return this.getUpdated().getMillis() / 1000;
	}

	@Override
	public String toString() {
        return "AuthToken [uuid=" + uuid + ", user=" + user + ", created=" + created + ", updated=" + updated + ", ip=" + ip + ", ua=" + ua + "]";
	}

}
