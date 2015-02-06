package net.pechorina.kontempl.rest;

import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.data.PageNode;
import net.pechorina.kontempl.data.Site;
import net.pechorina.kontempl.service.PageService;
import net.pechorina.kontempl.service.PageTreeService;
import net.pechorina.kontempl.service.SiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SiteBrowseResource {
	static final Logger logger = LoggerFactory.getLogger(SiteBrowseResource.class);
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private PageService pageService;

	@Autowired
	private PageTreeService pageTreeService;
	
	@Autowired
	private Environment env;
	
	@RequestMapping(method = RequestMethod.GET, value="/api/browse/sites/{siteName}")
	public ResponseEntity<Site> getSiteByName(@PathVariable("siteName") String siteName) {
		Site s =  siteService.findByNameCached(siteName);
		return new ResponseEntity<>(s, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/api/browse/sites/{siteName}/pages/{pageName}")
	public ResponseEntity<Page> getPageByName(@PathVariable("siteName") String siteName, 
			@PathVariable("pageName") String pageName) {
		Site s =  siteService.findByNameCached(siteName);
		Page p = pageService.getPageCached(s, pageName);
		return new ResponseEntity<>(p, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/api/browse/sites/{siteName}/pages/{pageName}/children")
	public List<Page> getPageChildrenByName(@PathVariable("siteName") String siteName, 
			@PathVariable("pageName") String pageName) {
		Site s =  siteService.findByNameCached(siteName);
		Page p = pageService.getPageCached(s, pageName);
        return pageService.listPageChildren(p);
	}
	
	@RequestMapping(method = RequestMethod.GET, value="/api/browse/sites/{siteName}/tree")
	public List<PageNode> getPageTree(@PathVariable("siteName") String siteName, 
			@RequestParam(value="files", required=false) boolean includeFiles,
			@RequestParam(value="images", required=false) boolean includeImages) {
		logger.debug("Site tree: " + siteName + ", images:" + includeImages + ", files:" + includeFiles);
		Site s =  siteService.findByNameCached(siteName);
        return pageTreeService.getPageNodeTreePublic(s, includeImages, includeFiles);
	}
}
