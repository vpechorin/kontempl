package net.pechorina.kontempl.data

import groovy.transform.TypeChecked;

import java.io.File;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TypeChecked

@Entity
@Table(name = "embedimage")
@EqualsAndHashCode(includes=['id', 'name', 'fileSize', 'page'])
@ToString(includeNames=true, excludes=["page"])
@TypeChecked
class EmbedImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id
	
	@Column(name="pageId", insertable=false, updatable=false)
	int pageId
	
	@ManyToOne
	@JoinColumn(name="pageId")
	@JsonIgnore
	Page page

	String name

	String contentType

	int width
	int height
	long fileSize
	
	String getHFileSize() {
		String hs = "";
		if (fileSize > (1024*1024)) {
			BigDecimal s = (fileSize / (1024 * 1024)).setScale(1, RoundingMode.HALF_UP)
			hs = "$s MB";
		}
		else if (fileSize < 1024) {
			hs = "$fileSize bytes";
		}
		else {
			BigDecimal sl = (this.fileSize / 1024).setScale(1, RoundingMode.HALF_UP)
			hs = "$sl KB";
		}
		return hs;
	}
	
	@Transient
	String getAbsolutePath() {
		String res = File.separator + page.getId() + File.separator + "embed" + File.separator + this.name;
		return res;
	}
	
	@Transient
	String getDirectoryPath() {
		String res = File.separator + page.getId() + File.separator + "embed";
		return res;
	}
}
