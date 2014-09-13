package net.pechorina.kontempl.data

import groovy.transform.ToString
import groovy.transform.TypeChecked

@ToString(includeNames=true)
@TypeChecked
class LoginForm {
	String username
	String password
}
