package net.pechorina.kontempl.rest;

import javax.servlet.http.HttpServletRequest;

import net.pechorina.kontempl.service.SitemapService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/sitemaps")
public class SitemapResource {
	private static final Logger logger = LoggerFactory.getLogger(SitemapResource.class);
	
	@Autowired
	private SitemapService sitemapService;
	
	@Autowired
	private Environment env;
	
	@RequestMapping(method = RequestMethod.POST, value = "/update")
	public ResponseEntity<String> updateSitemap(HttpServletRequest request) {
		sitemapService.onlyUpdateSitemap();
		logger.info("SITEMAPs UPDATED");
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/submit")
	public ResponseEntity<String> submitSitemap(HttpServletRequest request) {
		sitemapService.updateSitemap();
		logger.info("SITEMAPs UPDATED and SUBMITTED");
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}
}
