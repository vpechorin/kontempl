package net.pechorina.kontempl.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.pechorina.kontempl.data.ImageFile;
import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.data.PageElement;
import net.pechorina.kontempl.repos.PageElementRepo;
import net.pechorina.kontempl.repos.PageRepo;
import net.pechorina.kontempl.utils.StringUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("pageService")
public class PageService {
	private static final Logger logger = Logger.getLogger(PageService.class);

	@Autowired
	private PageRepo pageRepo;
	
    @Autowired
    private PageElementRepo pageElementRepo;
    
    @Autowired
    private ImageFileService imageFileService;
    
	@Autowired
	@Qualifier("appConfig")
	public java.util.Properties appConfig;

	@Transactional
	public Page getPage(Integer id) {
		logger.debug("getPage(" + id + ")");
		return pageRepo.findOne(id);
	}

	@Transactional
	public Page getPage(String name) {
		logger.debug("getPage(" + name + ")");
		return pageRepo.findByName(name);
	}

	@Transactional
	public Page getPageWithElements(String name) {
		logger.debug("getPageWithElements(" + name + ")");
		Page p = pageRepo.findByName(name);
		if (p != null) {
			retrievePageElements(p);
		}
		
		return p;
	}
	
	private void retrievePageElements(Page p) {
		Map<String, PageElement> m = getPageElementsMap(p.getId());
		if (m != null && m.size() > 0) {
			p.setPageElements(m);
		}
	}
	
	private Map<String, PageElement> getPageElementsMap(Integer pageId) {
		List<PageElement> elements = listPageElements(pageId);
		Map<String, PageElement> m = new HashMap<String, PageElement>();
		if (elements != null) {
			for(PageElement pe: elements) {
				m.put(pe.getName(), pe);
			}
		}
		return m;
	}
	
	@Transactional
	public List<PageElement> listPageElements(Integer pageId) {
		return pageElementRepo.listPageElements(pageId);
	}
	
	@Transactional
	public Map<String, PageElement> listInheritedPageElements(Page page) {
		List<Page> parents = listPageParents(page);
		parents.add(page);

		Map<String, PageElement> pageElements = new HashMap<String, PageElement>();

		for (Page parent : parents) {
			Map<String, PageElement> parentPageElements = getPageElementsMap(parent.getId());
			// logger.debug("Parent: " + parent);
			// logger.debug("elements: " + parentPageElements);
			if (parentPageElements != null) {
				parent.setPageElements(parentPageElements);
				for (String key : parentPageElements.keySet()) {
//					logger.debug("Processing key: " + key + " of page: "
//							+ parent.getId() + " " + parent.getName());
					pageElements.put(key, parentPageElements.get(key));
				}
			}
		}

		return pageElements;
	}

	@Transactional
	public Page getPageWithElements(Integer id) {
		logger.debug("getPageWithElements(" + id + ")");
		Page p = pageRepo.findOne(id);
		if (p != null) {
			retrievePageElements(p);
		}
		
		return p;
	}
	
	@Transactional
	public List<Page> listPageParents(Page page) {
		List<Page> pages = new ArrayList<Page>();
		Page thisPage = page;
		while (thisPage.getParentId() != 0) {
			Page parent = pageRepo.findOne(thisPage.getParentId());
			pages.add(parent);
			thisPage = parent;
		}
		
		Collections.reverse(pages);

		return pages;
	}

	@Transactional
	public Page getPageWithInheritedElements(Integer id) {
		logger.debug("getPageWithElements(" + id + ")");
		Page p = pageRepo.findOne(id);
		if (p != null) {
			Map<String, PageElement> m = listInheritedPageElements(p);
			if (m != null) {
				p.setPageElements(m);
			}
		}

		return p;
	}

	@Transactional
	@Cacheable("pageWithElementsCache")
	public Page getPageWithInheritedElements(String pageName) {
		logger.debug("getPageWithElements(" + pageName + ")");
		Page p = pageRepo.findByName(pageName);
		if (p != null) {
			Map<String, PageElement> m = listInheritedPageElements(p);
			if (m != null) {
				p.setPageElements(m);
			}
			
			ImageFile im = imageFileService.getMainImageForPage(p.getId());
			logger.debug("ImageFile: " + im);
			
			if (im != null) {
				p.setMainImage(im);
				logger.debug("Main image retrieved and set");
			}

		}

		return p;
	}

	@Transactional
	@CacheEvict(value = {"publicPageTreeCache", "pageWithElementsCache"}, allEntries = true)
	public void savePage(Page page) {
		pageRepo.save(page);
	}

	@Transactional
	@CacheEvict(value = {"publicPageTreeCache", "pageWithElementsCache"}, allEntries = true)
	public void deletePage(Page page) {
		deleteSubPages(page.getId());
		deletePageWithElements(page);
	}
	
	@Transactional
	public void deleteSubPages(Integer parentId) {
		List<Page> pages = pageRepo.listSubPages(parentId);
		for(Page p: pages) {
			deleteSubPages(p.getId());
			deletePageWithElements(p);
			logger.debug("Page: " + p.getId() + "/" + p.getName() + " removed");
		}
	}
	
	@Transactional
	public void deletePageWithElements(Page p) {
		// remove page elements
		List<PageElement> elements = listPageElements(p.getId());
		for(PageElement pe: elements) {
			pageElementRepo.delete(pe);
		}
		
		// remove images
		imageFileService.removeAllImagesForPage(p.getId());
		
		pageRepo.delete(p);
	}

	public Page beforeSave(Page page) {
		if (page.getName().isEmpty()) {
			String pl = StringUtils.convertNameToPath(page.getTitle());
			page.setName(pl);
		}
		return page;
	}

	@Transactional
	public List<Page> listRootPages() {
		List<Page> l = pageRepo.listSubPages(0);

		return l;
	}

	@Transactional
	public int getMaxSortindex(Integer parentId) {
		int si = 0;
		List<Page> l = pageRepo.listSubPages(parentId);
		if (l != null && l.size() > 0) {
			Page lastPage = l.get( l.size() - 1 );
			if (lastPage != null) {
				si = lastPage.getSortindex();
			}
		}

		return si;
	}
	
	@Transactional
	@CacheEvict(value = {"publicPageTreeCache", "pageWithElementsCache"}, allEntries = true)
	public void movePage(Integer pageId, String direction) {
		Page p = getPage(pageId);
		if (p == null) return;
		
		
		List<Page> pages = pageRepo.listSubPages(p.getParentId());
		
		int idx = pages.indexOf(p);
		
		int newIdx = 0;
		if (direction.equalsIgnoreCase("UP")) {
			// moving element up the list
			if (idx > 0) {
				newIdx = idx -1;
			}
		}
		else {
			// moving element down the list
			if (idx < (pages.size() - 1)) {
				newIdx = idx + 1;
			}
			else {
				newIdx = idx;
			}
		}
		logger.debug("Old index: " + idx + " New index: " + newIdx + " Total elements: " + pages.size());
		
		Page element = pages.remove(idx);
		pages.add(newIdx, element);
		
		// renumber all elements
		int i = 10;
		for(Page page: pages) {
			page.setSortindex(i);
			pageRepo.save(page);
			i += 10;
		}
	}
	
	@Transactional
	@CacheEvict(value = {"publicPageTreeCache", "pageWithElementsCache"}, allEntries = true)
	public Page copyPage(Page src) {
		Page newPage = src.copy();
		String newName = findNewPageName(src.getName());
		newPage.setName(newName);
		String newTitle = src.getTitle() + " - Copy";
		newPage.setTitle(newTitle);
		int sortIndex = getMaxSortindex(src.getParentId()) + 10;
		newPage.setSortindex(sortIndex);
		Page savedNewPage = pageRepo.saveAndFlush(newPage); 
		
		// copy page elements
		List<PageElement> elements = pageElementRepo.listPageElements(src.getId());
		if (elements != null && (elements.size() > 0)) {
			Map<String, PageElement> m = new HashMap<String, PageElement>();
			for(PageElement el: elements) {
				PageElement newEl = el.copy();
				newEl.setPageId(savedNewPage.getId());
				PageElement savedPageEl = pageElementRepo.saveAndFlush(newEl);
				m.put(savedPageEl.getName(), savedPageEl);
			}
			savedNewPage.setPageElements(m);
		}
		
		// copy page images
		List<ImageFile> images = imageFileService.listImagesForPage(src.getId());
		if (images != null &&(images.size() > 0)) {
			for(ImageFile im: images) {
				imageFileService.copyFileToPage(im, savedNewPage.getId());
			}
		}
		
		return savedNewPage;
	}
	
	@Transactional
	public String findNewPageName(String name) {
		int idx = 1;
		boolean run = true;
		String newName = name + "-" + idx;
		
		while(run) {
			long c = pageRepo.countPagesForName(newName);
			if (c > 0) {
				idx++;
				newName = name + "-" + idx;
			}
			else {
				run = false;
			}
		}
			
		return newName;
	}
	
	@CacheEvict(value = {"publicPageTreeCache", "pageWithElementsCache"}, allEntries = true)
	public void resetPageCache() {
		
	}
}
