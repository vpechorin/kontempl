package net.pechorina.kontempl.data

import groovy.transform.TypeChecked;

import static javax.persistence.FetchType.EAGER;

import java.io.File;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.pechorina.kontempl.utils.UnitUtils;

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TypeChecked

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
	
	boolean mainImage
	
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
