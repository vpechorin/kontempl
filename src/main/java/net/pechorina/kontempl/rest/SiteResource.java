package net.pechorina.kontempl.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.pechorina.kontempl.data.Site;
import net.pechorina.kontempl.data.SiteProperty;
import net.pechorina.kontempl.service.SiteService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/sites")
public class SiteResource {
	private static final Logger logger = LoggerFactory.getLogger(SiteResource.class);
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private Environment env;
	
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
	
	@RequestMapping(method = RequestMethod.POST, value = "/{id}")
	public ResponseEntity<Site> createSiteProperty(@PathVariable("id") Integer siteId, 
			@RequestBody SiteProperty siteProperty, HttpServletRequest request) {
		Site site = siteService.findById(siteId);
		
		siteProperty.setSite(site);
		site.addProperty(siteProperty);
		
		Site savedEntity = siteService.save(site);
		logger.info("SITE PROPERTY CREATE: " + savedEntity + " Src:" + request.getRemoteAddr());
		return new ResponseEntity<Site>(savedEntity, HttpStatus.CREATED);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}/{propId}")
	public void saveProperty(@PathVariable("id") Integer siteId, 
			@PathVariable("propId") Integer propertyId,
			@RequestBody SiteProperty siteProperty, 
			HttpServletRequest request, HttpServletResponse response) {
		Site site = siteService.findById(siteId);
		
		// merge updates
		SiteProperty existingProperty = site.findPropertyById(propertyId);
		if (existingProperty != null) {
			existingProperty.setName(siteProperty.getName().trim());
			existingProperty.setContent(siteProperty.getContent().trim());
		}

		Site savedEntity = siteService.save(site);
		logger.info("SITE PROPERTY SAVE: " + savedEntity + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}/{propName}")
	public void deleteProperty(@PathVariable("id") Integer siteId, 
			@PathVariable("propName") String propName,
			HttpServletRequest request, HttpServletResponse response) {
		Site site = siteService.findById(siteId);
		if (site == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		SiteProperty p = site.findPropertyByName(propName);		
		if (p != null) {
			site.getProperties().removeIf(sp -> sp.getName().equalsIgnoreCase(propName));
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		Site savedEntity = siteService.save(site);
		logger.info("SITE PROPERTY DELETE: " + savedEntity + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public void save(@PathVariable("id") Integer id, @RequestBody Site site, HttpServletRequest request, HttpServletResponse response) {
		Site existingEntity = siteService.findById(id);
		
		// merge data
		existingEntity.setDomain(site.getDomain());
		existingEntity.setTitle(site.getTitle());
		existingEntity.setName(site.getName());
		existingEntity.setHomePage(site.getHomePage());
		
		Site savedEntity = siteService.save(existingEntity);
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
