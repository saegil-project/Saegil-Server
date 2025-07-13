package com.newworld.saegil.user.repository;

import com.newworld.saegil.user.domain.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {

    List<UserInterest> findAllByUserId(Long userId);

    void deleteAllByUserId(Long userId);
} 
