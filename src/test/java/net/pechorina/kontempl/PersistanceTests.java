package net.pechorina.kontempl;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.data.Site;
import net.pechorina.kontempl.service.PageService;
import net.pechorina.kontempl.service.SiteService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Equals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
@IntegrationTest
public class PersistanceTests {
	static final Logger logger = LoggerFactory
			.getLogger(PersistanceTests .class);

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private Environment env;

//	@Value("${security.user.name}")
//	private String user;

//	@Value("${security.user.password}")
//	private String password;

	@Autowired
	private SiteService siteService;
	
	@Autowired
	private PageService pageService;

	@Test
	public void testSiteCreate() throws Exception {
		Site s = new Site("testsite1", "Test site 1", "test localdomain");
		Site e = siteService.save(s);

		assertNotNull(e);
	}
	
	@Test
	public void testPageMove() throws Exception {
		Page p = pageService.getPage(10);
		int currentIdx = pageService.getPosition(p);
		logger.debug("current idx: " + currentIdx + " sortindex: " + p.getSortindex());
		pageService.movePage(p, 2, p.getParentId());
		int newIdx = pageService.getPosition(p);
		logger.debug("new idx: " + newIdx + " sortindex: " + p.getSortindex());
		assertThat(newIdx, equalTo(2));
		assertThat(newIdx, not(9));
	}

}
