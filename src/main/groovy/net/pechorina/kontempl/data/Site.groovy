package net.pechorina.kontempl.data

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OrderBy
import javax.persistence.Table
import javax.persistence.Transient

import static javax.persistence.CascadeType.ALL

@Entity
@Table(name = "site")
@JsonIgnoreProperties(ignoreUnknown=true)
@ToString(includeNames=true, excludes=["pageTree", "pages", "properties"])
@EqualsAndHashCode(includes=['id', 'name', 'domain', 'homePage'])
@TypeChecked
@Slf4j
class Site {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id
	
	@Column(unique = true)
	String name
	
	String title
	
	String domain
	
	String homePage
	
	@OneToMany(mappedBy="site", fetch=FetchType.EAGER, cascade=ALL, orphanRemoval=true)
	@OrderBy("name ASC")
	List<SiteProperty> properties = new ArrayList<>()
	
	@JsonIgnore
	@OneToMany(mappedBy="site", cascade = ALL)
	@OrderBy("name ASC")
	List<Page> pages = new ArrayList<>()
	
	@Transient
	PageTree pageTree
	
	@Transient
	public void addProperty(SiteProperty p) {
		if (!hasSuchPropertyName(p.name)) {
			this.properties.add(p);
			if (p.site != this) {
				p.site = this;
			}
		}
	}
	
	void setPages(List<Page> pages) {
		this.pages.clear()
		this.pages.addAll(pages)
	}
	
	@Transient
	@JsonProperty(value="propertyMap")
	Map<String,String> getPropertyMap() {
		return this.properties.collectEntries { [(it.name): it.content] }
	}
	
	@Transient
	@JsonIgnore
	boolean hasSuchPropertyName(String n) {
		return this.properties.any { it.name == n }
	}

	@Transient
	@JsonIgnore
	SiteProperty findPropertyByName(String n) {
		return this.properties.find {it.name == n}
	}
	
	@Transient
	@JsonIgnore
	SiteProperty findPropertyById(int id) {
		return this.properties.find {it.id == id}
	}
}
