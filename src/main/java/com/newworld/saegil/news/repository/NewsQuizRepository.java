package com.newworld.saegil.news.repository;

import com.newworld.saegil.news.domain.NewsQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsQuizRepository extends JpaRepository<NewsQuiz, Long> {

    List<NewsQuiz> findAllByNewsId(Long newsId);
}
