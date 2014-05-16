package net.pechorina.kontempl.service;

import java.util.List;

import net.pechorina.kontempl.data.GenericTreeNode;
import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.data.PageTree;
import net.pechorina.kontempl.repos.PageRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("pageTreeService")
public class PageTreeService {
	static final Logger logger = LoggerFactory.getLogger(PageTreeService.class);

	@Autowired
	private PageRepo pageRepo;
	
	@Autowired
	private ImageFileService imageFileService;
    
	@Autowired
	@Qualifier("appConfig")
	public java.util.Properties appConfig;

	@Transactional
	public PageTree getPageTree() {
		PageTree pageTree = new PageTree();

		List<Page> pages = pageRepo.listSubPages(0);

		for (Page p : pages) {
			GenericTreeNode<Page> node = new GenericTreeNode<Page>(p);
			pageTree.addChild(node);
			auxiliaryAddChildren(node);
		}
		
		return pageTree;
	}
	
	private void auxiliaryAddChildren(GenericTreeNode<Page> parent) {
		Integer parentId = parent.getData().getId();
		List<Page> subPages = pageRepo.listSubPages(parentId);

		for (Page child : subPages) {
			GenericTreeNode<Page> node = new GenericTreeNode<Page>(child);
			node.setParent(parent);
			auxiliaryAddChildren(node);
			parent.addChild(node);
		}
	}
	
	private void auxiliaryAddPublicChildren(GenericTreeNode<Page> parent) {
		Integer parentId = parent.getData().getId();
		List<Page> subPages = pageRepo.listSubPages(parentId);

		for (Page child : subPages) {
			if (child.isPublicPage()) {
				// set image for this page
				child.setMainImage(imageFileService.getMainImageForPage(child.getId()));
				
				GenericTreeNode<Page> node = new GenericTreeNode<Page>(child);
				node.setParent(parent);
				auxiliaryAddPublicChildren(node);
				parent.addChild(node);
			}
		}
	}

	@Transactional
	@Cacheable("publicPageTreeCache")
	public PageTree getPublicPageTree() {
		logger.debug("getPublicPageTree");
		PageTree pageTree = new PageTree();

		// find home page
		Page homePage = pageRepo.findByName(appConfig.getProperty("homePage"));
		
		if (homePage == null) {
			logger.warn("Homepage not found");
			return null;
		}
		
		// set image for this page
		homePage.setMainImage(imageFileService.getMainImageForPage(homePage.getId()));
		
		if (homePage != null) {
			GenericTreeNode<Page> home = new GenericTreeNode<Page>(homePage);
			pageTree.addChild(home);
			auxiliaryAddPublicChildren(home);
		}
		return pageTree;
	}
	
	
}
