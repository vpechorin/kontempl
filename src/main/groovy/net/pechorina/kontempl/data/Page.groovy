package net.pechorina.kontempl.data

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TypeChecked;
import groovy.util.logging.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import net.pechorina.kontempl.utils.StringUtils;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "page")
@JsonIgnoreProperties(ignoreUnknown=true)
@ToString(includeNames=true, excludes=["mainImage", "images", "docs"])
@EqualsAndHashCode(includes=['id', 'siteId', 'name', 'title'])
@TypeChecked
@Slf4j
class Page {
	static DateTimeFormatter fmtW3C = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss")
	static DateTimeFormatter fmtW3CDate = DateTimeFormat.forPattern("yyyy-MM-dd")
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id
	
	@JsonIgnore
	@ManyToOne( fetch=FetchType.LAZY )
	@JoinColumn(name = "siteId")
	Site site
	
	@Column(name="siteId", insertable=false, updatable=false)
	int siteId
	
	@NotNull
	Integer parentId
	
	@NotNull
	int sortindex
	
	boolean publicPage = false
	boolean autoName = true
	boolean hideTitle = false
	boolean placeholder = false
	boolean richText = true
	boolean includeForm = false

	String name

	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	DateTime created = new DateTime()

	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	DateTime updated = new DateTime()

	String title
	
	String htmlTitle

	@Lob
	String description

	@Lob
	String tags

	@Lob
	String body
	
	Integer formId
	
	@OneToMany(mappedBy="page", fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
	@OrderBy("name ASC")
	List<PageProperty> properties = new ArrayList<>()
	
	@OneToMany(mappedBy="page", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	@JsonIgnore
	Set<EmbedImage> embedImages = new HashSet<>()
	
	@Transient
	ImageFile mainImage
	
	@Transient
	List<ImageFile> images = new ArrayList<>()
	
	@Transient
	List<DocFile> docs = new ArrayList<>()
	
	void setProperties(List<PageProperty> properties) {
		this.properties.clear()
		this.properties.addAll(properties)
	}
	
	void setEmbedImages(Set<EmbedImage> embedImages) {
		this.embedImages.clear()
		this.embedImages.addAll(embedImages)
	}
	
	void addEmbedImage(EmbedImage im) {
		if (im.page != this) {
			im.page = this;
		}
		embedImages.add(im);
	}
	
	void checkName() {
		if (this.getName().isEmpty()) {
			String pl = StringUtils.convertNameToPath(this.title).toLowerCase()
			this.setName(pl)
		}
	}
	
	@JsonProperty(value="metadesc")
	String getTextDescription(Integer size) {
		if (size == null) {
			size = 500
		}
		String d = ''
		
		if (this.getDescription() != null && this.getDescription().length() > 2) {
			d += StringUtils.clearDescription(this.getDescription())
		}

		d = d.trim()
		if (d.length() > size) {
			d = d.substring(0, size) + ' ...'
		}
		return d
	}
	
	String lastModified() {
		if (this.getUpdated() == null) {
			return null
		}
		DateTime dtUTC = this.getUpdated().withZone(DateTimeZone.UTC)
		return dtUTC.toString(fmtW3C) + " GMT"
	}
	
	String lastModifiedDate() {
		if (this.getUpdated() == null) {
			return null
		}
		DateTime dtUTC = this.getUpdated().withZone(DateTimeZone.UTC)
		return dtUTC.toString(fmtW3CDate)
	}
	
	@Transient
	void addProperty(PageProperty prop) {
		if (!hasSuchPropertyName(prop.name)) {
			this.properties.add(prop)
			if (prop.page != this) {
				prop.page = this
			}
		}
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
	PageProperty findPropertyByName(String n) {
		return this.properties.find {it.name == n}
	}
	
	@Transient
	@JsonIgnore
	PageProperty findPropertyById(int id) {
		return this.properties.find {it.id == id}
	}
}
