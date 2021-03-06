package net.pechorina.kontempl.service;

import net.pechorina.kontempl.data.DocFile;
import net.pechorina.kontempl.data.ImageFile;
import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.data.PageProperty;
import net.pechorina.kontempl.data.Site;
import net.pechorina.kontempl.repos.PageRepo;
import net.pechorina.kontempl.utils.CloneFactory;
import net.pechorina.kontempl.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PageService {
	static final Logger logger = LoggerFactory.getLogger(PageService.class);

	@Autowired
	private PageRepo pageRepo;
	
    @Autowired
    private ImageFileService imageFileService;

    @Autowired
    private DocFileService docFileService;
    
    @Autowired
    private PageTreeService pageTreeService;

	
	@Transactional
	public Page getPage(Integer id) {
		logger.debug("getPage(" + id + ")");
		Page p = pageRepo.findOne(id);
		fetchPageProperties(p);
		return p;
	}

	@Transactional
	public Page getPage(Site site, String name) {
		logger.debug("getPage(" + name + ")");
		Page p = pageRepo.findBySiteAndName(site, name);
		fetchPageProperties(p);
		return p;
	}
	
	private void fetchPageProperties(Page p) {
		if (p != null) {
			List<PageProperty> props = p.getProperties();
			if (props != null) props.size();
		}
	}
	
	@Transactional
	public List<Page> listPageParents(Page page) {
		List<Page> pages = new ArrayList<>();
		Page thisPage = page;
		while (thisPage.getParentId() != 0) {
			Page parent = pageRepo.findOne(thisPage.getParentId());
			fetchPageProperties(parent);
			pages.add(parent);
			thisPage = parent;
		}
		
		Collections.reverse(pages);

		return pages;
	}
	
	@Transactional
	@Cacheable("pageCache")
	public Page getPageCached(Site site, String pageName) {
		Page p = getPage(site, pageName);
		if (p != null) {
			
			ImageFile im = imageFileService.getMainImageForPage(p.getId());
			logger.debug("ImageFile: " + im);
			
			if (im != null) {
				p.setMainImage(im);
				logger.debug("Main image retrieved and set");
			}
			
			List<ImageFile> images = imageFileService.listImagesForPageOrdered(p.getId());
			if (images != null) p.setImages(images);
			
			List<DocFile> files =  docFileService.listDocsForPageOrdered(p.getId());
			if (files != null) p.setDocs(files);

		}

		return p;
	}

	@Transactional
	@CacheEvict(value = {"pageCache"}, allEntries = true)
	public Page savePage(Page page) {
		pageTreeService.resetPageTreeCache();
		return pageRepo.saveAndFlush(page);
	}

	@Transactional
	@CacheEvict(value = {"pageCache"}, allEntries = true)
	public void deletePage(Page page) {
		pageTreeService.resetPageTreeCache();
		deleteSubPages(page);
		// remove images
		imageFileService.removeAllImagesForPage(page);
		
		pageRepo.delete(page);
		logger.debug("Page: " + page.getId() + "/" + page.getName() + " removed");
	}
	
	@Transactional
	@CacheEvict(value = {"pageCache"}, allEntries = true)
	public void deletePage(Integer id) {
		pageTreeService.resetPageTreeCache();
		Page p = pageRepo.findOne(id);
		if (p != null) deletePage(p);
	}
	
	@Transactional
	public void deleteSubPages(Page parent) {
		List<Page> pages = pageRepo.listSubPages(parent.getId(), parent.getSiteId());
        pages.forEach(this::deletePage);
	}

	public Page beforeSave(Page page) {
		if (page.getName().isEmpty()) {
			String pl = StringUtils.convertNameToPath(page.getTitle());
			page.setName(pl);
		}
		return page;
	}

	@Transactional
	public int getMaxSortindex(Page parent) {
		int si = 0;
		List<Page> l = pageRepo.listSubPages(parent.getId(), parent.getSiteId());
		if (l != null && l.size() > 0) {
			Page lastPage = l.get( l.size() - 1 );
			if (lastPage != null) {
				si = lastPage.getSortindex();
			}
		}

		return si;
	}
	
	@Transactional
	@CacheEvict(value = {"pageCache"}, allEntries = true)
	public void movePage(Page p, String direction) {
		pageTreeService.resetPageTreeCache();
		if (p == null) return;
		
		List<Page> pages = pageRepo.listSubPages(p.getParentId(), p.getSiteId());
		
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
	public void movePage(Page p, int dstIndex, int newParentId) {
		Page page = p;
		if (newParentId != p.getParentId()) {
			// change parent
			p.setParentId(newParentId);
			page = pageRepo.saveAndFlush(p);
		}
		setPosition(page, dstIndex);
	}
	
	@Transactional
	public int getPosition(Page p) {
		List<Page> siblings = pageRepo.listSubPages(p.getParentId(), p.getSiteId());
		if (siblings == null) return 0;
		return siblings.indexOf(p);
	}
	
	private void setPosition(Page p, int pos ) {
		List<Page> siblings = pageRepo.listSubPages(p.getParentId(), p.getSiteId());
		int currentIndex = siblings.indexOf(p);
		if (currentIndex != pos) {
			siblings.remove(currentIndex);
			// 12345   2-4 1245 
			siblings.add(pos, p);
			logger.debug("save new order by altering sortindex");
			int idx = 1;
			for (Page pg: siblings) {
				logger.debug(" -- "+ pg.getId() + "/" + pg.getName());
				pg.setSortindex(idx);
				pageRepo.save(pg);
				idx++;
			}
		}
	}
	
	@Transactional
	@CacheEvict(value = {"pageCache"}, allEntries = true)
	public Page copyPage(Page src) {
		pageTreeService.resetPageTreeCache();
		Page newPage = CloneFactory.copyPage(src);
		String newName = findNewPageName(src.getSite(), src.getName());
		newPage.setName(newName);
		String newTitle = src.getTitle() + " - Copy";
		newPage.setTitle(newTitle);
		int sortIndex = getMaxSortindex(src) + 10;
		newPage.setSortindex(sortIndex);
		Page savedNewPage = pageRepo.saveAndFlush(newPage); 
		
		// copy page images
		List<ImageFile> images = imageFileService.listImagesForPage(src.getId());
		if (images != null &&(images.size() > 0)) {
			for(ImageFile im: images) {
				imageFileService.copyFileToPage(im, src, savedNewPage);
			}
		}
		
		return savedNewPage;
	}
	
	@Transactional
	public String findNewPageName(Site site, String name) {
		int idx = 1;
		boolean run = true;
		String newName = name + "-" + idx;
		
		while(run) {
			long c = pageRepo.countPagesForName(site, newName);
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
	
	@CacheEvict(value = {"pageCache"}, allEntries = true)
	public void resetPageCache() {
		pageTreeService.resetPageTreeCache();
	}
	
	@Transactional
	public List<Page> listAll() {
		return pageRepo.findAll();
	}
	
	@Transactional
	public List<Page> listBySite(Site site) {
		List<Page> pages = pageRepo.findBySite(site);
		pages.forEach(p -> p.setMainImage(imageFileService.getMainImageForPage(p.getId())));
		return pages;
	}
	
	@Transactional
	public boolean findOtherPagesWithSameName(Integer pageId, String name) {
		Page p = pageRepo.findOne(pageId);
		long c = pageRepo.countOtherPagesForName(p.getSite(), pageId, name);
		return (c > 0);
	}
	
	@Transactional
	public List<Page> listPageChildren(Page p) {
		List<Page> children = pageRepo.listSubPages(p.getId(), p.getSiteId());
		for(Page c: children) {
			ImageFile f = imageFileService.getMainImageForPage(c.getId());
			c.setMainImage(f);
		}
		return children;
	}
	
}
