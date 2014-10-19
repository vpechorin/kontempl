package net.pechorina.kontempl.data

import javax.persistence.Entity

import java.util.List;

import javax.persistence.*

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import groovy.transform.*

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
	boolean persist
	
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
