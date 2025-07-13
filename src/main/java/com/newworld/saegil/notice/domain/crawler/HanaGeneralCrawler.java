package com.newworld.saegil.notice.domain.crawler;

import com.newworld.saegil.notice.domain.Notice;
import com.newworld.saegil.notice.domain.NoticeCrawler;
import com.newworld.saegil.notice.domain.NoticeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
//@Component
@RequiredArgsConstructor
public class HanaGeneralCrawler implements NoticeCrawler {

    private final NoticeCrawlerHelper noticeCrawlerHelper;

    @Override
    public Set<NoticeType> getSupportingNoticeType() {
        return Set.of(
                NoticeType.HANA_RECRUIT_NOTICE,
                NoticeType.HANA_GENERAL_ANNOUNCEMENT,
                NoticeType.HANA_JOB_OPENING,
                NoticeType.HANA_HOUSING_NOTICE,
                NoticeType.HANA_RELATED_AGENCY_NOTICE,
                NoticeType.HANA_NEWS_MATERIAL,
                NoticeType.HANA_PUBLIC_NOTIFICATION
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
                final Elements rows = document.select("table.board_list tbody tr");

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
                log.error("크롤링 중 오류 발생: " + e.getMessage());
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

    private Notice parseToNotice(final Element element, final NoticeType noticeType) throws IOException {
        final Element titleElement = element.selectFirst("td.tit a");
        if (titleElement == null) {
            return null;
        }
        final String title = titleElement.text().trim();
        final String onclick = titleElement.attr("onclick").trim();
        final String id = onclick.replaceAll(".*'detail',\\s*'(\\d+)',.*", "$1");
        final String webLink = noticeType.getDetailUrlPrefix() + id;
        final String content = noticeCrawlerHelper.extractContentBody(webLink, "div.editor_view");

        final Elements tds = element.select("td");
        final String dateValue = (tds.size() > 5) ? tds.get(5).text() : null;
        final LocalDate date = noticeCrawlerHelper.parseDate(dateValue, webLink);

        return new Notice(
                title,
                content,
                noticeType,
                date,
                webLink
        );
    }
}
