package net.pechorina.kontempl.repos

import net.pechorina.kontempl.data.DocFile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface DocFileRepo extends JpaRepository<DocFile, Integer> {
    List<DocFile> findByPageId(Integer pageId);

    @Query("select f from DocFile f where f.pageId=:pageId order by f.sortIndex asc, f.name asc")
    List<DocFile> getPageDocsOrdered(@Param("pageId") Integer pageId);

    @Query("select MAX(sortIndex) from DocFile f where f.pageId=:pageId")
    Integer getMaxSortIndex(@Param("pageId") Integer pageId);

    @Query("select COUNT(f) from DocFile f where f.pageId=:pageId")
    Long countDocsForPage(@Param("pageId") Integer pageId);

}