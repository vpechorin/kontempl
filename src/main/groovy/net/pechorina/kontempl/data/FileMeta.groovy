package net.pechorina.kontempl.data

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@EqualsAndHashCode(includes=['uuid', 'fileName', 'fileSize', 'fileType'])
@ToString(includeNames=true,excludes=["bytes"])
@JsonIgnoreProperties(ignoreUnknown=true)
class FileMeta {
	String uuid = UUID.randomUUID().toString()
	String fileName
	long fileSize
	String fileType

	@JsonIgnore
	byte[] bytes
}
