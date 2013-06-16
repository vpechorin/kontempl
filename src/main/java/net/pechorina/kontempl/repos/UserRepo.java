package net.pechorina.kontempl.repos;

import java.util.List;

import net.pechorina.kontempl.data.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepo extends JpaRepository<User, Integer> {

	@Query("select u from User u where u.active=true order by name asc")
	List<User> listActiveUsers();
}