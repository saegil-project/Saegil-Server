package com.newworld.saegil.news.repository;

import com.newworld.saegil.news.domain.NewsQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsQuizRepository extends JpaRepository<NewsQuiz, Long> {

    Optional<NewsQuiz> findByNewsId(Long newsId);
}
