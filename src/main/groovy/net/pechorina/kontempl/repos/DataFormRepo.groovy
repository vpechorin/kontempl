package net.pechorina.kontempl.repos

import java.util.List;

import net.pechorina.kontempl.data.DataForm
import net.pechorina.kontempl.data.ImageFile;
import net.pechorina.kontempl.data.Site;
import net.pechorina.kontempl.data.Thumbnail;
import net.pechorina.kontempl.data.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface DataFormRepo extends JpaRepository<DataForm, Integer> {
	
	DataForm findByName(String formName)
	
	DataForm findByNameAndSiteId(String formName, int siteId)
}
