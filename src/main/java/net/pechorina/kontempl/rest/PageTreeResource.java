package net.pechorina.kontempl.rest;

import java.util.List;

import net.pechorina.kontempl.data.PageNode;
import net.pechorina.kontempl.data.Site;
import net.pechorina.kontempl.service.PageService;
import net.pechorina.kontempl.service.PageTreeService;
import net.pechorina.kontempl.service.SiteService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/pagetrees")
public class PageTreeResource {
	static final Logger logger = LoggerFactory.getLogger(PageTreeResource.class);
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private PageService pageService;

	@Autowired
	private PageTreeService pageTreeService;
	
	@Autowired
	private Environment env;

	
	@RequestMapping(method=RequestMethod.GET, value="/{siteId}")
	public List<PageNode> tree(@PathVariable("siteId") Integer siteId) {
		Site site = siteService.findById(siteId);
		List<PageNode> tree = pageTreeService.getPageNodeTree(site);
		return tree;
	}
	
}
