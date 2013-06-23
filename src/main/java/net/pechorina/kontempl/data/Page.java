package net.pechorina.kontempl.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import net.pechorina.kontempl.utils.StringUtils;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Index;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Entity
@Table(name = "page")
public class Page implements Serializable, Cloneable {
	private static final Logger logger = Logger.getLogger(Page.class);
	private static final long serialVersionUID = 1222384650558947506L;

	private static final DateTimeFormatter fmtW3C = DateTimeFormat.forPattern("EEE, dd MMM yyyy HH:mm:ss");
	private static final DateTimeFormatter fmtW3CDate = DateTimeFormat.forPattern("yyyy-MM-dd");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	@Index(name="parentIDX")	
	private Integer parentId;
	
	@NotNull
	@Index(name="sortIndexIDX")
	private int sortindex;
	
	@Index(name="pubIDX")
	private boolean publicPage;
	
	private boolean hideTitle;
	private boolean placeholder;

	@Column(unique = true)
	private String name;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updated;
	
	@Index(name="updatedByIDX")
	private Integer updatedBy;

	private String title;

	@Lob
	private String description;

	@Lob
	private String tags;

	@Lob
	private String body;
	
	@Transient
	private Map<String, PageElement> pageElements;
	
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
		this.updated = new Date();
		this.hideTitle = false;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the parentId
	 */
	public Integer getParentId() {
		return parentId;
	}

	/**
	 * @param parentId
	 *            the parentId to set
	 */
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

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
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

	public Map<String, PageElement> getPageElements() {
		return pageElements;
	}

	public void setPageElements(Map<String, PageElement> pageElements) {
		this.pageElements = pageElements;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
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
		
/*		if (d.length() < size) {
			if (this.getBody() != null && this.getBody().length() > 2) {
				String body = StringUtils.clearDescription(this.getBody());
				TextContentUtils tp = new TextContentUtils();
			    d+= " " +  tp.extractTextFromHTML(body);
			}
		}*/

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
		DateTime d = new DateTime(this.getUpdated());
		DateTime dtUTC = d.withZone(DateTimeZone.UTC);
		return dtUTC.toString(fmtW3C) + " GMT"; 
	}
	
	public String lastModifiedDate() {
		if (this.getUpdated() == null) {
			return null;
		}
		DateTime d = new DateTime(this.getUpdated());
		DateTime dtUTC = d.withZone(DateTimeZone.UTC);
		return dtUTC.toString(fmtW3CDate); 
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Page [id=");
		builder.append(id);
		builder.append(", parentId=");
		builder.append(parentId);
		builder.append(", sortindex=");
		builder.append(sortindex);
		builder.append(", publicPage=");
		builder.append(publicPage);
		builder.append(", hideTitle=");
		builder.append(hideTitle);
		builder.append(", name=");
		builder.append(name);
		builder.append(", created=");
		builder.append(created);
		builder.append(", updated=");
		builder.append(updated);
		builder.append(", updatedBy=");
		builder.append(updatedBy);
		builder.append(", title=");
		builder.append(title);
		builder.append(", description=");
		builder.append(description);
		builder.append(", tags=");
		builder.append(tags);
		builder.append(", body=");
		builder.append(body);
		if (this.pageElements != null) {
			builder.append(", pageElementsNum=");
			builder.append(this.pageElements.size());
		}
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Page))
			return false;
		Page other = (Page) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
