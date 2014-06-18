package net.pechorina.kontempl.view;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.pechorina.kontempl.data.DocFile;
import net.pechorina.kontempl.data.GenericTreeNode;
import net.pechorina.kontempl.data.ImageFile;
import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.data.PageTree;
import net.pechorina.kontempl.data.Site;
import net.pechorina.kontempl.service.DocFileService;
import net.pechorina.kontempl.service.ImageFileService;
import net.pechorina.kontempl.service.PageNavigationService;
import net.pechorina.kontempl.service.PageService;
import net.pechorina.kontempl.service.PageTreeService;
import net.pechorina.kontempl.service.SiteService;
import net.pechorina.kontempl.utils.StringUtils;
import net.pechorina.kontempl.view.forms.PageForm;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/page")
public class PageController extends AbstractController {
	static final Logger logger = LoggerFactory.getLogger(PageController.class);
    
	@Autowired
    private PageService pageService;    
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private PageTreeService pageTreeService;
	
	@Autowired
	private PageNavigationService pageNavService;
	
	@Autowired
	private ImageFileService imageFileService;
	
	@Autowired
	private DocFileService docFileService;
    
    @RequestMapping(value="/{pageId}/add-child", method=RequestMethod.GET)
    public String pageAddChildOld(@PathVariable("pageId") Integer parentId, Model model) {    
    	if (parentId == null) { parentId = 0; }
    	
    	return "redirect:/page/add?parentId=" + parentId;
    }
    
    @RequestMapping(value="/add", method=RequestMethod.GET)
    public String pageAddChild(@RequestParam(value="parentId", required=false) Integer parentId,
    		@RequestParam(value="site", required=true) String siteName,
    		Model model) {    
    	Site site = siteService.findByNameCached(siteName);
    	if (parentId == null) { parentId = 0; }
        PageForm newPage = new PageForm();
        newPage.setAutoName(true);
       	newPage.setParentId(parentId);
       	Page parent = pageService.getPage(parentId);

        int sortindex = pageService.getMaxSortindex(parent);
        sortindex += 10;
        newPage.setSortindex(sortindex);
        model.addAttribute("pagenode", newPage);
        model.addAttribute("parents", getParentsMap(site));
        
        return "commons/pageedit";        
    }
    
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String pageAddChild(@ModelAttribute PageForm pageform, BindingResult result, HttpSession session, Model model) {
        
        logger.debug("pageEditSubmit:: pageform=" + pageform);
        
        Page page = new Page();

        page.setCreated(new DateTime());
        page.setUpdated(new DateTime());
        page.setParentId(pageform.getParentId());
        
        page.setTitle(pageform.getTitle());
        page.setDescription(pageform.getDescription());
        page.setBody(pageform.getBody());
        page.setTags(pageform.getTags());
        page.setName(pageform.getName());
        page.setParentId(pageform.getParentId());
        page.setPublicPage(pageform.isPublicPage());
        page.setSortindex(pageform.getSortindex());
        page.setHideTitle(pageform.isHideTitle());
        page.setPlaceholder(pageform.isPlaceholder());

        page.checkName();
        pageService.savePage(page);
        
        model.addAttribute("pagenode", page);
        
        return "redirect:/page/edit/" + page.getId();
    } 
    
    @RequestMapping(value="/{pageId}/edit", method=RequestMethod.GET)
    public String pageEdit(@PathVariable("pageId") Integer pageId, Model model) {    
        logger.debug("pageEdit:: pageId=" + pageId);
        Page p = pageService.getPage(pageId);
        Site s = p.getSite();
        PageForm pageform = new PageForm(p);
        if (p != null) {
            logger.debug("Page:" + p);
        }
        
        model.addAttribute("pagenode", pageform);
        model.addAttribute("pagedata", p);
        int imagesNum = 0;
        int filesNum = 0;
        
        if (pageId != null && pageId != 0) {
        	List<ImageFile> images = imageFileService.listImagesForPageOrdered(pageId);
        	model.addAttribute("pagenodeImages", images);
        	if (images != null) {
        		imagesNum = images.size();
        	}
        	List<DocFile> files = docFileService.listDocsForPageOrdered(pageId);
        	model.addAttribute("pagenodeFiles", files);
        	if (files != null) {
        		filesNum = files.size();
        	}
        }
        model.addAttribute("imagesNum", imagesNum);
        model.addAttribute("filesNum", filesNum);
        model.addAttribute("parents", getParentsMap(s));
        
        return "commons/pageedit";
    }
    
    @RequestMapping(value="/edit/{pageId}", method=RequestMethod.POST)
    public String pageEditSubmit(@PathVariable("pageId") Integer pageId, 
            @ModelAttribute PageForm pageform, BindingResult result, HttpSession session, Model model) {
        
        logger.debug("pageId: " + pageId);
        logger.debug("pageEditSubmit:: pageform=" + pageform);
        
        Page page = new Page();
        if (pageId > 0) {
            page = pageService.getPage(pageId);
            logger.debug("loaded page from DB: id=" + pageId);
        } else {
            // creating a new page
            page.setCreated(new DateTime());
        }
        
        page.setUpdated(new DateTime());
        
        page.setTitle(pageform.getTitle());
        page.setDescription(pageform.getDescription());
        page.setBody(pageform.getBody());
        page.setTags(pageform.getTags());
        page.setName(pageform.getName());
        page.setParentId(pageform.getParentId());
        page.setPublicPage(pageform.isPublicPage());
        page.setSortindex(pageform.getSortindex());
        page.setHideTitle(pageform.isHideTitle());
        page.setPlaceholder(pageform.isPlaceholder());

        page.checkName();
        pageService.savePage(page);
        
        model.addAttribute("pagenode", page);
        
        return "redirect:/page/edit/" + page.getId();
    }    
    
    @RequestMapping(value="/tree/{site}", method=RequestMethod.GET)
    public String pageTree(@PathVariable("site") String siteName, Model model) {    
        logger.debug("pageTree method called, site=" + siteName);
        Site s = siteService.findByNameCached(siteName);
        PageTree t = pageTreeService.getPageTree(s);

        model.addAttribute("pagetree", t);
        return "commons/pagetree";
    } 
    
    @RequestMapping(value="/delete/{pageId}")
    public String pageDelete(@PathVariable("pageId") Integer pageId) {    
        logger.debug("delete page, pageId=" + pageId);
        Page p = pageService.getPage(pageId);
        if (p != null) {
            pageService.deletePage(p);
        }
        return "redirect:/page/tree";
    }
    
    @RequestMapping(value="/asyncdelete/{pageId}")
    public @ResponseBody String pageDeleteAsync(@PathVariable("pageId") Integer pageId) {    
        logger.debug("delete page, pageId=" + pageId);
        Page p = pageService.getPage(pageId);
        if (p != null) {
            pageService.deletePage(p);
        }
        return "OK";
    }

    
    @RequestMapping(value="/imgaes/{pageId}", method=RequestMethod.GET)
    public String getPageImages(@PathVariable("pageId") Integer pageId, Model model) {    

    	List<ImageFile> images = imageFileService.listImagesForPageOrdered(pageId);
    	model.addAttribute("pagenodeImages", images);
    	
    	return "commons/pageimages2";
    }
    
    @RequestMapping(value="/files/{pageId}", method=RequestMethod.GET)
    public String getPageFiles(@PathVariable("pageId") Integer pageId, Model model) {    

    	List<DocFile> images = docFileService.listDocsForPageOrdered(pageId);
    	model.addAttribute("pagenodeFiles", images);
    	
    	return "commons/pagefiles";
    }
    
    @RequestMapping(value="/move/{pageId}")
    public String pageMove(@PathVariable("pageId") Integer pageId, @RequestParam(value="direction", required=true) String dir) {    
        logger.debug("move page, pageId=" + pageId + " - " + dir);
        Page p = pageService.getPage(pageId);
        pageService.movePage(p, dir);
        return "redirect:/page/tree";
    }
    
    @RequestMapping(value="/copy/{pageId}")
    public String pageCopy(@PathVariable("pageId") Integer pageId) {    
        logger.debug("copy page, pageId=" + pageId);
        Page p = pageService.getPage(pageId);
        Page newPage = pageService.copyPage(p);
        return "redirect:/page/edit/" + newPage.getId();
    }
    
    @RequestMapping(value="/titleToName")
    public @ResponseBody String titleToName(@RequestParam(value="title", required=true) String title, Model model) {
    	return StringUtils.clearPageName(title);
    }
    
	private Map<String,String> getParentsMap(Site site) {
		Map<String,String> map = new LinkedHashMap<String, String>();
		map.put("0", "ROOT");
		PageTree t = pageTreeService.getPageTree(site);
		int level = 0;
		for(GenericTreeNode<Page> node: t.getChildren()) {
			map.put(node.getData().getId().toString(), node.getData().getName());
			auxAddChildrenToTheParentMap(node, map, level);
		}
				
        return map;
	}
	
	private void auxAddChildrenToTheParentMap(GenericTreeNode<Page> parent, Map<String,String> map, int level) {
		level++;
		String prefix = "";
		for(int i =0; i < level; i++) {
			prefix += "..";
		}
		if (parent.hasChildren()) {
			for(GenericTreeNode<Page> node: parent.getChildren()) {
				String n = prefix + node.getData().getName();
				map.put(node.getData().getId().toString(), n);
				auxAddChildrenToTheParentMap(node, map, level);
			}
		}
	}
    
}
