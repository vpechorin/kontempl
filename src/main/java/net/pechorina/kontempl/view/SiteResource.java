package net.pechorina.kontempl.view;

import java.util.List;

import net.pechorina.kontempl.data.Site;
import net.pechorina.kontempl.service.SiteService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/sites")
public class SiteResource extends AbstractController {
	static final Logger logger = LoggerFactory.getLogger(SiteResource.class);
	
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
}
