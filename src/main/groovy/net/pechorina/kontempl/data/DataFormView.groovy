package net.pechorina.kontempl.data

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import groovy.transform.*

@EqualsAndHashCode(includes=['name', 'title'])
@ToString(includeNames=true)
class DataFormView {
	String name
	String title
	String formFields
}
