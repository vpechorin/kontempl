package net.pechorina.kontempl.data

import groovy.transform.TypeChecked;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j;

@Entity
@Table(name = "site_property")
@EqualsAndHashCode(includes=['id', 'page', 'name'])
@ToString(includeNames=true, excludes=["page"])
@TypeChecked
@Slf4j
class SiteProperty {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id
	
	@ManyToOne
	@JoinColumn(name = "siteId")
	@JsonIgnore
	Site site
	
	String name
	
	@Lob
	String content
	
	public void setSite(Site site) {
		this.site = site
		if (!site.properties.contains(this)) {
			site.properties.add(this)
		}
	}
}
