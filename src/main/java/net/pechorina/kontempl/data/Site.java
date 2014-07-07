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
		this.properties = new ArrayList<>();
		this.pages = new ArrayList<>();
	}

	public Site(Integer id, String name, String title, String domain) {
		super();
		this.id = id;
		this.name = name;
		this.title = title;
		this.domain = domain;
		this.properties = new ArrayList<>();
		this.pages = new ArrayList<>();
	}

	public Site(String name, String title, String domain) {
		super();
		this.name = name;
		this.title = title;
		this.domain = domain;
		this.properties = new ArrayList<>();
		this.pages = new ArrayList<>();
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
		this.pages.clear();
		this.pages.addAll(pages);
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
		this.properties.clear();
		this.properties.addAll(properties);
	}
	
	@Transient
	public void addProperty(SiteProperty p) {
		if (!hasSuchPropertyName(p.getName())) {
			this.properties.add(p);
			if (p.getSite() != this) {
				p.setSite(this);
			}
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
		SiteProperty sp = this.getProperties().stream().filter(p -> p.getName().equalsIgnoreCase(n)).findFirst().orElse(null);
		return (sp != null);
	}

	@Transient
	@JsonIgnore
	public SiteProperty findPropertyByName(String n) {
		SiteProperty sp = this.getProperties().stream().filter(p -> p.getName().equalsIgnoreCase(n)).findFirst().orElse(null);
		return sp;
	}
	
	@Transient
	@JsonIgnore
	public SiteProperty findPropertyById(int id) {
		SiteProperty sp = this.getProperties().stream().filter(p -> p.getId() == id).findFirst().orElse(null);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Site other = (Site) obj;
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
