package net.pechorina.kontempl.repos

import java.util.List;

import net.pechorina.kontempl.data.Credential;
import net.pechorina.kontempl.data.Site;
import net.pechorina.kontempl.data.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface CredentialRepo extends JpaRepository<Credential, Integer> {
	Credential findByUid(String uid);
}
