package net.pechorina.kontempl;

import static org.junit.Assert.assertNotNull;
import net.pechorina.kontempl.data.Site;
import net.pechorina.kontempl.service.SiteService;

import org.junit.Test;
import org.junit.runner.RunWith;
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
@DirtiesContext
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

	@Test
	public void testSiteCreate() throws Exception {
		Site s = new Site("testsite1", "Test site 1", "test localdomain");
		Site e = siteService.save(s);

		assertNotNull(e);
	}

}
