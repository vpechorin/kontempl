package net.pechorina.kontempl.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.pechorina.kontempl.data.PageElementType;
import net.pechorina.kontempl.repos.PageElementTypeRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("pageElementTypeService")
public class PageElementTypeService {

	static final Logger logger = LoggerFactory.getLogger(PageElementTypeService.class);

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
	
	@Transactional
	public List<String> getTypeList() {
		List<String> typeList = new ArrayList<String>();
        List<PageElementType> l = pageElementTypeRepo.listAll();
        if (l != null) {
        	for(PageElementType r: l) {
        		typeList.add(r.getName());
        	}
        }
		
        return typeList;
	}
	
}
