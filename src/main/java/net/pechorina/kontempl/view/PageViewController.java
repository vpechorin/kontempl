package net.pechorina.kontempl.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.pechorina.kontempl.data.ImageFile;
import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.exceptions.PageNotFoundException;
import net.pechorina.kontempl.exceptions.SiteNotFoundException;
import net.pechorina.kontempl.service.ImageFileService;
import net.pechorina.kontempl.service.PageNavigationService;
import net.pechorina.kontempl.service.PageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
public class PageViewController extends AbstractController {
	static final Logger logger = LoggerFactory.getLogger(PageViewController.class);
    
	@Autowired
    private PageService pageService;
	
	@Autowired
	private PageNavigationService pageNavService;
	
	@Autowired
	private ImageFileService imageFileService;
	
    @RequestMapping(value="/v/{site}/index.html")
    public String homePage(@PathVariable("site") String siteName, Model model) throws PageNotFoundException, SiteNotFoundException {    
        logger.debug("show home page");
        String pageName = appConfig.getProperty("homePage");
        
        Page p = pageService.getPageCached(siteName, pageName);
        
        if (p == null) {
        	throw new PageNotFoundException("No home page configured");
        }
        
        model.addAttribute("pageName", pageName);
        model.addAttribute("txt", p);
    	model.addAttribute("pagenodeImages", imageFileService.listImagesForPageOrdered(p.getId()));
    	model.addAttribute("ogproperties", getOgProperties(p));
        
    	return "pages/pageview";
    }
	
    @RequestMapping(value="/v/{site}/p/{pageName}")
    public String pageView(WebRequest webRequest, 
    			@PathVariable("site") String siteName,
                @PathVariable("pageName") String pageName, Model model) throws PageNotFoundException {   
        logger.debug("webRequest: " + webRequest.getDescription(true));
        
        Page p = pageService.getPageCached(siteName, pageName);
        
        if (p == null) {
            logger.warn("Page not found: /" + pageName + "/ webrq: " + webRequest.getDescription(true));
            throw new PageNotFoundException("Page not found: /" + pageName + "/ webrq: " + webRequest.getDescription(true));
        }
        
        List<Page> bc = pageNavService.getBreadcrumbs(p);
        model.addAttribute("bclist", bc);
        		
        model.addAttribute("pageName", pageName);
        model.addAttribute("txt", p);
        model.addAttribute("pagenodeImages", imageFileService.listImagesForPageOrdered(p.getId()));
        model.addAttribute("ogproperties", getOgProperties(p));
        
        webRequest.setAttribute("last-modified", p.lastModified(), WebRequest.SCOPE_REQUEST);
        
        return "pages/pageview";
    }    
    
    private Map<String,String> getOgProperties(Page p) {
    	Map<String,String> ogProperties = new HashMap<String, String>();
    	if (p.getTitle() != null && (p.getTitle().trim().length()> 0)) {
    		ogProperties.put("title", p.getTitle());
    	}
    	
    	String desc = p.getTextDescription(Integer.parseInt( appConfig.getProperty("ogDescriptionLength") ) );
    	if (desc.length() > 0) {
    		ogProperties.put("description", desc);
    	}
    	
    	// "sitename":cmslib.getPageElementContent(txt, "sitename")
    	if (p.getProperties() != null) {
    		if( p.getProperties().containsKey("sitename") ) {
    			ogProperties.put("sitename", p.getProperties().get("sitename").getContent() );
    		}
    	}
    	
    	if (p.getMainImage() != null) {
    		if (p.getMainImage().getThumb() != null) {
    			String thumbUrl = "http://" + appConfig.getProperty("domainname") + appConfig.getProperty("fileStorageUrl") + p.getMainImage().getThumb().getAbsolutePath();
    			ogProperties.put("iconUrl", thumbUrl);
    		}
    	}
    	
    	return ogProperties;
    }
    
    @RequestMapping(value="/v/{site}/p/{pageName}/agileImages")
    public @ResponseBody ArrayList<HashMap<String, String>> agileCarouselData(WebRequest webRequest, 
    		@PathVariable("site") String siteName,
            @PathVariable("pageName") String pageName, Model model) throws PageNotFoundException {
    	Page p = pageService.getPageCached(siteName, pageName);
    	ArrayList<HashMap<String, String>> l = new ArrayList<HashMap<String, String>>();
    	List<ImageFile> images = imageFileService.listImagesForPageOrdered(p.getId());
    	for(ImageFile img: images) {
    		HashMap<String,String> m = new HashMap<String, String>();
    		m.put("content", "<div class='slide_inner'><a class='photo_link' href='#'><img class=photo src='" + appConfig.getProperty("fileStorageUrl") + img.getAbsolutePath() + "'></a></div>");
    		m.put("content_button", "<div class='thumb'><img src='" + appConfig.getProperty("fileStorageUrl") + img.getThumb().getAbsolutePath() + "'></div>");
    		l.add(m);
    	}
    	return l;
    }

}
