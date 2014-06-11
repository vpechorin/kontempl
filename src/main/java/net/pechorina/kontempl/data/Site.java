package net.pechorina.kontempl.data;

import static javax.persistence.CascadeType.ALL;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "site")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Site implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(unique = true)
	private String name;
	
	private String title;
	
	private String domain;
	
	@OneToMany(mappedBy="site", fetch=FetchType.EAGER, cascade=ALL, orphanRemoval=true)
	@OrderBy("name ASC")
	private List<SiteProperty> properties;
	
	@JsonIgnore
	@OneToMany(mappedBy="site", cascade = ALL)
	private List<Page> pages;
	
	@Transient
	private PageTree pageTree;

	public Site() {
		super();
	}

	public Site(Integer id, String name, String title, String domain) {
		super();
		this.id = id;
		this.name = name;
		this.title = title;
		this.domain = domain;
	}

	public Site(String name, String title, String domain) {
		super();
		this.name = name;
		this.title = title;
		this.domain = domain;
	}

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

	public List<SiteProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<SiteProperty> properties) {
		this.properties = properties;
	}
	
	@Transient
	public void addProperty(SiteProperty p) {
		List<SiteProperty> props = this.getProperties();
		if (props == null) props = new ArrayList<SiteProperty>();
		if (!hasSuchPropertyName(p.getName())) {
			props.add(p);
			this.setProperties(props);
		}
	}
	
	@Transient
	public Map<String,String> getPropertyMap() {
		if (this.getProperties() == null) return null;
		Map<String,String> m = this.getProperties().stream().collect(Collectors.toMap(SiteProperty::getName, p -> p.getContent()));
		return m;
	}
	
	@Transient
	@JsonIgnore
	public boolean hasSuchPropertyName(String n) {
		if (this.getProperties() == null) return false;
		SiteProperty sp = this.getProperties().stream().filter(p -> p.getName().equalsIgnoreCase(n)).findFirst().orElse(null);
		return (sp != null);
	}

	@Transient
	@JsonIgnore
	public SiteProperty findPropertyByName(String n) {
		if (this.getProperties() == null) return null;
		SiteProperty sp = this.getProperties().stream().filter(p -> p.getName().equalsIgnoreCase(n)).findFirst().orElse(null);
		return sp;
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
		builder.append(", properties=");
		builder.append(properties);
		builder.append("]");
		return builder.toString();
	}
	
}
