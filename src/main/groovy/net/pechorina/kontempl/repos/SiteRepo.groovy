package net.pechorina.kontempl.repos

import net.pechorina.kontempl.data.Site
import org.springframework.data.jpa.repository.JpaRepository

interface SiteRepo extends JpaRepository<Site, Integer> {
    Site findByName(String siteName)
}
