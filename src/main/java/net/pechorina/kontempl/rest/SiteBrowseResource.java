package net.pechorina.kontempl.rest;

import java.util.List;

import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.data.PageNode;
import net.pechorina.kontempl.data.Site;
import net.pechorina.kontempl.service.PageService;
import net.pechorina.kontempl.service.PageTreeService;
import net.pechorina.kontempl.service.SiteService;
import net.pechorina.kontempl.view.AbstractController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SiteBrowseResource extends AbstractController {
	static final Logger logger = LoggerFactory.getLogger(SiteBrowseResource.class);
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private PageService pageService;

	@Autowired
	private PageTreeService pageTreeService;
	
	@RequestMapping(method = RequestMethod.GET, value="/api/browse/sites/{siteName}")
	public ResponseEntity<Site> getSiteByName(@PathVariable("siteName") String siteName) {
		Site s =  siteService.findByNameCached(siteName);
		return new ResponseEntity<Site>(s, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/api/browse/sites/{siteName}/pages//{pageName}")
	public ResponseEntity<Page> getPageByName(@PathVariable("siteName") String siteName, 
			@PathVariable("pageName") String pageName) {
		Site s =  siteService.findByNameCached(siteName);
		Page p = pageService.getPageCached(s, pageName);
		return new ResponseEntity<Page>(p, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/api/browse/sites/{siteName}/tree")
	public List<PageNode> getPageTree(@PathVariable("siteName") String siteName) {
		Site s =  siteService.findByNameCached(siteName);
		List<PageNode> tree = pageTreeService.getPageNodeTreePublic(s);
		return tree;
	}
}
