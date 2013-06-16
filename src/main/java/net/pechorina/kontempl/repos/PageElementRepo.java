package net.pechorina.kontempl.repos;

import java.util.List;

import net.pechorina.kontempl.data.PageElement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PageElementRepo extends JpaRepository<PageElement, Integer> {
	@Query("select pe from PageElement pe where pe.pageId=:pageId")
	List<PageElement> listPageElements(@Param("pageId") Integer pageId);
}