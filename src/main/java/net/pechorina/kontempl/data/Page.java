package net.pechorina.kontempl.data;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
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

@Entity
@Table(name = "page", indexes={
		@Index(name="nameIDX", columnList="name"),
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
	
	@ManyToOne( fetch=FetchType.EAGER )
	@JoinColumn(name = "siteId")
	private Site site;
	
	@NotNull
	private Integer parentId;
	
	@NotNull
	private int sortindex;
	
	private boolean publicPage;
	
	private boolean hideTitle;
	private boolean placeholder;

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
	
	@OneToMany(mappedBy="page", fetch=FetchType.EAGER)
	@MapKey(name="name")
	private Map<String, PageProperty> properties;
	
	@Transient
	private ImageFile mainImage;

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
		this.updated = new DateTime();
		this.hideTitle = false;
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

	public Map<String, PageProperty> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, PageProperty> properties) {
		this.properties = properties;
	}

	public void checkName() {
		if (this.getName().isEmpty()) {
			String pl = StringUtils.convertNameToPath(this.title)
					.toLowerCase();
			this.setName(pl);
		}
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
}
