package com.newworld.saegil.notice.domain.crawler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.newworld.saegil.notice.domain.Notice;
import com.newworld.saegil.notice.domain.NoticeCrawler;
import com.newworld.saegil.notice.domain.NoticeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeoulForeignerPortalCrawler implements NoticeCrawler {

    @Value("${openapi.seoul-data.service-key}")
    private String serviceKey;

    private final RestTemplate restTemplate;

    @Override
    public Set<NoticeType> getSupportingNoticeType() {
        return Set.of(NoticeType.SEOUL_FOREIGNER_PORTAL_NOTICE);
    }

    @Override
    public List<Notice> crawl(final NoticeType noticeType, final LocalDate lastDate) {
        final long crawlStartTime = System.currentTimeMillis();
        log.info("{} {} 크롤링 시작", noticeType.getSource(), noticeType.getCategory());
        final List<Notice> newNotices = new ArrayList<>();
        int startIndex = 1;
        int endIndex = 500;
        int requestCount = 500;
        boolean done = false;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (!done) {
            final String requestUri = String.format(
                    "http://openapi.seoul.go.kr:8088/%s/json/TBordCont9/%d/%d/",
                    serviceKey, startIndex, endIndex
            );
            try {
                final SeoulForeignerPortalResponse response = restTemplate.getForObject(requestUri, SeoulForeignerPortalResponse.class);
                if (response == null || response.isItemEmpty()) {
                    break;
                }
                for (final SeoulForeignerPortalItem item : response.getItems()) {
                    final String title = item.title();
                    final String content = item.content();
                    final String dateStr = item.date();
                    final LocalDate date = dateStr != null ? LocalDate.parse(dateStr, formatter) : null;
                    final String webLink = "https://global.seoul.go.kr/web/news/libr/bordContListPage.do?brd_no=9";
                    final Notice notice = new Notice(
                            title,
                            content,
                            noticeType,
                            date,
                            webLink
                    );
                    if (notice.isBefore(lastDate)) {
                        done = true;
                        break;
                    }
                    newNotices.add(notice);
                }
                startIndex += requestCount;
                endIndex += requestCount;
            } catch (final Exception e) {
                log.error("크롤링 중 오류 발생: {}", e.getMessage());
                break;
            }
        }
        final long crawlEndTime = System.currentTimeMillis();
        log.info("{} {} 크롤링 완료. 날짜({})일 이후의 게시물 총 {}개를 수집했습니다.",
                noticeType.getSource(), noticeType.getCategory(), lastDate, newNotices.size()
        );
        log.info("{} {} 크롤링 소요 시간: {}ms", noticeType.getSource(), noticeType.getCategory(), crawlEndTime - crawlStartTime);
        return newNotices;
    }

    public record SeoulForeignerPortalResponse(
            @JsonProperty("TBordCont9") SeoulForeignerPortalResponseDetail detail
    ) {
        public boolean isItemEmpty() {
            return detail == null || detail.isItemEmpty();
        }

        public List<SeoulForeignerPortalItem> getItems() {
            return detail == null ? Collections.emptyList() : detail.getItems();
        }
    }

    public record SeoulForeignerPortalResponseDetail(
            @JsonProperty("row") List<SeoulForeignerPortalItem> items
    ) {
        public boolean isItemEmpty() {
            return items == null || items.isEmpty();
        }

        public List<SeoulForeignerPortalItem> getItems() {
            return items;
        }
    }

    public record SeoulForeignerPortalItem(
            @JsonProperty("TITL_NM") String title,
            @JsonProperty("CONT") String content,
            @JsonProperty("REG_DT") String date
    ) {}
}
