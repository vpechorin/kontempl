package net.pechorina.kontempl.data

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.hibernate.annotations.Type
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.JoinColumn
import javax.persistence.Lob
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.Transient

@Entity
@Table(name = "dataform_record", indexes = [ @Index(name = "POSTED_IDX", columnList = "posted")])
@EqualsAndHashCode(includes=['uuid'])
@ToString(includeNames=true)
@JsonIgnoreProperties(ignoreUnknown = true)
class DataFormRecord {
	static DateTimeFormatter dateFmt = DateTimeFormat.forPattern("yyyy-MM-dd");
	static DateTimeFormatter datetimeFmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id
	
	@Column(insertable=false, updatable=false)
	Integer formId
	
	@JsonIgnore
	@ManyToOne( fetch=FetchType.LAZY )
	@JoinColumn(name = "formId")
	DataForm dataForm

	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	DateTime posted = new DateTime()
	
	@Lob
	String data
	
	String ip
	String ua
	
	void setDataForm(DataForm f) {
		this.dataForm = f
		if (!f.records.contains(this)) {
			f.records.add(this);
		}
	}
	
	@Transient
	@JsonProperty("postedDate")
	String postedDate() {
		if (posted) {
			return posted.toString(dateFmt)
		}
		return null
	}
	
	@Transient
	@JsonProperty("postedTime")
	String postedTime() {
		if (posted) {
			return posted.toString(datetimeFmt)
		}
		return null
	}
}
