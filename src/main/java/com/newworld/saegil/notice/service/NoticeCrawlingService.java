package com.newworld.saegil.notice.service;

import com.newworld.saegil.notice.domain.Notice;
import com.newworld.saegil.notice.domain.NoticeCrawler;
import com.newworld.saegil.notice.domain.NoticeType;
import com.newworld.saegil.notice.repository.NoticeRepository;
import com.newworld.saegil.notification.service.NotificationHelper;
import com.newworld.saegil.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.joining;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NoticeCrawlingService {

    private final NoticeRepository noticeRepository;
    private final Set<NoticeCrawler> crawlers;
    private final NotificationHelper notificationHelper;
    private final UserRepository userRepository;

    public void fetchNewNotices() {
        final long crawlStartTime = System.currentTimeMillis();
        final List<Notice> newNoticesToSave = crawlAllNotices();
        final long crawlEndTime = System.currentTimeMillis();

        log.info("공지사항 크롤링 완료. 크롤링 소요 시간: {} ms", crawlEndTime - crawlStartTime);

        sortByDateAsc(newNoticesToSave);

        final long saveStartTime = System.currentTimeMillis();
        noticeRepository.saveAll(newNoticesToSave);
        final long saveEndTime = System.currentTimeMillis();

        log.info("공지사항 저장 완료. 저장 소요 시간: {} ms", saveEndTime - saveStartTime);
        log.info("총 크롤링 및 저장 소요 시간: {} ms", saveEndTime - crawlStartTime);

        if (!newNoticesToSave.isEmpty()) {
            sendNotificationForNewNotices(newNoticesToSave);
        }
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

    private void sortByDateAsc(final List<Notice> newNoticesToSave) {
        newNoticesToSave.sort(((o1, o2) -> {
            if (o1.getDate() != null && o2.getDate() != null) {
                return o1.getDate().compareTo(o2.getDate());
            }
            if (o1.getDate() == null && o2.getDate() == null) {
                return 0;
            }
            if (o1.getDate() != null && o2.getDate() == null) {
                return 1;
            } else {
                return -1;
            }
        }));
    }

    private void sendNotificationForNewNotices(final List<Notice> newNotices) {
        try {
            final List<Long> userIds = userRepository.findAllUserIdsWithDeviceToken();

            if (userIds.isEmpty()) {
                log.info("알림을 보낼 사용자가 없습니다.");
                return;
            }

            final String title = String.format("%d개의 새로운 공지사항이 등록되었습니다.", newNotices.size());
            final String body = newNotices.stream()
                                          .map(Notice::getTitle)
                                          .collect(joining("\n"));

            notificationHelper.publishNotificationEvent(title, body, userIds);

            log.info("새로운 공지사항 알림 이벤트 발행 완료 - 사용자 수: {}", userIds.size());
        } catch (Exception e) {
            log.error("새로운 공지사항 알림 발송 중 오류 발생: {}", e.getMessage());
        }
    }
}
