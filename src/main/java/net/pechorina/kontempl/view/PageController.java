package net.pechorina.kontempl.view;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.pechorina.kontempl.data.GenericTreeNode;
import net.pechorina.kontempl.data.ImageFile;
import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.data.PageElement;
import net.pechorina.kontempl.data.PageTree;
import net.pechorina.kontempl.data.User;
import net.pechorina.kontempl.service.ImageFileService;
import net.pechorina.kontempl.service.PageElementService;
import net.pechorina.kontempl.service.PageElementTypeService;
import net.pechorina.kontempl.service.PageNavigationService;
import net.pechorina.kontempl.service.PageService;
import net.pechorina.kontempl.service.PageTreeService;
import net.pechorina.kontempl.utils.StringUtils;
import net.pechorina.kontempl.view.forms.PageForm;

import org.apache.log4j.Logger;
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
    private static final Logger logger = Logger.getLogger(PageController.class);
    
    @Autowired
    private PageElementService pageElementService;
    
	@Autowired
    private PageService pageService;    
	
	@Autowired
	private PageTreeService pageTreeService;
	
	@Autowired
	private PageNavigationService pageNavService;
	
	@Autowired
	private ImageFileService imageFileService;
	
	@Autowired
	private PageElementTypeService pageElementTypeService;
    
    @RequestMapping(value="/{pageId}/add-child", method=RequestMethod.GET)
    public String pageAddChildOld(@PathVariable("pageId") Integer parentId, Model model) {    
    	if (parentId == null) { parentId = 0; }
    	
    	return "redirect:/page/add?parentId=" + parentId;
    }
    
    @RequestMapping(value="/add", method=RequestMethod.GET)
    public String pageAddChild(@RequestParam(value="parentId", required=false) Integer parentId, Model model) {    
    	if (parentId == null) { parentId = 0; }
        PageForm newPage = new PageForm();
        newPage.setAutoName(true);
       	newPage.setParentId(parentId);

        int sortindex = pageService.getMaxSortindex(parentId);
        sortindex += 10;
        newPage.setSortindex(sortindex);
        model.addAttribute("pagenode", newPage);
        model.addAttribute("parents", getParentsMap());
        
        return "commons/pageedit";        
    }
    
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String pageAddChild(@ModelAttribute PageForm pageform, BindingResult result, HttpSession session, Model model) {
        
        logger.debug("pageEditSubmit:: pageform=" + pageform);

        User u = getUser();
        
        Page page = new Page();
        page.setUpdatedBy(u.getId());
        page.setCreated(new Date());
        page.setUpdated(new Date());
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
        
        return "redirect:/page/" + page.getId() +"/edit";
    } 
    
    @RequestMapping(value="/{pageId}/edit", method=RequestMethod.GET)
    public String pageEdit(@PathVariable("pageId") Integer pageId, Model model) {    
        logger.debug("pageEdit:: pageId=" + pageId);
        Page p = pageService.getPageWithElements(pageId);
        PageForm pageform = new PageForm(p);
        if (p != null) {
            logger.debug("Page:" + p);
        }
        
        model.addAttribute("pagenode", pageform);
        int imagesNum = 0;
        
        if (pageId != null && pageId != 0) {
        	List<ImageFile> images = imageFileService.listImagesForPageOrdered(pageId);
        	model.addAttribute("pagenodeImages", images);
        	if (images != null) {
        		imagesNum = images.size();
        	}
        }
        model.addAttribute("imagesNum", imagesNum);
        model.addAttribute("parents", getParentsMap());
        
        return "commons/pageedit";
    }
    
    @RequestMapping(value="/{pageId}/edit", method=RequestMethod.POST)
    public String pageEditSubmit(@PathVariable("pageId") Integer pageId, 
            @ModelAttribute PageForm pageform, BindingResult result, HttpSession session, Model model) {
        
        logger.debug("pageId: " + pageId);
        logger.debug("pageEditSubmit:: pageform=" + pageform);

        User u = getUser();
        
        Page page = new Page();
        if (pageId > 0) {
            page = pageService.getPage(pageId);
            logger.debug("loaded page from DB: id=" + pageId);
        } else {
            // creating a new page
            page.setCreated(new Date());
        }
        
        page.setUpdated(new Date());
        page.setUpdatedBy(u.getId());
        
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
        
        return "redirect:/page/" + page.getId() +"/edit";
    }    
    
    @RequestMapping(value="/tree", method=RequestMethod.GET)
    public String pageTree(Model model) {    
        logger.debug("pageTree method called");
        PageTree t = pageTreeService.getPageTree();

        model.addAttribute("pagetree", t);
        return "commons/pagetree";
    } 
    
    @RequestMapping(value="/{pageId}/delete")
    public String pageDelete(@PathVariable("pageId") Integer pageId) {    
        logger.debug("delete page, pageId=" + pageId);
        Page p = pageService.getPage(pageId);
        if (p != null) {
            pageService.deletePage(p);
        }
        return "redirect:/page/tree";
    }
    
    @RequestMapping(value="/{pageId}/asyncdelete")
    public @ResponseBody String pageDeleteAsync(@PathVariable("pageId") Integer pageId) {    
        logger.debug("delete page, pageId=" + pageId);
        Page p = pageService.getPage(pageId);
        if (p != null) {
            pageService.deletePage(p);
        }
        return "OK";
    }

    @RequestMapping(value="/{pageId}/add-element", method=RequestMethod.GET)
    public String pageElementAdd(@PathVariable("pageId") Integer pageId, Model model) {    
        logger.debug("pageElementAdd:: pageId=" + pageId);
        Page p = pageService.getPageWithElements(pageId);
        
        if (p != null) {
            logger.debug("Page:" + p);
        }
        
        Map<String,String> types = pageElementTypeService.getTypeMap();
        model.addAttribute("types", types);
        
        PageElement pe = new PageElement();
        pe.setPageId(pageId);
        model.addAttribute("pagenode", p);
        model.addAttribute("pageelement", pe);
        
        return "commons/pageeledit";
    } 
    
    @RequestMapping(value="/{pageId}/edit-element", method=RequestMethod.GET)
    public String pageElementEdit(@PathVariable("pageId") Integer pageId,
        @RequestParam(value="id", required=false) Integer pageElementId, Model model) {    
        logger.debug("pageElementEdit:: pageId=" + pageId + " pageElementId=" + pageElementId);

        Map<String,String> types = pageElementTypeService.getTypeMap();
        model.addAttribute("types", types);

        Page p = pageService.getPageWithElements(pageId);
        PageElement pe = new PageElement();
        if (pageElementId != null) {
            pe = pageElementService.getPageElementById(pageElementId);
        }
        
        model.addAttribute("pagenode", p);
        model.addAttribute("pageelement", pe);        
        return "commons/pageeledit";
    }    
    
    @RequestMapping(value="/{pageId}/edit-element", method=RequestMethod.POST)
    public String pageElementSubmit(@PathVariable("pageId") Integer pageId,
        @ModelAttribute PageElement pageElementForm, HttpSession session, Model model) {    
        logger.debug("pageElementSubmit:: pageId=" + pageId);
        logger.debug("Page element form" + pageElementForm);
    	
        User u = getUser();
        
        Map<String,String> types = pageElementTypeService.getTypeMap();
        model.addAttribute("types", types);
        
        PageElement pe = new PageElement();
        Integer pageElementId = pageElementForm.getId();
        if (pageElementId != null) {
        	if (pageElementId != 0) {
        		pe = pageElementService.getPageElementById(pageElementId);
        	}
        }
        
        if (pageElementForm.getName().equalsIgnoreCase("custom")) {
            pe.setName(pageElementForm.getCustomName());
        }
        else {
            pe.setName(pageElementForm.getName());
        }

        pe.setBody(pageElementForm.getBody());
        pe.setPageId(pageId);
        
        pe.setUpdated(new Date());
        pe.setUpdatedBy(u.getId());
        
        pageElementService.savePageElement(pe);
        
        return "redirect:/page/" + pageId +"/edit";
    }     
    
    @RequestMapping(value="/{pageId}/delete-element", method=RequestMethod.GET)
    public String pageElementDelete(@PathVariable("pageId") Integer pageId,
        @RequestParam(value="id", required=true) Integer pageElementId, Model model) {    
        logger.debug("pageElementDelete:: pageId=" + pageId + " pageElementId=" + pageElementId);
        PageElement pe = pageElementService.getPageElementById(pageElementId);
        if (pe != null) {
            pageElementService.deletePageElement(pe);
        }

        return "redirect:/page/{pageId}/edit";
    }    
    
    @RequestMapping(value="/{pageId}/images", method=RequestMethod.GET)
    public String getPageImages(@PathVariable("pageId") Integer pageId, Model model) {    

    	List<ImageFile> images = imageFileService.listImagesForPageOrdered(pageId);
    	model.addAttribute("pagenodeImages", images);
    	
    	return "commons/pageimages2";
    }
    
    @RequestMapping(value="/{pageId}/move")
    public String pageMove(@PathVariable("pageId") Integer pageId, @RequestParam(value="direction", required=true) String dir) {    
        logger.debug("move page, pageId=" + pageId + " - " + dir);
        pageService.movePage(pageId, dir);
        return "redirect:/page/tree";
    }
    
    @RequestMapping(value="/{pageId}/copy")
    public String pageCopy(@PathVariable("pageId") Integer pageId) {    
        logger.debug("copy page, pageId=" + pageId);
        Page p = pageService.getPageWithElements(pageId);
        Page newPage = pageService.copyPage(p);
        return "redirect:/page/" + newPage.getId() + "/edit";
    }
    
    @RequestMapping(value="/titleToName")
    public @ResponseBody String titleToName(@RequestParam(value="title", required=true) String title, Model model) {
    	return StringUtils.clearPageName(title);
    }
    
	private Map<String,String> getParentsMap() {
		Map<String,String> map = new LinkedHashMap<String, String>();
		map.put("0", "ROOT");
		PageTree t = pageTreeService.getPageTree();
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
