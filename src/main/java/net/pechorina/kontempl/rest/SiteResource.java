package net.pechorina.kontempl.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.pechorina.kontempl.data.Site;
import net.pechorina.kontempl.service.SiteService;
import net.pechorina.kontempl.view.AbstractController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/sites")
public class SiteResource extends AbstractController {
	private static final Logger logger = LoggerFactory.getLogger(SiteResource.class);
	
	@Autowired
	private SiteService siteService;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Site> list() {
		return siteService.listAll();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public Site getById(@PathVariable("id") Integer id) {
		return siteService.findById(id);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Site> createSite(@RequestBody Site site, HttpServletRequest request) {
		Site savedEntity = siteService.save(site);
		logger.info("SITE CREATE: " + savedEntity + " Src:" + request.getRemoteAddr());
		return new ResponseEntity<Site>(savedEntity, HttpStatus.CREATED);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public void save(@PathVariable("id") Integer id, @RequestBody Site site, HttpServletRequest request, HttpServletResponse response) {
		site.setId(id);
		Site savedEntity = siteService.save(site);
		logger.info("SITE SAVE: " + savedEntity + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void remove(@PathVariable("id") Integer id, HttpServletRequest request, HttpServletResponse response) {
		siteService.delete(id);
		logger.info("SITE DELETE: " + id + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
