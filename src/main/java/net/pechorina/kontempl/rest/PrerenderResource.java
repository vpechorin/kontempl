package net.pechorina.kontempl.rest;

import javax.servlet.http.HttpServletRequest;

import net.pechorina.kontempl.service.Prerender;

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
@RequestMapping(value = "/api/prerender")
public class PrerenderResource {
	private static final Logger logger = LoggerFactory.getLogger(PrerenderResource.class);
	
	@Autowired
	private Prerender prerender;
	
	@Autowired
	private Environment env;
	
	@RequestMapping(method = RequestMethod.POST, value = "/submit")
	public ResponseEntity<String> doPrerender(HttpServletRequest request) {
		prerender.processSites();
		logger.info("doPrerender activated");
		return new ResponseEntity<String>("OK", HttpStatus.OK);
	}
}
