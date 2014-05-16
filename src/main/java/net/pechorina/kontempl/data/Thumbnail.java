package net.pechorina.kontempl.data;

import static javax.persistence.CascadeType.PERSIST;

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
import javax.persistence.Index;

@Entity
@Table(name = "thumbnail", indexes={
		@Index(name="pageIdIdx", columnList="pageId"),
		@Index(name="nameIdx", columnList="name")
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
	
	@OneToOne(cascade = PERSIST)
	@JoinColumn(name = "imageFileId", referencedColumnName = "id")
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
		builder.append("]");
		return builder.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Thumbnail))
			return false;
		Thumbnail other = (Thumbnail) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
