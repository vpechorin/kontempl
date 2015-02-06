package net.pechorina.kontempl.rest;

import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.data.Site;
import net.pechorina.kontempl.service.PageService;
import net.pechorina.kontempl.service.PageTreeService;
import net.pechorina.kontempl.service.SiteService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/pages")
public class PageResource {
	static final Logger logger = LoggerFactory.getLogger(PageResource.class);
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private PageService pageService;

	@Autowired
	private PageTreeService pageTreeService;
	
	@Autowired
	private Environment env;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Page> list(@RequestParam(value="siteId", required=true) Integer siteId) {
		Site site = siteService.findById(siteId);
        return pageService.listBySite(site);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public Page getById(@PathVariable("id") Integer id) {
		return pageService.getPage(id);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/namecheck")
	public void nameCheck(@PathVariable("id") Integer id, 
			@RequestParam(value="name", required=true) String pageName,HttpServletResponse response ) {
		boolean badName = pageService.findOtherPagesWithSameName(id, pageName);
		if (badName) { response.setStatus(HttpServletResponse.SC_CONFLICT); }
		else  {response.setStatus(HttpServletResponse.SC_OK);}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/name/{name}")
	public Page getByName(@PathVariable("name") String name, @RequestParam(value="siteName", required=true) String siteName) {
		Site site = siteService.findByName(siteName);
		return pageService.getPage(site, name);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Page> create(@RequestBody Page page, HttpServletRequest request, HttpServletResponse response) {
		Site site = siteService.findById(page.getSiteId());
		if (site == null) {
			logger.error("No site id found for the new page");
			return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
		}
		Page newPage = new Page();
		newPage.setSite(site);

		// merge data
		newPage.setBody(page.getBody());
		newPage.setTitle(page.getTitle());
		newPage.setHtmlTitle(page.getHtmlTitle());
		newPage.setName(page.getName());
		newPage.setDescription(page.getDescription());
		newPage.setHideTitle(page.isHideTitle());
		newPage.setParentId(page.getParentId());
		newPage.setPlaceholder(page.isPlaceholder());
		newPage.setPublicPage(page.isPublicPage());
		newPage.setAutoName(page.isAutoName());
		newPage.setRichText(page.isRichText());
		newPage.setSortindex(page.getSortindex());
		newPage.setTags(page.getTags());
		newPage.setIncludeForm(page.getIncludeForm());
		newPage.setFormId(page.getFormId());
		
		Page savedEntity = pageService.savePage(newPage);
		
		pageService.resetPageCache();
		
		logger.info("PAGE SAVE: " + savedEntity + " Src:" + request.getRemoteAddr());
		return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public void save(@PathVariable("id") Integer id, @RequestBody Page page, HttpServletRequest request, HttpServletResponse response) {
		Page existingEntity = pageService.getPage(id);

		// merge data
		existingEntity.setBody(page.getBody());
		existingEntity.setTitle(page.getTitle());
		existingEntity.setHtmlTitle(page.getHtmlTitle());
		existingEntity.setName(page.getName());
		existingEntity.setDescription(page.getDescription());
		existingEntity.setHideTitle(page.isHideTitle());
		existingEntity.setParentId(page.getParentId());
		existingEntity.setPlaceholder(page.isPlaceholder());
		existingEntity.setPublicPage(page.isPublicPage());
		existingEntity.setAutoName(page.isAutoName());
		existingEntity.setRichText(page.isRichText());		
		// existingEntity.setSortindex(page.getSortindex());
		existingEntity.setTags(page.getTags());
		existingEntity.setUpdated(new DateTime());
		existingEntity.setIncludeForm(page.getIncludeForm());
		existingEntity.setFormId(page.getFormId());
		
		Page savedEntity = pageService.savePage(existingEntity);
		logger.info("PAGE SAVE: " + savedEntity + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/{id}/move")
	public void save(@PathVariable("id") Integer id, @RequestBody Map<String,String> movement, HttpServletRequest request, HttpServletResponse response) {
		Page page = pageService.getPage(id);
		
		logger.debug("Movement: " + movement);
		
		int dstIndex = Integer.parseInt(movement.get("dstIndex"));
		int dstParentId = Integer.parseInt(movement.get("dstParentId"));
		pageService.movePage(page, dstIndex, dstParentId);
		
		pageService.resetPageCache();
		
		logger.info("PAGE MOVED: " + movement + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public void remove(@PathVariable("id") Integer id, HttpServletRequest request, HttpServletResponse response) {
		pageService.deletePage(id);
		
		pageService.resetPageCache();
		
		logger.info("PAGE DELETE: " + id + " Src:" + request.getRemoteAddr());
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
