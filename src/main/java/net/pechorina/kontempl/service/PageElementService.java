package net.pechorina.kontempl.service;

import net.pechorina.kontempl.data.PageElement;
import net.pechorina.kontempl.repos.PageElementRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("pageElementService")
public class PageElementService {
	static final Logger logger = LoggerFactory.getLogger(PageElementService.class);
    
    @Autowired 
    private PageElementRepo pageElementRepo;    
    
    @Transactional
    public void savePageElement(PageElement pe) {
    	pageElementRepo.save(pe);
    }
    
    @Transactional
    public void deletePageElement(PageElement pe) {
    	pageElementRepo.delete(pe);
    }
    
    @Transactional
    public PageElement getPageElementById(Integer id) {
        logger.debug("pageElement id:" + id);
        return pageElementRepo.findOne(id);
    }
}
