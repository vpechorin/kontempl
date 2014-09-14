package net.pechorina.kontempl.repos

import java.util.List;

import net.pechorina.kontempl.data.Site;
import net.pechorina.kontempl.data.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface SiteRepo extends JpaRepository<Site, Integer> {
	Site findByName(String siteName)
}
