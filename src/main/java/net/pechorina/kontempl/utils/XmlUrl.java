package net.pechorina.kontempl.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@XmlAccessorType(value = XmlAccessType.NONE)
@XmlRootElement(name = "url")
public class XmlUrl {
	private static final DateTimeFormatter lastmodFmt = DateTimeFormat.forPattern("yyyy-MM-dd");
	
	public enum Priority {
		TOP("1.0"), ESSENTIAL("0.8"), HIGH("0.7"), MEDIUM("0.6"), LOW("0.5"), VERYLOW("0.3");

		private String value;

		Priority(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	@XmlElement
	private String loc;
	
	@XmlElement
	private String lastmod = new DateTime().withZone(DateTimeZone.UTC).toString(lastmodFmt);

	@XmlElement
	private String changefreq = "daily";

	@XmlElement
	private String priority;

	public XmlUrl() {
	}

	public XmlUrl(String loc, Priority priority) {
		this.loc = loc;
		this.priority = priority.getValue();
	}
	
	public XmlUrl(String loc, Priority priority, String changefreq) {
		this.loc = loc;
		this.priority = priority.getValue();
		this.changefreq = changefreq;
	}

	public String getLoc() {
		return loc;
	}

	public String getPriority() {
		return priority;
	}

	public String getChangefreq() {
		return changefreq;
	}

	public String getLastmod() {
		return lastmod;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

	public void setLastmod(String lastmod) {
		this.lastmod = lastmod;
	}

	public void setChangefreq(String changefreq) {
		this.changefreq = changefreq;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}
}
