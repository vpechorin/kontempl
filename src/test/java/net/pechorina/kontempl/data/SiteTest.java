package net.pechorina.kontempl.data;

import java.util.Map;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SiteTest extends TestCase {
	static final Logger logger = LoggerFactory.getLogger(SiteTest.class);

	public void testSitePropertySearch() {

		Site s = new Site();
		s.setId(1);
		s.setDomain("2.test.localdomain");
		s.setName("testsite2");
		s.setTitle("Test Site No 2");
		
		s.addProperty(makeProp(1));
		s.addProperty(makeProp(2));
		s.addProperty(makeProp(3));
		s.addProperty(makeProp(4));
		s.addProperty(makeProp(5));
		s.addProperty(makeProp(6));

		SiteProperty testProp = makeProp(2);

		logger.debug("Site: " + s);

		assertTrue(s.hasSuchPropertyName(testProp.getName()));
		assertFalse(s.hasSuchPropertyName("xyz"));
		
		Map<String,String> propMap = s.getPropertyMap();
		logger.debug("as map: " + propMap);
		assertEquals(propMap.get("prop3"), "content3");
	}
	
	private SiteProperty makeProp(Integer n) {
		SiteProperty p = new SiteProperty();
		p.setId(n);
		p.setName("prop" + n);
		p.setContent("content" + n);
		return p;
	}

}
