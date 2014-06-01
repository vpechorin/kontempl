package net.pechorina.kontempl.repos;

import net.pechorina.kontempl.data.AuthToken;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthTokenRepo extends JpaRepository<AuthToken, String> {
}