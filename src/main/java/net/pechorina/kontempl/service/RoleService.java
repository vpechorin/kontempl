package net.pechorina.kontempl.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.pechorina.kontempl.data.Role;
import net.pechorina.kontempl.repos.RoleRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("roleService")
public class RoleService {

	static final Logger logger = LoggerFactory.getLogger(RoleService.class);

	@Autowired
	private RoleRepo roleRepo;
	
	@Transactional
	public List<Role> listAllRoles() {
		return roleRepo.listAll();
	}
	
	@Transactional
	public Map<String,String> getRoleMap() {
        Map<String,String> roleMap = new LinkedHashMap<String,String>();
        List<Role> l = roleRepo.listAll();
        if (l != null) {
        	for(Role r: l) {
        		roleMap.put(r.getName(), r.getDscr());
        	}
        }
        
        return roleMap;
	}
	
}
