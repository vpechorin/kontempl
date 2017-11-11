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
import javax.persistence.Table
import javax.persistence.Transient

@Entity
@Table(name = "docfile")
@EqualsAndHashCode(includes=['id', 'name', 'pageId', 'contentType', 'title', 'sortIndex', 'fileSize'])
@ToString(includeNames=true)
@JsonIgnoreProperties(ignoreUnknown=true)
class DocFile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id

	Integer pageId

	String name
	
	String title
	
	String contentType
	
	int sortIndex

	int width
	int height
	long fileSize
	
	String getHFileSize() {
		return UnitUtils.bytesToHuman(fileSize)
	}
	
	@Transient
	String getAbsolutePath() {
		return File.separator + pageId + File.separator + "docs" + File.separator + this.name
	}
	
	@Transient
	String getDirectoryPath() {
		return File.separator + pageId + File.separator + "docs"
	}
	
	@Transient
	@JsonIgnore
	void setPropertiesFromFileMeta(FileMeta fm) {
		this.contentType = fm.fileType
		this.fileSize = fm.fileSize
		this.name = fm.fileName
		this.sortIndex = 10
	}
}
