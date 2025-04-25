package com.newworld.saegil.notice.domain.crawler;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Slf4j
@Component
public class NoticeCrawlerHelper {

    public String extractContentBody(final String webLink, final String elementSelector) throws IOException {
        final Document document = Jsoup.connect(webLink).get();
        final Element contentElement = document.selectFirst(elementSelector);

        return contentElement != null ? contentElement.text().substring(0, 100) : "";
    }

    public LocalDate parseDate(final String dateValue, final String webLink) {
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
