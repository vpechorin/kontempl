package net.pechorina.kontempl.repos;

import java.util.List;

import net.pechorina.kontempl.data.Page;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PageRepo extends JpaRepository<Page, Integer> {
	Page findByName(String pageName);
	
	@Query("select p from Page p where p.parentId=:parentId order by sortindex asc, name asc")
	List<Page> listSubPages(@Param("parentId") Integer parentId);
	
	@Query("select COUNT(p) from Page p where p.name=:pageName")
	Long countPagesForName(@Param("pageName") String pageName);
}