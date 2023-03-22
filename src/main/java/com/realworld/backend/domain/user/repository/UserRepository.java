package com.realworld.backend.domain.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.realworld.backend.domain.user.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	@Query("SELECT u FROM UserEntity u WHERE u.username = :username OR u.email = :email")
	List<UserEntity> findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);
	
	Optional<UserEntity> findByEmail(String email);
	
	Optional<UserEntity> findByUsername(String username);
}
