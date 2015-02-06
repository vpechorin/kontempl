package net.pechorina.kontempl.service;

import net.pechorina.kontempl.data.*;
import net.pechorina.kontempl.repos.PageRepo;
import net.pechorina.kontempl.repos.SiteRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PageTreeService {
	static final Logger logger = LoggerFactory.getLogger(PageTreeService.class);

	@Autowired
	private PageRepo pageRepo;
	
	@Autowired
	private SiteRepo siteRepo;
	
	@Autowired
	private ImageFileService imageFileService;
    
	@Autowired
	private DocFileService docFileService;
	
	@Autowired
	private Environment env;

	@Transactional
	public List<Site> getSiteTree() {
		List<Site> sites = siteRepo.findAll();
		
		for(Site site: sites) {
			PageTree pageTree = new PageTree();

			List<Page> pages = pageRepo.listRootPages(site);

			for (Page p : pages) {
				GenericTreeNode<Page> node = new GenericTreeNode<>(p);
				pageTree.addChild(node);
				auxiliaryAddChildren(node);
			}
		}
		
		return sites;
	}
	
	@Transactional
	public PageTree getPageTree(Site site) {
		PageTree pageTree = new PageTree();

		List<Page> pages = pageRepo.listRootPages(site);

		for (Page p : pages) {
			GenericTreeNode<Page> node = new GenericTreeNode<>(p);
			pageTree.addChild(node);
			auxiliaryAddChildren(node);
		}
		
		return pageTree;
	}
	
	private void auxiliaryAddChildren(GenericTreeNode<Page> parent) {
		List<Page> subPages = pageRepo.listSubPages(parent.getData().getId(), parent.getData().getSiteId());

		for (Page child : subPages) {
			GenericTreeNode<Page> node = new GenericTreeNode<>(child);
			node.setParent(parent);
			auxiliaryAddChildren(node);
			parent.addChild(node);
		}
	}
	
	private void auxiliaryAddPublicChildren(GenericTreeNode<Page> parent) {

		List<Page> subPages = pageRepo.listSubPages(parent.getData().getId(), parent.getData().getSiteId());

        // set image for this page
        subPages.stream().filter(Page::isPublicPage).forEach(child -> {
            // set image for this page
            child.setMainImage(imageFileService.getMainImageForPage(child.getId()));

            GenericTreeNode<Page> node = new GenericTreeNode<>(child);
            node.setParent(parent);
            auxiliaryAddPublicChildren(node);
            parent.addChild(node);
        });
	}

	@Transactional
	public PageTree getPublicPageTree(Site site) {
		logger.debug("getPublicPageTree: " + site.getDomain());

		PageTree pageTree = new PageTree();

		// find home page
		Page homePage = pageRepo.findBySiteAndName(site, site.getHomePage());
		
		if (homePage == null) {
			logger.warn("Homepage not found");
			return null;
		}
		
		// set image for this page
		homePage.setMainImage(imageFileService.getMainImageForPage(homePage.getId()));

        GenericTreeNode<Page> home = new GenericTreeNode<>(homePage);
        pageTree.addChild(home);
        auxiliaryAddPublicChildren(home);
        return pageTree;
	}
	
	@Transactional
	public List<PageNode> getPageNodeTree(Site site) {
		List<PageNode> tree = new ArrayList<>();

		List<Page> pages = pageRepo.listRootPages(site);

		for (Page p : pages) {
			PageNode rootNode = new PageNode(p);
			auxiliaryAddChildren(rootNode, p);
			tree.add(rootNode);
		}
		
		return tree;
	}
	
	private void auxiliaryAddChildren(PageNode node, Page parentPage) {
		List<Page> subPages = pageRepo.listSubPages(parentPage.getId(), parentPage.getSiteId());

		for (Page child : subPages) {
			PageNode childNode = new PageNode(child);
			auxiliaryAddChildren(childNode, child);
			node.addChild(childNode);
		}
	}
	
	@Transactional
	@Cacheable("treeCache")
	public List<PageNode> getPageNodeTreePublic(Site site, boolean includeImages, boolean includeFiles) {
		List<PageNode> tree = new ArrayList<>();

		List<Page> pages = pageRepo.listRootPages(site);

        pages.stream().filter(Page::isPublicPage).forEach(p -> {
            PageNode rootNode = new PageNode(p);
            updateAttachments(rootNode, includeImages, includeFiles);
            auxiliaryAddPublicChildren(rootNode, p, includeImages, includeFiles);
            tree.add(rootNode);
        });
		
		return tree;
	}
	
	private void updateAttachments(PageNode node, boolean includeImages, boolean includeFiles) {
		if (includeFiles) {
			List<DocFile> files =  docFileService.listDocsForPageOrdered(node.getId());
			if (files != null) node.setFiles(files);
		}
		if (includeImages) {
			List<ImageFile> images = imageFileService.listImagesForPageOrdered(node.getId());
			if (images != null) node.setImages(images);
		}
	}
	
	private void auxiliaryAddPublicChildren(PageNode node, Page parentPage, boolean includeImages, boolean includeFiles) {
		List<Page> subPages = pageRepo.listSubPages(parentPage.getId(), parentPage.getSiteId());

        subPages.stream().filter(Page::isPublicPage).forEach(child -> {
            PageNode childNode = new PageNode(child);
            updateAttachments(childNode, includeImages, includeFiles);
            auxiliaryAddPublicChildren(childNode, child, includeImages, includeFiles);
            node.addChild(childNode);
        });
	}
	
	@CacheEvict(value = {"treeCache"}, allEntries = true)
	public void resetPageTreeCache() {
		
	}
}
