package net.pechorina.kontempl.view.forms;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import net.pechorina.kontempl.data.Site;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SiteForm implements Serializable {

	static final Logger logger = LoggerFactory.getLogger(SiteForm.class);
	private static final long serialVersionUID = 1L;

	@NotNull
	private String name;

	@NotNull
	private String title;

	@NotNull
	private String domain;

	public SiteForm() {
		super();
		this.name = "default";
		this.title = "";
		this.domain = "";
	}

	public SiteForm(Site s) {
		this.name = s.getName();
		this.title = s.getTitle();
		this.domain = s.getDomain();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SiteForm [");
		builder.append("name=");
		builder.append(name);
		builder.append(", title=");
		builder.append(title);
		builder.append(", domain=");
		builder.append(domain);
		builder.append("]");
		return builder.toString();
	}

}
