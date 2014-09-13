package net.pechorina.kontempl.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import net.pechorina.kontempl.utils.StringUtils;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "page", indexes={
		@Index(name="parentIDX", columnList="parentId"),
		@Index(name="sortIndexIDX", columnList="sortindex"),
		@Index(name="pubIDX", columnList="publicPage")
})
public class Page implements Serializable, Cloneable {
	static final Logger logger = LoggerFactory.getLogger(Page.class);
	private static final long serialVersionUID = 1L;

	private static final DateTimeFormatter fmtW3C = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss");
	private static final DateTimeFormatter fmtW3CDate = DateTimeFormat.forPattern("yyyy-MM-dd");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@JsonIgnore
	@ManyToOne( fetch=FetchType.LAZY )
	@JoinColumn(name = "siteId")
	private Site site;
	
	@Column(name="siteId", insertable=false, updatable=false)
	private int siteId;
	
	@NotNull
	private Integer parentId;
	
	@NotNull
	private int sortindex;
	
	private boolean publicPage;
	private boolean autoName;
	private boolean hideTitle;
	private boolean placeholder;
	private boolean richText;

	private String name;

	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime created;

	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime updated;

	private String title;

	@Lob
	private String description;

	@Lob
	private String tags;

	@Lob
	private String body;
	
	@OneToMany(mappedBy="page", fetch=FetchType.EAGER, cascade=CascadeType.ALL, orphanRemoval=true)
	@OrderBy("name ASC")
	private List<PageProperty> properties;
	
	@OneToMany(mappedBy="page", fetch=FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval=true)
	@JsonIgnore
	private Set<EmbedImage> embedImages;
	
	@Transient
	private ImageFile mainImage;
	
	@Transient
	private List<ImageFile> images;
	
	@Transient
	private List<DocFile> docs;
	
	@JsonIgnoreProperties(ignoreUnknown=true)
	public Page() {
		super();
		this.parentId = 0;
		this.sortindex = 100;
		this.publicPage = false;
		this.name = "";
		this.title = "";
		this.description = "";
		this.body = "";
		this.tags = "";
		
		DateTime d = new DateTime(); 
		this.updated = d;
		this.created = d;
		
		this.publicPage = false;
		this.hideTitle = false;
		this.autoName = true;
		this.richText = true;
		this.properties = new ArrayList<>();
		this.embedImages = new HashSet<EmbedImage>();
	}
	
	public void addEmbedImage(EmbedImage im) {
		embedImages.add(im);
		if (im.getPage() != this) {
			im.setPage(this);
		}
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
		if (!site.getPages().contains(this)) {
			site.getPages().add(this);
		}
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public boolean isPublicPage() {
		return publicPage;
	}

	public void setPublicPage(boolean publicPage) {
		this.publicPage = publicPage;
	}

	public int getSortindex() {
		return sortindex;
	}

	public void setSortindex(int sortindex) {
		this.sortindex = sortindex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public boolean isHideTitle() {
		return hideTitle;
	}

	public void setHideTitle(boolean hideTitle) {
		this.hideTitle = hideTitle;
	}

	public boolean isPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(boolean placeholder) {
		this.placeholder = placeholder;
	}

	public ImageFile getMainImage() {
		return mainImage;
	}

	public void setMainImage(ImageFile mainImage) {
		this.mainImage = mainImage;
	}

	public List<PageProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<PageProperty> properties) {
		this.properties.clear();
		this.properties.addAll(properties);
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public boolean isAutoName() {
		return autoName;
	}

	public void setAutoName(boolean autoName) {
		this.autoName = autoName;
	}

	public boolean isRichText() {
		return richText;
	}

	public void setRichText(boolean richText) {
		this.richText = richText;
	}

	public List<ImageFile> getImages() {
		return images;
	}

	public void setImages(List<ImageFile> images) {
		this.images = images;
	}

	public List<DocFile> getDocs() {
		return docs;
	}

	public void setDocs(List<DocFile> docs) {
		this.docs = docs;
	}

	public Set<EmbedImage> getEmbedImages() {
		return embedImages;
	}

	public void setEmbedImages(Set<EmbedImage> embedImages) {
		this.embedImages = embedImages;
	}

	public void checkName() {
		if (this.getName().isEmpty()) {
			String pl = StringUtils.convertNameToPath(this.title)
					.toLowerCase();
			this.setName(pl);
		}
	}
	
	@JsonProperty(value="metadesc")
	public String getTextDescription(Integer size) {
		if (size == null) {
			size = 500;
		}
		String d = "";
		
		if (this.getDescription() != null && this.getDescription().length() > 2) {
			d += StringUtils.clearDescription(this.getDescription());
		}

		d = d.trim();
		if (d.length() > size) {
			d = d.substring(0, size) + " ...";
		}
		return d;
	}
	
	public String lastModified() {
		if (this.getUpdated() == null) {
			return null;
		}
		DateTime dtUTC = this.getUpdated().withZone(DateTimeZone.UTC);
		return dtUTC.toString(fmtW3C) + " GMT"; 
	}
	
	public String lastModifiedDate() {
		if (this.getUpdated() == null) {
			return null;
		}
		DateTime dtUTC = this.getUpdated().withZone(DateTimeZone.UTC);
		return dtUTC.toString(fmtW3CDate); 
	}
	
	@Transient
	public void addProperty(PageProperty p) {
		if (!hasSuchPropertyName(p.getName())) {
			this.properties.add(p);
			if (p.getPage() != this) {
	            p.setPage(this);
	        }
		}
	}
	
	@Transient
	@JsonProperty(value="propertymap")
	public Map<String,String> getPropertyMap() {
		Map<String,String> m = this.getProperties().stream().collect(Collectors.toMap(PageProperty::getName, p -> p.getContent()));
		return m;
	}
	
	@Transient
	@JsonIgnore
	public boolean hasSuchPropertyName(String n) {
		PageProperty sp = this.getProperties().stream().filter(p -> p.getName().equalsIgnoreCase(n)).findFirst().orElse(null);
		return (sp != null);
	}

	@Transient
	@JsonIgnore
	public PageProperty findPropertyByName(String n) {
		PageProperty sp = this.getProperties().stream().filter(p -> p.getName().equalsIgnoreCase(n)).findFirst().orElse(null);
		return sp;
	}
	
	@Transient
	@JsonIgnore
	public PageProperty findPropertyById(int id) {
		PageProperty sp = this.getProperties().stream().filter(p -> p.getId() == id).findFirst().orElse(null);
		return sp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((site == null) ? 0 : site.hashCode());
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
		Page other = (Page) obj;
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
		if (site == null) {
			if (other.site != null)
				return false;
		} else if (!site.equals(other.site))
			return false;
		return true;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Page clonedPage = (Page) super.clone(); 
		clonedPage.setId(null);
		return clonedPage;
	}
	
	public Page copy() {
		Page newPage = null;
		try {
			newPage = (Page) this.clone();
		} catch (CloneNotSupportedException e) {
			logger.error("Exception: " + e);
		}
		return newPage;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Page [id=");
		builder.append(id);
		builder.append(", site=");
		builder.append(site);
		builder.append(", parentId=");
		builder.append(parentId);
		builder.append(", sortindex=");
		builder.append(sortindex);
		builder.append(", publicPage=");
		builder.append(publicPage);
		builder.append(", autoName=");
		builder.append(autoName);
		builder.append(", hideTitle=");
		builder.append(hideTitle);
		builder.append(", placeholder=");
		builder.append(placeholder);
		builder.append(", richText=");
		builder.append(richText);
		builder.append(", name=");
		builder.append(name);
		builder.append(", created=");
		builder.append(created);
		builder.append(", updated=");
		builder.append(updated);
		builder.append(", title=");
		builder.append(title);
		builder.append(", description=");
		builder.append(description);
		builder.append(", tags=");
		builder.append(tags);
		builder.append(", body=");
		builder.append(body);
		builder.append(", properties=");
		builder.append(properties);
		builder.append(", mainImage=");
		builder.append(mainImage);
		builder.append(", images=");
		builder.append(images);
		builder.append(", docs=");
		builder.append(docs);
		builder.append("]");
		return builder.toString();
	}
	
	
}
