package net.pechorina.kontempl.data;

import static javax.persistence.CascadeType.ALL;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "site")
public class Site implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(unique = true)
	private String name;
	
	private String title;
	
	private String domain;
	
	@OneToMany(mappedBy="site", fetch=FetchType.EAGER)
	@MapKey(name="name")
	private Map<String, SiteProperty> properties;
	
	@JsonIgnore
	@OneToMany(mappedBy="site", cascade = ALL)
	private List<Page> pages;
	
	@Transient
	private PageTree pageTree;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public List<Page> getPages() {
		return pages;
	}

	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

	public PageTree getPageTree() {
		return pageTree;
	}

	public void setPageTree(PageTree pageTree) {
		this.pageTree = pageTree;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Map<String, SiteProperty> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, SiteProperty> properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Site [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", title=");
		builder.append(title);
		builder.append(", domain=");
		builder.append(domain);
		builder.append("]");
		return builder.toString();
	}
	
}
