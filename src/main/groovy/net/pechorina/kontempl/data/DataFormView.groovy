package net.pechorina.kontempl.data

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includes=['name', 'title'])
@ToString(includeNames=true)
class DataFormView {
	String name
	String title
	String formFields
}
