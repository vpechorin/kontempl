package net.pechorina.kontempl.view;

import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.service.PageService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DefaultController {
	private static final Logger logger = Logger.getLogger(DefaultController.class);
	
    @Autowired
    @Qualifier("appConfig")
    public java.util.Properties appConfig;
    
	@Autowired
    private PageService pageService; 
    
    @RequestMapping(value="/robots.txt", method=RequestMethod.GET, produces="text/plain")
    @ResponseBody
    public String robotsPage() {
    	logger.debug("show robots.txt page");
    	String responseBody = "";
    	Page p = pageService.getPage("robotstxt");
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
