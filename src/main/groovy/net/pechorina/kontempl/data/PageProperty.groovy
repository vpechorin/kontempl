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
@Table(name = "page_property")
@EqualsAndHashCode(includes=['id', 'page', 'name'])
@ToString(includeNames=true, excludes=["page"])
@TypeChecked
@Slf4j
class PageProperty {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id
	
	@ManyToOne
	@JoinColumn(name="pageId")
	@JsonIgnore
	Page page
	
	String name
	
	@Lob
	String content
		
	void setPage(Page page) {
		this.page = page;
		if (!page.properties.contains(this)) {
			page.properties.add(this);
		}
	}
}
