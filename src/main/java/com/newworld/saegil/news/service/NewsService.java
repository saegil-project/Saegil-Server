package com.newworld.saegil.news.service;

import com.newworld.saegil.news.domain.News;
import com.newworld.saegil.news.domain.NewsCategory;
import com.newworld.saegil.news.domain.NewsQuiz;
import com.newworld.saegil.news.repository.NewsQuizRepository;
import com.newworld.saegil.news.repository.NewsRepository;
import com.newworld.saegil.user.domain.User;
import com.newworld.saegil.user.domain.UserInterest;
import com.newworld.saegil.user.repository.UserInterestRepository;
import com.newworld.saegil.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsQuizRepository newsQuizRepository;
    private final UserRepository userRepository;
    private final UserInterestRepository userInterestRepository;

    public List<NewsCategory> readAllCategories() {
        return Arrays.asList(NewsCategory.values());
    }

    public void setUserInterests(final Long userId, final List<NewsCategory> interests) {
        final User user = userRepository.findById(userId)
                                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        userInterestRepository.deleteAllByUserId(userId);

        final List<UserInterest> userInterests = interests.stream()
                                                          .map(category -> new UserInterest(user.getId(), category))
                                                          .toList();

        userInterestRepository.saveAll(userInterests);
    }

    public List<NewsCategory> readUserInterests(final Long userId) {
        return userInterestRepository.findAllByUserId(userId)
                                     .stream()
                                     .map(UserInterest::getCategory)
                                     .toList();
    }

    public List<NewsDto> readUserInterestNews(final Long userId, final LocalDate newsDate, final int size) {
        final List<NewsCategory> userInterests = readUserInterests(userId);
        if (userInterests.isEmpty()) {
            return Collections.emptyList();
        }

        final List<News> filteredNews = newsRepository.findByCategoryInAndDate(userInterests, newsDate);

        Collections.shuffle(filteredNews);
        return filteredNews.stream()
                           .limit(size)
                           .map(NewsDto::from)
                           .toList();
    }

    public NewsQuizDto readQuizByNewsId(final long newsId) {
        final NewsQuiz newsQuiz = newsQuizRepository.findByNewsId(newsId)
                                                    .orElseThrow(() -> new NewsQuizNotFoundException(
                                                            "해당 뉴스에 대한 퀴즈가 존재하지 않습니다."
                                                    ));

        return NewsQuizDto.from(newsQuiz);
    }
}
