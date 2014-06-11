package net.pechorina.kontempl.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "site_property")
@JsonIgnoreProperties(ignoreUnknown=true)
public class SiteProperty implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "siteId")
	@JsonIgnore
	private Site site;

	private String name;

	@Lob
	private String content;

	public SiteProperty(Integer id, Site site, String name, String content) {
		super();
		this.id = id;
		this.site = site;
		this.name = name;
		this.content = content;
	}

	public SiteProperty() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SiteProperty [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", content=");
		builder.append(content);
		builder.append("]");
		return builder.toString();
	}

}
