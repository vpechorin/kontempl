package net.pechorina.kontempl.data

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.pechorina.kontempl.utils.UnitUtils

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.Transient

import static javax.persistence.FetchType.EAGER

@Entity
@Table(name = "imagefile")
@EqualsAndHashCode(includes=['id', 'name', 'pageId', 'contentType', 'mainImage', 'sortIndex', 'fileSize'])
@ToString(includeNames=true, excludes=["thumb"])
@JsonIgnoreProperties(ignoreUnknown=true)
class ImageFile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id

	Integer pageId

	String name

	String contentType
	
	Boolean mainImage
	
	int sortIndex

	int width
	int height
	long fileSize
	
	@OneToOne(mappedBy = "imageFile", fetch = EAGER)
	Thumbnail thumb
	
	String getHFileSize() {
		return UnitUtils.bytesToHuman(fileSize)
	}
	
	@Transient
	String getAbsolutePath() {
		return File.separator + pageId + File.separator + "images" + File.separator + this.name
	}
	
	@Transient
	String getDirectoryPath() {
		return File.separator + pageId + File.separator + "images"
	}
	
	@Transient
	@JsonIgnore
	void setPropertiesFromFileMeta(FileMeta fm) {
		this.contentType = fm.fileType
		this.fileSize = fm.fileSize
		this.name = fm.fileName
		this.mainImage = false
		this.sortIndex = 10
	}
	
	void setThumb(Thumbnail t) {
		this.thumb = t
		if (t.imageFile != this) {
			t.imageFile = this
		}
	}
}
