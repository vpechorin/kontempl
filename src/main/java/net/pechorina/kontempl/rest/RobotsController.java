package net.pechorina.kontempl.rest;

import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.data.Site;
import net.pechorina.kontempl.service.PageService;
import net.pechorina.kontempl.service.SiteService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RobotsController {
	static final Logger logger = LoggerFactory.getLogger(RobotsController.class);
	
    @Autowired
    private Environment env;
    
	@Autowired
    private PageService pageService; 
	
	@Autowired
	private SiteService siteService;
    
    @RequestMapping(value="/v/{site}/robots.txt", method=RequestMethod.GET, produces="text/plain")
    @ResponseBody
    public String robotsPage(@PathVariable("site") String siteName) {
    	logger.debug("show robots.txt page");
    	Site s =  siteService.findByNameCached(siteName);
    	String responseBody = "";
    	Page p = pageService.getPageCached(s, "robotstxt");
    	if (p != null) {
    		responseBody = p.getBody();
    	}
    	else {
    		responseBody = "User-agent: *\n";
    		responseBody += "Disallow: /security/login\n";
    	}
    	return responseBody;
    }

}
