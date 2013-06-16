package net.pechorina.kontempl.repos;

import java.util.List;

import net.pechorina.kontempl.data.PageElementType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PageElementTypeRepo extends JpaRepository<PageElementType, Integer> {
	
	@Query("select pe from PageElementType pe order by pe.name asc, pe.description asc")
	List<PageElementType> listAll();
}