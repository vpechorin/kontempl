package net.pechorina.kontempl.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.pechorina.kontempl.data.GenericTreeNode;
import net.pechorina.kontempl.data.Page;
import net.pechorina.kontempl.data.PageTree;
import net.pechorina.kontempl.repos.PageElementRepo;
import net.pechorina.kontempl.repos.PageRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("pageNavService")
public class PageNavigationService {

	static final Logger logger = LoggerFactory.getLogger(PageNavigationService.class);

	@Autowired
	private PageRepo pageRepo;
	
    @Autowired
    private PageElementRepo pageElementRepo;
    
    @Autowired
    private ImageFileService imageFileService;
    
    @Autowired
    private PageTreeService pageTreeService;
    
	@Autowired
	@Qualifier("appConfig")
	public java.util.Properties appConfig;
	
	@Transactional
	public List<Page> getBreadcrumbs(Page page) {
		List<Page> pages = new ArrayList<Page>();
		PageTree tree = pageTreeService.getPublicPageTree();
		
		GenericTreeNode<Page> n = tree.findPageNode(page.getId());
		
		boolean r = true;
		do {
			GenericTreeNode<Page> parent = n.getParent();
			if ((parent != null) && (parent.getData().isPublicPage()) ) {
				pages.add(parent.getData());
				n = parent;
			}
			else {
				r = false;
			}
		}
		while(r);
		
		Collections.reverse(pages);
		pages.add(page);
		
		return pages;
	}


}
