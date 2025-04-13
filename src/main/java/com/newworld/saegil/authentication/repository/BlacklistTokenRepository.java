package com.newworld.saegil.authentication.repository;

import com.newworld.saegil.authentication.domain.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken, Long> {

    boolean existsByUserIdAndToken(Long userId, String token);
}
