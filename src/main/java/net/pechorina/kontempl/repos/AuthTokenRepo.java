package net.pechorina.kontempl.repos;

import java.util.List;

import net.pechorina.kontempl.data.AuthToken;
import net.pechorina.kontempl.data.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthTokenRepo extends JpaRepository<AuthToken, String> {
	List<AuthToken> findByUser(User user);
}