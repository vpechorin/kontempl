package net.pechorina.kontempl.repos

import net.pechorina.kontempl.data.Page
import net.pechorina.kontempl.data.Site
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PageRepo extends JpaRepository<Page, Integer> {
    Page findByName(String pageName);

    Page findBySiteAndName(Site site, String pageName);

    @Query("select p from Page p where p.parentId=:parentId and p.siteId=:siteId order by sortindex asc, name asc")
    List<Page> listSubPages(@Param("parentId") Integer parentId, @Param("siteId") Integer siteId);

    @Query("select p from Page p where p.parentId=0 and p.site=:site order by sortindex asc, name asc")
    List<Page> listRootPages(@Param("site") Site site);

    List<Page> findBySite(Site site);

    @Query("select COUNT(p) from Page p where p.name=:pageName and p.site=:site")
    Long countPagesForName(@Param("site") Site site, @Param("pageName") String pageName);

    @Query("select COUNT(p) from Page p where p.name=:pageName and p.site=:site and p.id != :id")
    Long countOtherPagesForName(@Param("site") Site site, @Param("id") Integer id, @Param("pageName") String pageName);
}
