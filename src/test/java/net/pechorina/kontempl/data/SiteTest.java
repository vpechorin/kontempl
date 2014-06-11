package net.pechorina.kontempl.data;

import java.util.Map;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SiteTest extends TestCase {
	static final Logger logger = LoggerFactory.getLogger(SiteTest.class);

	public void testSitePropertySearch() {
		Site s = new Site(1, "somesite", "somedomain", "sometitle");

		s.addProperty(new SiteProperty(1, s, "prop1", "content1"));
		s.addProperty(new SiteProperty(2, s, "prop2", "content2"));
		s.addProperty(new SiteProperty(3, s, "prop3", "content3"));
		s.addProperty(new SiteProperty(4, s, "prop4", "content4"));
		s.addProperty(new SiteProperty(5, s, "prop5", "content5"));
		s.addProperty(new SiteProperty(6, s, "prop6", "content6"));

		SiteProperty testProp = new SiteProperty(2, s, "prop2", "content1");

		logger.debug("Site: " + s);

		assertTrue(s.hasSuchPropertyName(testProp.getName()));
		assertFalse(s.hasSuchPropertyName("xyz"));
		
		Map<String,String> propMap = s.getPropertyMap();
		logger.debug("as map: " + propMap);
		assertEquals(propMap.get("prop3"), "content3");
	}

}
