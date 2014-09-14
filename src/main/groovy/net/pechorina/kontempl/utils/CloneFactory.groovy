package net.pechorina.kontempl.utils

import java.util.Set;

import net.pechorina.kontempl.data.DocFile
import net.pechorina.kontempl.data.ImageFile;
import net.pechorina.kontempl.data.Page;

import org.codehaus.groovy.runtime.InvokerHelper

class CloneFactory {
	static ImageFile copyImageFile(ImageFile src) {
		def filter = ["id", "thumb"] as Set
		ImageFile target = new ImageFile()
		copyPropertiesWithoutSome(src, target, filter)
		return target
	}
	
	static Page copyPage(Page src) {
		def filter = ["id", "created", "updated", "properties", "embedImages", "mainImage", "images", "docs"] as Set
		Page newPage = new Page()
		copyPropertiesWithoutSome(src, newPage, filter)
		return newPage
	}
	
	static DocFile copyDocFile(DocFile src) {
		def filter = ["id"] as Set
		DocFile target = new DocFile()
		copyPropertiesWithoutSome(src, target, filter)
		return target
	}
	
	static copyProperties(source, target) {
		InvokerHelper.setProperties(target, source.properties)
	}
	
	static copyPropertiesWithoutSome(source, target, Set<String> skipSet) {
		def filter
		Map<Object, Object> m = new HashMap<>()

		source.properties.each {
			if (!skipSet.contains(it.key)) {
				m[it.key] = it.value
			}
		}
		
		InvokerHelper.setProperties(target, m )
	}
}
