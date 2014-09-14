package net.pechorina.kontempl.data

import java.io.File;

import groovy.transform.TypeChecked
import groovy.util.logging.Slf4j
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@Entity
@Table(name = "thumbnail")
@JsonIgnoreProperties(ignoreUnknown=true)
@ToString(includeNames=true, excludes=["imageFile"])
@EqualsAndHashCode(includes=['id', 'name', 'pageId', 'imageFileId'])
@TypeChecked
@Slf4j
class Thumbnail {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id

	Integer pageId

	String name

	int width
	int height

	@Column(insertable = false, updatable = false, unique = true)
	Integer imageFileId;
	
	@OneToOne
	@JoinColumn(name = "imageFileId", referencedColumnName = "id")
	@JsonIgnore
	ImageFile imageFile
	
	@Transient
	String getAbsolutePath() {
		return getDirectoryPath() + File.separator + this.name
	}

	@Transient
	String getDirectoryPath() {
		return File.separator + pageId + File.separator + "thumbs"
	}
	
	@Transient
	@JsonIgnore
	void setPropertiesFromImageFile(ImageFile i) {
		this.name = i.getName().toLowerCase();
		this.pageId = i.getPageId();
		this.imageFile = i;
	}
	
	void setImageFile(ImageFile i) {
		this.imageFile = i
		if (this.imageFile.thumb != this) {
			this.imageFile.thumb = this
		}
	}
}
