package net.pechorina.kontempl.repos

import net.pechorina.kontempl.data.AuthToken
import net.pechorina.kontempl.data.User
import org.springframework.data.jpa.repository.JpaRepository

interface AuthTokenRepo extends JpaRepository<AuthToken, String> {
    List<AuthToken> findByUser(User user);
}
