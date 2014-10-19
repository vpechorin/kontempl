package net.pechorina.kontempl.data


import javax.persistence.*

import org.hibernate.annotations.Type
import org.joda.time.DateTime

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import groovy.transform.*

@Entity
@Table(name = "dataform_record", indexes = [ @Index(name = "POSTED_IDX", columnList = "posted")])
@EqualsAndHashCode(includes=['uuid'])
@ToString(includeNames=true)
@JsonIgnoreProperties(ignoreUnknown = true)
class DataFormRecord {
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
}
