package net.pechorina.kontempl.repos

import net.pechorina.kontempl.data.DataForm
import org.springframework.data.jpa.repository.JpaRepository

interface DataFormRepo extends JpaRepository<DataForm, Integer> {

    DataForm findByName(String formName)

    DataForm findByNameAndSiteId(String formName, int siteId)

    List<DataForm> findBySiteId(int siteId)
}
