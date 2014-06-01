package net.pechorina.kontempl.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@Table(name = "authtoken", indexes={
	@Index(name="updatedIdx", columnList="updated")
})
public class AuthToken implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator( name="system-uuid", strategy = "uuid")
	private String uuid;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "userId")
	private User user;
	
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime created;
	
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime updated;
	
	private String ip;
	private String ua;

	public AuthToken() {
		super();
		DateTime now = new DateTime();
		this.created = now;
		this.updated = now;
	}

	public AuthToken(User user, String ip, String ua) {
		super();
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AuthToken [uuid=");
		builder.append(uuid);
		builder.append(", user=");
		builder.append(user);
		builder.append(", created=");
		builder.append(created);
		builder.append(", updated=");
		builder.append(updated);
		builder.append(", ip=");
		builder.append(ip);
		builder.append(", ua=");
		builder.append(ua);
		builder.append("]");
		return builder.toString();
	}

}
