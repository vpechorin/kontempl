package net.pechorina.kontempl.repos

import net.pechorina.kontempl.data.Credential
import org.springframework.data.jpa.repository.JpaRepository

interface CredentialRepo extends JpaRepository<Credential, Integer> {
    Credential findByUid(String uid);
}
