package net.pechorina.kontempl.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.data.PageProperty;
import net.pechorina.kontempl.service.PageService;
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
@RequestMapping(value = "/api/pages/{pageId}/properties")
public class PagePropertyResource {
	static final Logger logger = LoggerFactory.getLogger(PagePropertyResource.class);
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private PageService pageService;
	
	@Autowired
	private Environment env;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<PageProperty> list(@PathVariable("pageId") Integer pageId) {
		Page p = pageService.getPage(pageId);
		return p.getProperties();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public PageProperty getById(@PathVariable("pageId") Integer pageId, @PathVariable("id") Integer id) {
		Page p = pageService.getPage(pageId);
		return p.findPropertyById(id);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Page> createPageProperty(@PathVariable("pageId") Integer pageId, 
			@RequestBody PageProperty pageProperty, HttpServletRequest request) {
		Page p = pageService.getPage(pageId);
		//pageProperty.setPage(p);
		p.addProperty(pageProperty);
		Page savedEntity = pageService.savePage(p);
		logger.info("PAGE PROPERTY CREATE: " + savedEntity + " Src:" + request.getRemoteAddr());
		return new ResponseEntity<Page>(savedEntity, HttpStatus.CREATED);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public void saveProperty(@PathVariable("pageId") Integer pageId, 
			@PathVariable("id") Integer propertyId,
			@RequestBody PageProperty pageProperty, 
			HttpServletRequest request, HttpServletResponse response) {
		Page p = pageService.getPage(pageId);
		
		// merge updates
		PageProperty existingProperty = p.findPropertyById(propertyId);
		if (existingProperty != null) {
			existingProperty.setName(pageProperty.getName().trim());
			existingProperty.setContent(pageProperty.getContent().trim());
		}

		Page savedEntity = pageService.savePage(p);
		logger.info("PAGE PROPERTY SAVE: " + savedEntity + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void deleteProperty(@PathVariable("pageId") Integer pageId, 
			@PathVariable("id") Integer id,
			HttpServletRequest request, HttpServletResponse response) {
		Page p = pageService.getPage(pageId);
		if (p == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		PageProperty prop = p.findPropertyById(id);		
		if (prop != null) {
			p.getProperties().removeIf(sp -> (sp.getId() == id));
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		Page savedEntity = pageService.savePage(p);
		logger.info("PAGE PROPERTY DELETE: " + savedEntity + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
