package net.pechorina.kontempl.repos

import java.util.List;

import net.pechorina.kontempl.data.ImageFile;
import net.pechorina.kontempl.data.Site;
import net.pechorina.kontempl.data.Thumbnail;
import net.pechorina.kontempl.data.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface ImageFileRepo extends JpaRepository<ImageFile, Integer> {
	List<ImageFile> findByPageId(Integer pageId);
	
	@Query("select img from ImageFile img where img.pageId=:pageId and img.mainImage = true order by img.id desc")
	List<ImageFile> locateMainImageForPage(@Param("pageId") Integer pageId);
	
	@Query("select img from ImageFile img where img.pageId=:pageId order by img.mainImage desc, img.sortIndex asc, img.name asc")
	List<ImageFile> getPageImagesOrdered(@Param("pageId") Integer pageId);
	
	@Query("select MAX(sortIndex) from ImageFile img where img.pageId=:pageId")
	Integer getMaxSortIndexForImages(@Param("pageId") Integer pageId);
	
	@Query("select COUNT(img) from ImageFile img where img.pageId=:pageId")
	Long countImagesForPage(@Param("pageId") Integer pageId);

}
