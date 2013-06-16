package net.pechorina.kontempl.repos;

import java.util.List;

import net.pechorina.kontempl.data.Thumbnail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ThumbnailRepo extends JpaRepository<Thumbnail, Integer> {
	List<Thumbnail> findByPageId(Integer pageId);
	
	@Query("select img from Thumbnail img where img.pageId=:pageId order by img.imageFileId asc")
	List<Thumbnail> getPageThumbnailsOrdered(@Param("pageId") Integer pageId);
	
	Thumbnail findByImageFileId(Integer imageFileId);
}