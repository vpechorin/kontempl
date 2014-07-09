package net.pechorina.kontempl.data;

import java.io.File;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "thumbnail", indexes={
//		@Index(name="pageIdIdx", columnList="pageId"),
//		@Index(name="nameIdx", columnList="name")
})
public class Thumbnail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer pageId;

	private String name;

	private int width;
	private int height;

	@Column(insertable = false, updatable = false, unique = true)
	private Integer imageFileId;
	
	@OneToOne
	@JoinColumn(name = "imageFileId", referencedColumnName = "id")
	@JsonIgnore
	private ImageFile imageFile;

	public Thumbnail() {
		super();
	}

	public Thumbnail(ImageFile i) {
		super();
		this.name = i.getName().toLowerCase();
		this.pageId = i.getPageId();
		this.imageFile = i;
	}

	@Transient
	public String getAbsolutePath() {
		String res = getDirectoryPath() + File.separator + this.name;
		return res;
	}

	@Transient
	public String getDirectoryPath() {
		return File.separator + pageId + File.separator + "thumbs";
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

	public Integer getImageFileId() {
		return imageFileId;
	}

	public void setImageFileId(Integer imageFileId) {
		this.imageFileId = imageFileId;
	}

	public ImageFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(ImageFile imageFile) {
		this.imageFile = imageFile;
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
		Thumbnail other = (Thumbnail) obj;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Thumbnail [id=");
		builder.append(id);
		builder.append(", pageId=");
		builder.append(pageId);
		builder.append(", name=");
		builder.append(name);
		builder.append(", width=");
		builder.append(width);
		builder.append(", height=");
		builder.append(height);
		builder.append(", imageFileId=");
		builder.append(imageFileId);
		builder.append("]");
		return builder.toString();
	}

}
