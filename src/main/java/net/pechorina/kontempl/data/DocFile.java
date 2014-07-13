package net.pechorina.kontempl.data;

import java.io.File;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "docfile", indexes={
		@Index(name="pageIdIdx", columnList="pageId"),
		@Index(name="nameIdx", columnList="name"),
		@Index(name="sortIdx", columnList="sortIndex")
})
@JsonIgnoreProperties(ignoreUnknown=true)
public class DocFile implements Serializable, Cloneable {
	static final Logger logger = LoggerFactory.getLogger(DocFile.class);
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer pageId;

	private String name;
	
	private String title;

	private String contentType;
	
	private int sortIndex;

	private long fileSize;

	public DocFile() {
		super();
	}
	
	public DocFile(FileMeta fm) {
		super();
		this.contentType = fm.getFileType();
		this.fileSize = fm.getFileSize();
		this.name = fm.getFileName();
		this.sortIndex = 10;
	}
	
	@Transient
	public String getAbsolutePath() {
		String res = File.separator + pageId + File.separator + "docs" + File.separator + this.name;
		return res;
	}
	
	@Transient
	public String getDirectoryPath() {
		String res = File.separator + pageId + File.separator + "docs";
		return res;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
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

	public long getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getContentType() {
		return this.contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Integer getPageId() {
		return pageId;
	}

	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}

	public int getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}

	
	public String getHFileSize() {
		String hs = "";
		if (this.fileSize > (1024*1024)) {
			float s = new Long(this.fileSize).floatValue() / (1024f * 1024f);
			hs = String.format("%.1f", s) + " MB";
		}
		else if (this.fileSize < 1024) {
			hs = this.fileSize + " bytes";
		}
		else {
			long sl = this.fileSize / 1024l;
			hs = String.format("%d", sl) + " KB";
		}
		return hs;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone(); 
	}
	
	public DocFile copy() {
		DocFile im = null;
		try {
			im = (DocFile) this.clone();
		} catch (CloneNotSupportedException e) {
			logger.error("Exception: " + e);
		}
		if (im != null) {
			im.setId(null);
		}
		return im;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DocFile [id=");
		builder.append(id);
		builder.append(", pageId=");
		builder.append(pageId);
		builder.append(", name=");
		builder.append(name);
		builder.append(", title=");
		builder.append(title);
		builder.append(", contentType=");
		builder.append(contentType);
		builder.append(", sortIndex=");
		builder.append(sortIndex);
		builder.append(", fileSize=");
		builder.append(fileSize);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((pageId == null) ? 0 : pageId.hashCode());
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
		DocFile other = (DocFile) obj;
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
		if (pageId == null) {
			if (other.pageId != null)
				return false;
		} else if (!pageId.equals(other.pageId))
			return false;
		return true;
	}
	
}
