package net.pechorina.kontempl.repos;

import java.util.List;

import net.pechorina.kontempl.data.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepo extends JpaRepository<Role, Integer> {
	
	Role findByName(String name);
	
	@Query("select r from Role r order by r.name asc, r.dscr asc")
	List<Role> listAll();
}