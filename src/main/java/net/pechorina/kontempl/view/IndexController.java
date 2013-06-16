package net.pechorina.kontempl.view;

import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.service.PageNavigationService;
import net.pechorina.kontempl.service.PageService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController extends AbstractController {
    private static final Logger logger = Logger.getLogger(IndexController.class);
    
	@Autowired
    private PageService pageService;
	
	@Autowired
	private PageNavigationService pageNavService;
	
    @RequestMapping(value="/do/index")
    public String indexPage(Model model) {    
        logger.debug("show index page");
        String pageName = appConfig.getProperty("homePage");
        Page p = pageService.getPageWithInheritedElements(pageName);
        model.addAttribute("txt", p);
    	return "pages/pageindex";
    }

}
