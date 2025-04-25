package com.newworld.saegil.notice.service;

import com.newworld.saegil.notice.domain.Notice;
import com.newworld.saegil.notice.domain.NoticeCrawler;
import com.newworld.saegil.notice.domain.NoticeType;
import com.newworld.saegil.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final Set<NoticeCrawler> crawlers;

    public void fetchNewNotices() {
        final long crawlStartTime = System.currentTimeMillis();
        final List<Notice> newNoticesToSave = crawlAllNotices();
        final long crawlEndTime = System.currentTimeMillis();

        log.info("공지사항 크롤링 완료. 크롤링 소요 시간: {} ms", crawlEndTime - crawlStartTime);

        final long saveStartTime = System.currentTimeMillis();
        noticeRepository.saveAll(newNoticesToSave);
        final long saveEndTime = System.currentTimeMillis();

        log.info("공지사항 저장 완료. 저장 소요 시간: {} ms", saveEndTime - saveStartTime);
        log.info("총 크롤링 및 저장 소요 시간: {} ms", saveEndTime - crawlStartTime);
    }

    private List<Notice> crawlAllNotices() {
        final List<Notice> newNoticesToSave = new ArrayList<>();
        for (final NoticeCrawler crawler : crawlers) {
            Set<NoticeType> supportedTypes = crawler.getSupportingNoticeType();
            for (NoticeType noticeType : supportedTypes) {
                final List<Notice> newNotices = crawlAndExtractNewNotices(crawler, noticeType);
                newNoticesToSave.addAll(newNotices);
            }
        }

        log.info("새로운 공지사항 총 개수: {}", newNoticesToSave.size());
        return newNoticesToSave;
    }

    private List<Notice> crawlAndExtractNewNotices(final NoticeCrawler crawler, final NoticeType noticeType) {
        final List<Notice> latestNotices = noticeRepository.findAllLatestNoticeByType(noticeType);
        final LocalDate lastDate = latestNotices.isEmpty() ? null : latestNotices.getFirst().getDate();
        final List<Notice> crawledNotices = crawler.crawl(noticeType, lastDate);

        final List<Notice> newNotices =
                crawledNotices.stream()
                              .filter(newNotice ->
                                      latestNotices.stream()
                                                   .noneMatch(latestNotice -> latestNotice.hasSameTitle(newNotice))
                              ).toList();
        log.info("새로운 {} {} 개수: {}",
                noticeType.getSource(), noticeType.getCategory(), newNotices.size()
        );

        return newNotices;
    }
}
