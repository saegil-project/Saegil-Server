package com.newworld.saegil.notice.domain.crawler;

import com.newworld.saegil.notice.domain.Notice;
import com.newworld.saegil.notice.domain.NoticeCrawler;
import com.newworld.saegil.notice.domain.NoticeType;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class UniKoreaCrawler implements NoticeCrawler {

    @Override
    public Set<NoticeType> getSupportingNoticeType() {
        return Set.of(
                NoticeType.UNIKOREA_NOTICE,
                NoticeType.UNIKOREA_RECRUIT
        );
    }

    @Override
    public List<Notice> crawl(final NoticeType noticeType, final LocalDate lastDate) {
        final long crawlStartTime = System.currentTimeMillis();
        log.info("{} {} 크롤링 시작", noticeType.getSource(), noticeType.getCategory());
        final List<Notice> newNotices = new ArrayList<>();
        int page = 1;
        boolean done = false;

        while (!done) {
            final String url = noticeType.getBoardUrlPrefix() + page;
            try {
                final Document document = Jsoup.connect(url).get();
                final Elements rows = document.select("table.bbsList tbody tr");

                for (final Element row : rows) {
                    final Notice notice = parseToNotice(row, noticeType);
                    if (notice == null || notice.isBefore(lastDate)) {
                        done = true;
                        break;
                    }
                    newNotices.add(notice);
                }

                page++;
            } catch (final Exception e) {
                log.error("Error while crawling {}: {}", url, e.getMessage());
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

    private Notice parseToNotice(final Element element, final NoticeType noticeType) {
        final Element titleElement = element.selectFirst("td.title a");
        if (titleElement == null) {
            return null;
        }
        final String title = titleElement.text().trim();
        final String href = titleElement.attr("href").replace("&amp;", "&");
        final String webLink = noticeType.getBaseUrl() + href;
        final Element dateElement = element.selectFirst("td.created");
        final String dateValue = (dateElement != null) ? dateElement.text() : null;
        final LocalDate date = parseDate(dateValue, webLink);

        return new Notice(
                title,
                "",
                noticeType,
                date,
                webLink
        );
    }

    private LocalDate parseDate(final String dateValue, final String webLink) {
        if (dateValue == null) {
            return null;
        }

        final String formatted = dateValue.replace(" ", "").substring(0, 10);
        try {
            return LocalDate.parse(formatted);
        } catch (final DateTimeParseException e) {
            log.warn("게시글 등록일 파싱에 실패했습니다. webLink = {}, dateValue = {}", webLink, dateValue);
            return null;
        }
    }
}
