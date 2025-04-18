package com.newworld.saegil.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.newworld.saegil.authentication.domain.OAuth2Type;
import com.newworld.saegil.user.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByOauth2IdAndOauth2Type(String oauth2Id, OAuth2Type oauth2Type);
}
