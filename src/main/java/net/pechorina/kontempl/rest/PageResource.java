package net.pechorina.kontempl.rest;

import java.util.List;

import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.data.Site;
import net.pechorina.kontempl.service.PageService;
import net.pechorina.kontempl.service.SiteService;
import net.pechorina.kontempl.view.AbstractController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/pages")
public class PageResource extends AbstractController {
	static final Logger logger = LoggerFactory.getLogger(PageResource.class);
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private PageService pageService;
	
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Page> list() {
		return pageService.listAll();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public Page getById(@PathVariable("id") Integer id) {
		return pageService.getPage(id);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/name/{name}")
	public Page getByName(@PathVariable("name") String name, @RequestParam(value="siteName", required=true) String siteName) {
		Site site = siteService.findByName(siteName);
		return pageService.getPage(site, name);
	}
}
