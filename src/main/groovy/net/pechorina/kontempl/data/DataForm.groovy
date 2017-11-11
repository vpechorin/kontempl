package net.pechorina.kontempl.data

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.Lob
import javax.persistence.OneToMany
import javax.persistence.OrderBy
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Table(name = "dataform", uniqueConstraints= @UniqueConstraint(columnNames = ["name"] ),
	indexes = [ @Index(name = "SITEID_IDX", columnList = "siteId")] )
@EqualsAndHashCode(includes=['id', 'name', 'siteId'])
@ToString(includeNames=true)
@JsonIgnoreProperties(ignoreUnknown = true)
class DataForm {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id
	
	String name
	
	String title
	
	int siteId
	
	@Lob
	String formFields

	// options	
	String emails
	Boolean persist
	
	@JsonIgnore
	@OneToMany(mappedBy="dataForm", cascade = CascadeType.ALL)
	@OrderBy("posted ASC")
	List<DataFormRecord> records = new ArrayList<>()
	
	void setRecords(List<DataFormRecord> list) {
		this.records.clear()
		this.records.addAll(list)
	}
	
	void addRecord(DataFormRecord r) {
		if (r.dataForm != this) {
			r.dataForm = this;
		}
		records.add(r);
	}
}
