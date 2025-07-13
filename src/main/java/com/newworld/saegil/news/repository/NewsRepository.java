package com.newworld.saegil.news.repository;

import com.newworld.saegil.news.domain.News;
import com.newworld.saegil.news.domain.NewsCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {

    List<News> findByCategoryInAndDate(List<NewsCategory> categories, LocalDate date);
} 
