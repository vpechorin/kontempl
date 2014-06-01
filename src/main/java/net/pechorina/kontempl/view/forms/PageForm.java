package net.pechorina.kontempl.view.forms;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import net.pechorina.kontempl.data.Page;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageForm implements Serializable {

	static final Logger logger = LoggerFactory.getLogger(PageForm.class);
	private static final long serialVersionUID = 1L;

	@NotNull
	private Integer id;

	@NotNull
	private Integer parentId;
	
	@NotNull
	private String siteName;

	@NotNull
	private int sortindex;

	private boolean publicPage;

	private boolean hideTitle;
	private boolean placeholder;

	private boolean autoName;
	private String name;

	private Date created;

	private Date updated;

	private String title;

	private String description;

	private String tags;

	private String body;

	public PageForm() {
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
	
	public PageForm(Page p) {
		this.id = p.getId();
		this.parentId = p.getParentId();
		this.sortindex = p.getSortindex();
		this.publicPage = p.isPublicPage();
		this.name = p.getName();
		this.title = p.getTitle();
		this.description = p.getDescription();
		this.body = p.getBody();
		this.tags = p.getTags();
		this.updated = p.getUpdated().toDate();
		this.created = p.getCreated().toDate();
		this.hideTitle = p.isHideTitle();
		this.placeholder = p.isPlaceholder();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public int getSortindex() {
		return sortindex;
	}

	public void setSortindex(int sortindex) {
		this.sortindex = sortindex;
	}

	public boolean isPublicPage() {
		return publicPage;
	}

	public void setPublicPage(boolean publicPage) {
		this.publicPage = publicPage;
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

	public boolean isAutoName() {
		return autoName;
	}

	public void setAutoName(boolean autoName) {
		this.autoName = autoName;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PageForm [id=");
		builder.append(id);
		builder.append(", parentId=");
		builder.append(parentId);
		builder.append(", siteName=");
		builder.append(siteName);
		builder.append(", sortindex=");
		builder.append(sortindex);
		builder.append(", publicPage=");
		builder.append(publicPage);
		builder.append(", hideTitle=");
		builder.append(hideTitle);
		builder.append(", placeholder=");
		builder.append(placeholder);
		builder.append(", autoName=");
		builder.append(autoName);
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
		builder.append("]");
		return builder.toString();
	}



}
