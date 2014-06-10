package net.pechorina.kontempl.data;

import java.io.File;
import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;

import javax.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "imagefile", indexes={
//		@Index(name="pageIdIdx", columnList="pageId"),
//		@Index(name="nameIdx", columnList="name"),
		@Index(name="mainImgIdx", columnList="mainImage"),
//		@Index(name="sortIdx", columnList="sortIndex")
})
public class ImageFile implements Serializable, Cloneable {
	static final Logger logger = LoggerFactory.getLogger(ImageFile.class);
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer pageId;

	private String name;

	private String contentType;
	
	private boolean mainImage;
	
	private int sortIndex;

	private int width;
	private int height;
	private long fileSize;
	
	@OneToOne(mappedBy = "imageFile", fetch = EAGER)
	private Thumbnail thumb;

	public ImageFile() {
		super();
	}
	
	public ImageFile(FileMeta fm) {
		super();
		this.contentType = fm.getFileType();
		this.fileSize = fm.getFileSize();
		this.name = fm.getFileName();
		this.mainImage = false;
		this.sortIndex = 10;
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
	
	@Transient
	public String getAbsolutePath() {
		String res = File.separator + pageId + File.separator + "images" + File.separator + this.name;
		return res;
	}
	
	@Transient
	public String getDirectoryPath() {
		String res = File.separator + pageId + File.separator + "images";
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

	public int getWidth() {
		return this.width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return this.height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Integer getPageId() {
		return pageId;
	}

	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}

	public boolean isMainImage() {
		return mainImage;
	}

	public void setMainImage(boolean mainImage) {
		this.mainImage = mainImage;
	}

	public int getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(int sortIndex) {
		this.sortIndex = sortIndex;
	}

	public Thumbnail getThumb() {
		return thumb;
	}

	public void setThumb(Thumbnail thumb) {
		this.thumb = thumb;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ImageFile [id=");
		builder.append(id);
		builder.append(", pageId=");
		builder.append(pageId);
		builder.append(", name=");
		builder.append(name);
		builder.append(", contentType=");
		builder.append(contentType);
		builder.append(", width=");
		builder.append(width);
		builder.append(", height=");
		builder.append(height);
		builder.append(", fileSize=");
		builder.append(fileSize);
		builder.append("]");
		return builder.toString();
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ImageFile))
			return false;
		ImageFile other = (ImageFile) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone(); 
	}
	
	public ImageFile copy() {
		ImageFile im = null;
		try {
			im = (ImageFile) this.clone();
		} catch (CloneNotSupportedException e) {
			logger.error("Exception: " + e);
		}
		if (im != null) {
			im.setId(null);
		}
		return im;
	}
	
}
