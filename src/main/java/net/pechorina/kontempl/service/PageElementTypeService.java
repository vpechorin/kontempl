package net.pechorina.kontempl.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.pechorina.kontempl.data.PageElementType;
import net.pechorina.kontempl.repos.PageElementTypeRepo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("pageElementTypeService")
public class PageElementTypeService {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(PageElementTypeService.class);

	@Autowired
	private PageElementTypeRepo pageElementTypeRepo;
	
	@Transactional
	public Map<String,String> getTypeMap() {
        Map<String,String> typeMap = new LinkedHashMap<String,String>();
        List<PageElementType> l = pageElementTypeRepo.listAll();
        if (l != null) {
        	for(PageElementType r: l) {
        		typeMap.put(r.getName(), " [" + r.getName() + "] " +r.getDescription());
        	}
        }
        
        return typeMap;
	}
	
}
